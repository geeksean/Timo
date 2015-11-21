package timo.cmu.com.timo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.*;

import timo.cmu.com.timo.R;

/**
 * Created by STuotuo.Wen on 2015/11/21.
 */
public class MainFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Installed Apps");
        List<String> appList = new ArrayList<String>();

        PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            appList.add(packageInfo.packageName + "###"
                    + pm.getApplicationLabel(packageInfo).toString());
        }

        AppAdapter adapter = new AppAdapter(appList);
        setListAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((AppAdapter) getListAdapter()).notifyDataSetChanged();
    }

    private class AppAdapter extends ArrayAdapter<String> {
        public AppAdapter(List<String> appList) {
            super(getActivity(), android.R.layout.simple_list_item_1, appList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.app_name_item, null);
            }
            // configure the view for this Student
            String[] appInfo = getItem(position).trim().split("###");
            final String pkgName = appInfo[0];
            final String appName = appInfo[1];

            TextView appText = (TextView) convertView.findViewById(R.id.item_text);

            appText.setText(appName);
            appText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SensorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("appName", appName);
                    bundle.putString("pkgName", pkgName);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
