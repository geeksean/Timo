package timo.cmu.com.timo;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Tom on 11/21/15.
 */
public class FrequencyManager {
    private Queue<Long> timestamps;
    private int freq;
    private long interval;

    public FrequencyManager(int freq, int interval) {
        this.freq = freq;
        this.interval = interval;
        this.timestamps = new LinkedList<>();
    }

    public boolean allowAccess() {
        Date date = new Date();
        Long curTs = date.getTime();
        if (timestamps.size() < freq) {
            timestamps.add(curTs);
            return true;
        }
        else {
            Long oldestTs = timestamps.peek();
            if (curTs - oldestTs >= interval) {
                timestamps.remove();
                timestamps.add(curTs);
                return true;
            }
        }

        return false;
    }
}
