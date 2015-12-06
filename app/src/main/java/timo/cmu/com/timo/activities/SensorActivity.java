package timo.cmu.com.timo.activities;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import timo.cmu.com.timo.FrequencyManager;
import timo.cmu.com.timo.R;
import timo.cmu.com.timo.Timo;
import timo.cmu.com.timo.model.SensorData;

import java.util.*;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class SensorActivity extends AppCompatActivity {

    private static int NUM_SENSOR = 13;
    private String pkgName;
    private String appName;
    private Switch[] switches = new Switch[NUM_SENSOR];
    private TextView[] textViews = new TextView[NUM_SENSOR];
    private Button confirmButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Bundle bundle = getIntent().getExtras();
        pkgName = bundle.getString("pkgName");
        appName = bundle.getString("appName");
        setTitle(appName);

        confirmButton = (Button) this.findViewById(R.id.button);

        SensorData[] sensorDatas = new SensorData[NUM_SENSOR];
        Timo.settings.put(pkgName, sensorDatas);

        // pressure sensor
        switches[Sensor.TYPE_PRESSURE] = (Switch) this.findViewById(R.id.accuracy_switch_1);
        textViews[Sensor.TYPE_PRESSURE] = (TextView) this.findViewById(R.id.freq_min1);
        sensorDatas[Sensor.TYPE_PRESSURE] = new SensorData("Pressure", true, false, 0);
        update(Sensor.TYPE_PRESSURE);

        // proximity sensor
        switches[Sensor.TYPE_PROXIMITY] = (Switch) this.findViewById(R.id.accuracy_switch_2);
        textViews[Sensor.TYPE_PROXIMITY] = (TextView) this.findViewById(R.id.freq_min2);
        sensorDatas[Sensor.TYPE_PROXIMITY] = new SensorData("Proximity", true, false, 0);
        update(Sensor.TYPE_PROXIMITY);




        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SensorActivity.this, Timo.settings.get(pkgName)[Sensor.TYPE_PRESSURE].toString() + "\n" +
                        Timo.settings.get(pkgName)[Sensor.TYPE_PROXIMITY].toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void update(final int sensor_type) {
        switches[sensor_type].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timo.settings.get(pkgName)[sensor_type].isAccurate = isChecked;
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
                Timo.settings.get(pkgName)[sensor_type].freqPerMin = limitVal;
                Timo.settings.get(pkgName)[sensor_type].limitFreq = !limit.isEmpty();
                FrequencyManager fm = new FrequencyManager(limitVal, 60 * 1000); //60 senconds, 6000 miliseconds
                Timo.addFrequencyManager(sensor_type,fm);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
