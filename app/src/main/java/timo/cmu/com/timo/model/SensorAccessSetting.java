package timo.cmu.com.timo.model;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class SensorAccessSetting {
    public static int SENSOR_NUMS = 13;
    public String name;
    public boolean isAccurate;
    public boolean limitFreq;
    public int freqPerSec;

    public SensorAccessSetting(String name, boolean isAccurate, boolean limitFreq, int freqPerSec) {
        this.name = name;
        this.isAccurate = isAccurate;
        this.limitFreq = limitFreq;
        this.freqPerSec = freqPerSec;
    }

    @Override
    public String toString() {
        return name + "," + isAccurate + "," + limitFreq + "," + freqPerSec;
    }
}
