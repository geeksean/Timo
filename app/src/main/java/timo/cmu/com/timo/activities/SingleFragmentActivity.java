package timo.cmu.com.timo.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import timo.cmu.com.timo.R;

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment sFragment = manager.findFragmentById(R.id.fragmentContainer);

        if (sFragment == null) {
            sFragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, sFragment)
                    .commit();
        }
    }
}
