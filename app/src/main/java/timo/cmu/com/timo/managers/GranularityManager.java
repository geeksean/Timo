package timo.cmu.com.timo.managers;

import timo.cmu.com.timo.model.GranularityType;

/**
 * Created by aihua on 11/21/15.
 */
public class GranularityManager {
    public static float getGranularityValue(GranularityType TYPE, float oldValue, float min, float max){

        float newValue = -1;
        switch (TYPE){
            case Accurate:
                newValue = oldValue;
                break;
            case NotAccurate:
                float interval = (max - min) / 10;
                float left = min;
                float right = interval;
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
        return newValue;
    }


}
