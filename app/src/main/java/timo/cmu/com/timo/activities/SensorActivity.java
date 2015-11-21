package timo.cmu.com.timo.activities;

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

import timo.cmu.com.timo.R;
import timo.cmu.com.timo.model.SensorData;

import java.util.*;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class SensorActivity extends AppCompatActivity {
    public static Map<String, SensorData[]> settings = new HashMap<String, SensorData[]>();

    private static int NUM_SENSOR = 2;
    private String pkgName;
    private String appName;
    private Switch[] switches = new Switch[NUM_SENSOR];
    private TextView[] textViews = new TextView[NUM_SENSOR];
    private SensorData[] sensorDatas = new SensorData[NUM_SENSOR];
    private Button confirmButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Bundle bundle = getIntent().getExtras();
        pkgName = bundle.getString("pkgName");
        appName = bundle.getString("appName");
        setTitle(appName);

        confirmButton = (Button) this.findViewById(R.id.button);
        // pressure sensor
        switches[0] = (Switch) this.findViewById(R.id.accuracy_switch_1);
        textViews[0] = (TextView) this.findViewById(R.id.freq_min1);
        sensorDatas[0] = new SensorData("Pressure", true, false, 0);
        update(0);

        // proximity sensor
        switches[1] = (Switch) this.findViewById(R.id.accuracy_switch_2);
        textViews[1] = (TextView) this.findViewById(R.id.freq_min2);
        sensorDatas[1] = new SensorData("Proximity", true, false, 0);
        update(1);

        settings.put(pkgName, sensorDatas);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SensorActivity.this, settings.get(pkgName)[0].toString() + "\n" +
                        settings.get(pkgName)[1].toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void update(final int i) {
        switches[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sensorDatas[i].isAccurate = isChecked;
            }
        });

        textViews[i].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String limit = textViews[i].getText().toString();
                int limitVal = 0;
                try {
                    limitVal = Integer.parseInt(limit);
                } catch (Exception e) {
                    limitVal = 0;
                }
                sensorDatas[i].freqPerMin = limitVal;
                sensorDatas[i].limitFreq = !limit.isEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
