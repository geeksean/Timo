package timo.cmu.com.timo.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }


}
