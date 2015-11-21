package timo.cmu.com.timo;

/**
 * Created by aihua on 11/21/15.
 */
public class TimoGranularity {
    
    public static enum GranularityType{
        Accurate,
        NotAccurate
    }
    public static float getGranularityValue(GranularityType TYPE, float oldValue, float min, float max){

        float newValue = 0;
        switch (TYPE){
            case Accurate:
                newValue = oldValue;
                break;
            case NotAccurate:
                float interval = (max - min) / 10;
                float left = min;
                float right = interval;
                while (right < max){
                    if (oldValue > left && oldValue < right){
                        return right;
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
