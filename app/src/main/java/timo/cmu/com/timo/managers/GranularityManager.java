package timo.cmu.com.timo.managers;

import android.hardware.Sensor;

import de.robv.android.xposed.XposedBridge;
import timo.cmu.com.timo.model.GranularityType;
import timo.cmu.com.timo.model.SensorMinMaxMap;

/**
 * Created by aihua on 11/21/15.
 */
public class GranularityManager {
    public static void  getGranularityValue(GranularityType TYPE, float[] value, int type){
        XposedBridge.log("Light type: "+Sensor.TYPE_LIGHT + " current type: "+type);
        if (!SensorMinMaxMap.map.containsKey(type)){
            return;
        }
        if (type == Sensor.TYPE_MAGNETIC_FIELD){
            for (int i = 0; i < 3; i++){
                value[i] *= 0.5;
            }
            return;
        }
        float oldValue = value[0];
        float min = (float) SensorMinMaxMap.map.get(type).get(0);
        float max = (float) SensorMinMaxMap.map.get(type).get(1);
        XposedBridge.log("min: "+min + " max: "+ max + " current: "+oldValue);
        float newValue = -1;
        switch (TYPE){
            case Accurate:
                newValue = oldValue;
                break;
            case NotAccurate:
                float interval = (max - min) / (float)5;
                float left = min;
                float right = min + interval;
                while (right <= max){
                    if (oldValue > left && oldValue <= right){
                        newValue=right;
                        break;
                    }
                    else{
                        right += interval;
                        left += interval;
                    }
                }

                break;
        }
        value[0] = newValue;
    }


}
