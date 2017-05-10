package com.iconasystems.android.trumeter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by christoandrew on 10/10/16.
 */
public class Config {
    public static final String KEY_ID = "id";
    public static final String KEY_BILLING_PERIOD_ID = "id";
    public static final String KEY_BILLING_PERIOD_START = "start_date";
    public static final String KEY_BILLING_PERIOD_END = "end_date";
    public static final String CONTENT_AUTHORITY = "com.iconasystems.android.sync.provider";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NEW_JOB_RETRY_COUNT = "new_reading_retry_count";
    private static final String KEY_API_URL = "api_url";

    private SharedPreferences mSharedPreferences;

    public Config(){

    }
    public Config(Context context) {
        mSharedPreferences = context.getSharedPreferences("cfg", Context.MODE_PRIVATE);
    }

    public long getUserId() {
        return mSharedPreferences.getLong(KEY_USER_ID, 1);
    }

    public void setUserId(long userId) {
        mSharedPreferences.edit().putLong(KEY_USER_ID, userId).apply();
    }

    public String getApiUrl() {
        return mSharedPreferences.getString(KEY_API_URL, "http://10.0.2.2:3000");
    }

    public void setApiUrl(String url) {
        mSharedPreferences.edit().putString(KEY_API_URL, url).apply();
    }

    public int getNewReadingRetryCount() {
        return mSharedPreferences.getInt(KEY_NEW_JOB_RETRY_COUNT, 20);
    }

    public void setNewReadingRetryCount(int count) {
        mSharedPreferences.edit().putInt(KEY_NEW_JOB_RETRY_COUNT, count).apply();
    }
}
