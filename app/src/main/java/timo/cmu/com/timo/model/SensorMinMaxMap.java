package timo.cmu.com.timo.model;

import android.hardware.Sensor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aihua on 12/6/15.
 */
public class SensorMinMaxMap {
    public static Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>(){{
        this.put(Sensor.TYPE_LIGHT, new ArrayList<Integer>(){{
            this.add(0);
            this.add(200);
        }});
        this.put(Sensor.TYPE_PRESSURE, new ArrayList<Integer>(){{
            this.add(850);
            this.add(1100);
        }});
        this.put(Sensor.TYPE_PROXIMITY, new ArrayList<Integer>(){{
            this.add(0);
            this.add(5);
        }});
        this.put(Sensor.TYPE_MAGNETIC_FIELD, new ArrayList<Integer>(){{
            this.add(-1);
            this.add(1);
            this.add(-50);
            this.add(50);
            this.add(-50);
            this.add(50);
        }});
    }};


}
