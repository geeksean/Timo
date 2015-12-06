package timo.cmu.com.timo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findClass;

import de.robv.android.xposed.XposedHelpers;
import timo.cmu.com.timo.model.SensorData;

import android.hardware.Sensor;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Tom on 11/7/15.
 */
public class Timo implements IXposedHookLoadPackage {
    private HashMap<Integer, HashSet<String>> blacklist = new HashMap<>();
    //public static Map<String, SensorData[]> settings = new HashMap<String, SensorData[]>();
    private static HashMap<Integer, FrequencyManager> frequencyManagers = new HashMap<>();

    public Timo() {
        HashSet<String> b1 = new HashSet<>();
        b1.add("imoblife.androidsensorbox");
        blacklist.put(Sensor.TYPE_PROXIMITY, b1);

        HashSet<String> b2 = new HashSet<>();
        b2.add("com.wered.sensorsmultitool");
        blacklist.put(Sensor.TYPE_PRESSURE, b2);
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
                        if (s.getType() == Sensor.TYPE_PROXIMITY || s.getType() == Sensor.TYPE_PRESSURE) {
                            XposedBridge.log(lpparam.packageName + " requests to access sensor type: " + s.getType());
                            if (blacklist.get(s.getType()).contains(lpparam.packageName)) {
                                XposedBridge.log("Block app: " + lpparam.packageName + " from accessing " + s.getType());
                                //XposedBridge.log("Setting: sensor data name" + settings.get(lpparam.packageName)[Sensor.TYPE_PROXIMITY].name);
                                XSharedPreferences xsp = new XSharedPreferences(this.getClass().getPackage().getName(), lpparam.packageName+"_prefs");
                                String pref = xsp.getString("i", "No Pref Error.");
                                XposedBridge.log("Qtong: " +  pref);
//                                float[] values = (float[]) param.args[1];
//                                values[0] = 0;
//                                param.args[1] = values;
                            }
                        }
                    }
                }
        );

    }

    public static void addFrequencyManager(int sensor, FrequencyManager fm) {
        XposedBridge.log("Add Sensor : " + sensor + "'s FreqManager");
        frequencyManagers.put(sensor, fm);
    }
}
