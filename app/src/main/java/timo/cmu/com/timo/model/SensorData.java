package timo.cmu.com.timo.model;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class SensorData {
    public String name;
    public boolean isAccurate;
    public boolean limitFreq;
    public int freqPerMin;

    public SensorData(String name, boolean isAccurate, boolean limitFreq, int freqPerMin) {
        this.name = name;
        this.isAccurate = isAccurate;
        this.limitFreq = limitFreq;
        this.freqPerMin = freqPerMin;
    }

    @Override
    public String toString() {
        return name + "," + isAccurate + "," + limitFreq + "," + freqPerMin;
    }
}
