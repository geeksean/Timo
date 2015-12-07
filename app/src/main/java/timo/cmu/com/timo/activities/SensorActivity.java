package timo.cmu.com.timo.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import timo.cmu.com.timo.R;
import timo.cmu.com.timo.model.SensorAccessSetting;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class SensorActivity extends AppCompatActivity {

    //private static int SENSOR_NUMS = 13;
    private String pkgName;
    private String appName;
    private Switch[] switches = new Switch[SensorAccessSetting.SENSOR_NUMS];
    private TextView[] textViews = new TextView[SensorAccessSetting.SENSOR_NUMS];
    private Button confirmButton;
    private Context mContext;
    private SensorAccessSetting[] sensorAccessSettings = new SensorAccessSetting[SensorAccessSetting.SENSOR_NUMS];


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_sensor);
        Bundle bundle = getIntent().getExtras();
        pkgName = bundle.getString("pkgName");
        appName = bundle.getString("appName");
        setTitle(appName);

        confirmButton = (Button) this.findViewById(R.id.button);


        // pressure sensor
        switches[Sensor.TYPE_PRESSURE] = (Switch) this.findViewById(R.id.accuracy_switch_1);
        textViews[Sensor.TYPE_PRESSURE] = (TextView) this.findViewById(R.id.freq_min1);
        sensorAccessSettings[Sensor.TYPE_PRESSURE] = new SensorAccessSetting("Pressure", true, false, 0);
        update(Sensor.TYPE_PRESSURE);

        // proximity sensor
        switches[Sensor.TYPE_PROXIMITY] = (Switch) this.findViewById(R.id.accuracy_switch_2);
        textViews[Sensor.TYPE_PROXIMITY] = (TextView) this.findViewById(R.id.freq_min2);
        sensorAccessSettings[Sensor.TYPE_PROXIMITY] = new SensorAccessSetting("Proximity", true, false, 0);
        update(Sensor.TYPE_PROXIMITY);




        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SensorActivity.this, sensorAccessSettings[Sensor.TYPE_PRESSURE].toString() + "\n" +
                        sensorAccessSettings[Sensor.TYPE_PROXIMITY].toString(), Toast.LENGTH_SHORT).show();

                SharedPreferences sh = mContext.getSharedPreferences(pkgName+"_prefs", Context.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = sh.edit();
                editor.putString("i", prefToString(sensorAccessSettings));
                editor.commit();
            }
        });

    }

    private String prefToString(SensorAccessSetting[] sensorAccessSettings){
        String res = "";
        for (SensorAccessSetting sd: sensorAccessSettings){
            res += (sd == null?"null":sd.toString());
            res += ";";
        }
        return res;

    }

    private void update(final int sensor_type) {
        switches[sensor_type].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sensorAccessSettings[sensor_type].isAccurate = isChecked;
        }
        });

        textViews[sensor_type].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String limit = textViews[sensor_type].getText().toString();
                int limitVal = 0;
                try {
                    limitVal = Integer.parseInt(limit);
                } catch (Exception e) {
                    limitVal = 0;
                }
                sensorAccessSettings[sensor_type].freqPerSec = limitVal;
                sensorAccessSettings[sensor_type].limitFreq = !limit.isEmpty();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
