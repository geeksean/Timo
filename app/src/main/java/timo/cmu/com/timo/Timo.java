package timo.cmu.com.timo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findClass;

import de.robv.android.xposed.XposedHelpers;
import timo.cmu.com.timo.managers.FrequencyManager;
import timo.cmu.com.timo.managers.GranularityManager;
import timo.cmu.com.timo.model.GranularityType;
import timo.cmu.com.timo.model.SensorAccessSetting;

import android.hardware.Sensor;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom<Qinyu Tong> on 11/7/15.
 */
public class Timo implements IXposedHookLoadPackage {
    //private HashMap<Integer, HashSet<String>> blacklist = new HashMap<>();
    private Map<String, SensorAccessSetting[]> appSensorSettings = new HashMap<>();
    private Map<String, FrequencyManager[]> frequencyManagers = new HashMap<>();

    public Timo() {

    }


    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // Alright, so we start by creating a reference to the class that handles sensors.
        final Class<?> systemSensorManager = findClass(
                "android.hardware.SystemSensorManager", lpparam.classLoader);


        XposedHelpers.findAndHookMethod(
                "android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader, "dispatchSensorEvent", int.class, float[].class, int.class, long.class, new XC_MethodHook() {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {

                        // This pulls the 'Handle to Sensor' array straight from the SystemSensorManager class, so it should always pull the appropriate sensor.
                        SparseArray<Sensor> sensors = (SparseArray<Sensor>) XposedHelpers.getStaticObjectField(systemSensorManager, "sHandleToSensor");
                        // params.args[] is an array that holds the arguments that dispatchSensorEvent received, which are a handle pointing to a sensor
                        // in sHandleToSensor and a float[] of values that should be applied to that sensor.
                        int handle = (Integer) (param.args[0]); // This tells us which sensor was currently called.
                        Sensor s = sensors.get(handle);
                        // This could be expanded to disable ANY sensor.



                        XSharedPreferences xsp = new XSharedPreferences(this.getClass().getPackage().getName(), lpparam.packageName + "_prefs");
                        String pref = xsp.getString("i", null);
//                        XposedBridge.log("Preference: " + pref);

                        if (frequencyManagers.containsKey(lpparam.packageName)) {
                            XposedBridge.log(lpparam.packageName + " requests to access sensor type: " + s.getType());
                            FrequencyManager fm = frequencyManagers.get(lpparam.packageName)[s.getType()];
                            if (fm != null ) {
                                if (fm.allowAccess()) {
                                    XposedBridge.log("[FrequencyManager]: "+ lpparam.packageName +" access allowed");
                                    SensorAccessSetting sas = appSensorSettings.get(lpparam.packageName)[s.getType()];
                                    float[] values = (float[]) param.args[1];
                                    XposedBridge.log("original value:" + values[0]);
                                    if (sas.isAccurate) {
                                        GranularityManager.getGranularityValue(GranularityType.Accurate, values, s.getType());
                                    }
                                    else {
                                        GranularityManager.getGranularityValue(GranularityType.NotAccurate, values, s.getType());
                                    }
                                    XposedBridge.log("blurred value:" + values[0]);

                                    param.args[1] = values;
                                }
                                else {
                                    XposedBridge.log("[FrequencyManager]: "+ lpparam.packageName +" access denied");
                                    float[] values = (float[]) param.args[1];
                                    values[0] = -s.getMaximumRange()-1; //set value to a invalid val
                                    param.args[1] = values;
                                }

                            }
                            else{ //fm == null
                                XposedBridge.log("[FrequencyManager]: not frequency limit, access allowed");
                                SensorAccessSetting sas = appSensorSettings.get(lpparam.packageName)[s.getType()];
                                if (sas != null) {
                                    float[] values = (float[]) param.args[1];
                                    XposedBridge.log("original value:" + values[0]);
                                    if (sas.isAccurate) {
                                        GranularityManager.getGranularityValue(GranularityType.Accurate, values, s.getType());
                                    }
                                    else {
                                        GranularityManager.getGranularityValue(GranularityType.NotAccurate, values, s.getType());
                                    }
                                    XposedBridge.log("blurred value:" + values[0]);
                                    param.args[1] = values;
                                }
                            }
                        }
                        else if (pref != null) { // no frequencyManager initialed, initial control settings
                            XposedBridge.log(lpparam.packageName + " requests to access sensor type: " + s.getType());

                            parsePreference(pref, lpparam.packageName);
                            FrequencyManager fm = frequencyManagers.get(lpparam.packageName)[s.getType()];
                            if (fm != null ) {
                                if (fm.allowAccess()) {
                                    XposedBridge.log("[FrequencyManager]: "+ lpparam.packageName +" access allowed");
                                    SensorAccessSetting sas = appSensorSettings.get(lpparam.packageName)[s.getType()];
                                    float[] values = (float[]) param.args[1];
                                    XposedBridge.log("original value:" + values[0]);
                                    if (sas.isAccurate) {
                                        GranularityManager.getGranularityValue(GranularityType.Accurate, values, s.getType());
                                    }
                                    else {
                                        GranularityManager.getGranularityValue(GranularityType.NotAccurate, values, s.getType());
                                    }
                                    XposedBridge.log("blurred value:" + values[0]);

                                    param.args[1] = values;
                                }
                                else {
                                    XposedBridge.log("[FrequencyManager]: "+ lpparam.packageName +" access denied");
                                    float[] values = (float[]) param.args[1];
                                    values[0] = -s.getMaximumRange()-1; //set value to a invalid val
                                    param.args[1] = values;
                                }

                            }
                            else{// fm == null
                                XposedBridge.log("[FrequencyManager]: not frequency limit, access allowed");
                                SensorAccessSetting sas = appSensorSettings.get(lpparam.packageName)[s.getType()];
                                if (sas != null) {
                                    float[] values = (float[]) param.args[1];
                                    XposedBridge.log("original value:" + values[0]);
                                    if (sas.isAccurate) {
                                        GranularityManager.getGranularityValue(GranularityType.Accurate, values, s.getType());
                                    }
                                    else {
                                        GranularityManager.getGranularityValue(GranularityType.NotAccurate, values, s.getType());
                                    }
                                    XposedBridge.log("blurred value:" + values[0]);
                                    param.args[1] = values;
                                }
                            }
                        }
                        //else: no rule specified, do nothing
                    }
                }
        );

    }

    public void parsePreference(String pref, String pkgName) {
        SensorAccessSetting[] sass = new SensorAccessSetting[SensorAccessSetting.SENSOR_NUMS];
        FrequencyManager[] fms = new FrequencyManager[SensorAccessSetting.SENSOR_NUMS];
        String[] setts = pref.split(";");
        for (int s_type = 0; s_type < SensorAccessSetting.SENSOR_NUMS; s_type++) {
            XposedBridge.log("[parsePreference]" + setts[s_type]);
            String[] vars = setts[s_type].split(",");
            if (vars[0].equals("null")) {
                sass[s_type] = null;
                fms[s_type] = null;
            }
            else {
                sass[s_type] = new SensorAccessSetting(vars[0],Boolean.valueOf(vars[1]), Boolean.valueOf(vars[2]), Integer.parseInt(vars[3]));
                if (sass[s_type].limitFreq) {
                    fms[s_type] = new FrequencyManager(sass[s_type].freqPerSec, 1000);
                }
                else {
                    fms[s_type] = null;
                }

            }
        }

        frequencyManagers.put(pkgName, fms);
        appSensorSettings.put(pkgName, sass);
    }
}
