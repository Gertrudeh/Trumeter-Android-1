package com.iconasystems.android.trumeter;

import android.content.Context;
import android.content.SharedPreferences;

import com.iconasystems.android.trumeter.vo.BillingPeriod;
import com.iconasystems.android.trumeter.vo.MeterReader;

/**
 * Created by MUSHABE on 8/3/2016.
 */
public class AppPreferences {
    public static final String PREFS_NAME = "AppPreferences";
    private static final String KEY_LAST_READING_TIMESTAMP = "timestamp";
    private static final String KEY_LOCAL_READING_ID = "local_reading_id";
    int PRIVATE_MODE = 0;
    private SharedPreferences.Editor editor;
    private SharedPreferences appPreferences;
    private Context context;
    private boolean isLoggedIn = false;
    private int id;
    private int billingPeriodId;
    private String billingPeriodStartDate;
    private String billingPeriodEndDate;
    private boolean hasFinishedWalkthrough = false;


    public static volatile AppPreferences singleton = null;

    private AppPreferences(Context context) {
        this.context = context;
        appPreferences = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = appPreferences.edit();

        //int token = appPreferences.getInt(Config.KEY_ID, 0);
        //isLoggedIn = token != 0;

        if (isLoggedIn) {
            id = appPreferences.getInt(Config.KEY_ID, 0);

        }
    }

    public static AppPreferences get(Context context) {
        if (singleton == null) {
            synchronized (AppPreferences.class) {
                singleton = new AppPreferences(context);
            }
        }
        return singleton;
    }

    public synchronized int generateIdForNewLocalReading() {
        int id = appPreferences.getInt(KEY_LOCAL_READING_ID, Integer.MIN_VALUE);
        editor.putInt(KEY_LOCAL_READING_ID, id + 1).apply();
        return id;
    }

    public boolean isLoggedIn(MeterReader reader) {
        if (reader!= null){
            id = reader.getId();
            editor.putInt(Config.KEY_ID, id);

        }else{
            return false;
        }
        return false;
    }

    public void logOut() {
        isLoggedIn = false;
        id = 0x0;
        editor.putInt(Config.KEY_ID, id);
        editor.apply();
    }

    public void setLoggedIn(MeterReader reader) {
        if (reader != null) {
            id = reader.getId();
            editor.putInt(Config.KEY_ID, id);
            editor.apply();
        }
    }


    public void setBillingPeriod(BillingPeriod billingPeriod){
        if (billingPeriod != null) {
            billingPeriodId = billingPeriod.getId();
            billingPeriodStartDate = billingPeriod.getStart_date();
            billingPeriodEndDate = billingPeriod.getEnd_date();
            editor.putInt(Config.KEY_BILLING_PERIOD_ID, billingPeriodId);
            editor.putString(Config.KEY_BILLING_PERIOD_START,billingPeriodStartDate);
            editor.putString(Config.KEY_BILLING_PERIOD_END,billingPeriodEndDate);
            editor.apply();
        }
    }

    public BillingPeriod getBillingPeriod(){
        BillingPeriod billingPeriod = new BillingPeriod();
        billingPeriod.setId(billingPeriodId);
        billingPeriod.setStart_date(billingPeriodStartDate);
        billingPeriod.setEnd_date(billingPeriodEndDate);
        return billingPeriod;
    }

    public MeterReader getUser() {
        MeterReader reader = new MeterReader();
        reader.setId(id);
        return reader;
    }


    public void clear() {
        editor.clear();
    }

    public int getId() {
        return id;
    }

    public interface LoginStatusListener {
        void onLogin();
        void onLogout();
    }
}
