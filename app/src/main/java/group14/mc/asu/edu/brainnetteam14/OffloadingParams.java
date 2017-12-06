package group14.mc.asu.edu.brainnetteam14;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by krish on 11/27/2017.
 * Class to calculate and store current battery capacity and network latency
 */

public class OffloadingParams {

    private long startBattteryLevel, endBatteryLevel, totalBatteryConsumed;
    private long startExecutionTime, endExecutionTime, totalExecutionTime;
    private static float  batteryPCT;
    private double netWorkDelay;
    private Context context;
    private String ServerType;
    private final String TAG = getClass().getSimpleName();
    IntentFilter ifilter ;
    Intent batteryStatus;

    OffloadingParams(Context context, String serverType){
        this.context = context;
        this.ServerType = serverType;
         ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
         batteryStatus = context.registerReceiver(null, ifilter);

    }

    public void setStartBattteryLevel(){
        this.startBattteryLevel = getCurrentBatteryCapacity();

    }

    public void setEndBatteryLevel(){
        this.endBatteryLevel = getCurrentBatteryCapacity();
    }

    public long getStartBattteryLevel(){
        return startBattteryLevel;
    }

    public long getEndBatteryLevel(){
        return endBatteryLevel;
    }

    public void setTotalBatteryConsumed(){
        this.totalBatteryConsumed = startBattteryLevel - endBatteryLevel;
    }

    public long getTotalBatteryConsumed(){
        return totalBatteryConsumed;
    }

    public long getStartExecutionTime() {
        return this.startExecutionTime;
    }

    public void setStartExecutionTime(long startExecutionTime) {
        this.startExecutionTime = startExecutionTime;
    }

    public long getEndExecutionTime() {
        return this.endExecutionTime;
    }

    public void setEndExecutionTime() {
        this.endExecutionTime = System.currentTimeMillis();
    }

    public void setEndExecutionTime(long exexTime) {
        this.endExecutionTime = exexTime;
    }

    public void setTotalExecutionTime(){
        this.totalExecutionTime = this.endExecutionTime - this.startExecutionTime;
    }

    public void setTotalExecutionTime(long d){
        this.totalExecutionTime = d;
    }

    public long getTotalExecutionTime(){
        return this.totalExecutionTime;
    }


    public void setnetWorkDelay(String response){

        String[] params = response.split("/");
        try {
            Log.d(TAG, response);
            Log.d(TAG, "AVG:" + params[4]);
            netWorkDelay = Double.valueOf(params[4]);
            Log.d(TAG, "Network delay of"+ServerType+": "+netWorkDelay);

        }
        catch (ArrayIndexOutOfBoundsException ex){
            netWorkDelay = Double.MAX_VALUE;
            Log.e(TAG, "Network delay error:"+ex.toString());
        }
        catch (NumberFormatException ex){
            netWorkDelay = Double.MAX_VALUE;
            Log.e(TAG, "Network delay error:"+ex.toString());
        }

    }

    public void setPingTime(String pingTime){

        this.netWorkDelay = Double.valueOf(pingTime);
    }

    public double getnetWorkDelay(){

        return this.netWorkDelay;

    }

    public long getCurrentBatteryCapacity(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            Integer chargeCounter = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Integer capacity = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if(chargeCounter == Integer.MIN_VALUE || capacity == Integer.MIN_VALUE)
                return 0;
            return (chargeCounter/capacity) *100;
        }
        return 0;
    }

    public void setBatteryPCT(){
        batteryPCT = getBatteryLevel();

    }

    public float getBatteryCPT(){
        return batteryPCT;
    }

    public float getBatteryLevel(){


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        return batteryPct;

    }


    //executeCmd("ping -c 1 -w 1 google.com", false);
    //executeCmd("ping -c 1 -w 1 google.com", false);

    public static String executeCmd(String cmd, boolean sudo){
        try {

            Process p;
            if(!sudo)
                p= Runtime.getRuntime().exec(cmd);
            else{
                p= Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            p.destroy();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public  void logNetworkandBatteryCPT(){
        Log.d(TAG, ServerType+" has "+netWorkDelay+" ms round trip time and the level of smartphone battery is at:"+(batteryPCT*100)+"%");
    }


    /* Get the objects having the */
    public static String takeOffloadingDecision(OffloadingParams cloudserver, OffloadingParams fogserver){

        long fogexecTime, cloudexecTime, fogtotalTime, cloudtotalTime;
        double fogroundTrip, cloudroundTrip;

        String[] servers = {"Fog", "Cloud", "No Connection"};
        int decision;

        fogexecTime = fogserver.getTotalExecutionTime();
        cloudexecTime = cloudserver.getTotalExecutionTime();
        fogroundTrip = fogserver.getnetWorkDelay();
        cloudroundTrip = cloudserver.getnetWorkDelay();

        float currentBatteryCPT = cloudserver.getBatteryCPT() * 100;

        if(fogroundTrip == Double.MAX_VALUE && cloudroundTrip == Double.MAX_VALUE){
            decision = 2;
            Log.d("OffloadingParams", "Both servers not reachable");
            return servers[2];

        }

        else if(fogroundTrip == Double.MAX_VALUE){
            decision = 1;
            Log.d("OffloadingParams", "Fog server not reachable");
            return servers[decision];
        }
        else if(cloudroundTrip == Double.MAX_VALUE){
            decision = 0;
            Log.d("OffloadingParams", "Cloud server not reachable");
            return servers[decision];
        }

        if(currentBatteryCPT <= 15){
            decision = 0;
            return servers[decision];
        }
        else{
            if(fogexecTime == Double.MAX_VALUE){
                decision = 1;
                return servers[decision];
            }
            if(cloudexecTime == Double.MAX_VALUE){
                decision = 0;
                return servers[decision];

            }
            fogtotalTime = fogexecTime + (long) fogroundTrip;
            cloudtotalTime = cloudexecTime + (long) cloudexecTime;
            decision = (fogtotalTime < cloudtotalTime) ? 0 : 1;
            return servers[decision];
        }

    }

    }


