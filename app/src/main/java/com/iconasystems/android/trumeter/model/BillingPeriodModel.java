package com.iconasystems.android.trumeter.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.vo.BillingPeriod;
import com.iconasystems.android.trumeter.vo.BillingPeriod$Table;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import static com.iconasystems.android.trumeter.AppPreferences.PREFS_NAME;

/**
 * Created by christoandrew on 12/12/16.
 */

public class BillingPeriodModel extends BaseModel {
    private static final String KEY_LAST_READING_TIMESTAMP = "timestamp";
    private static final String KEY_LOCAL_READING_ID = "local_billing_id";
    private static final String PREF_NAME = "billing_pref";
    private SharedPreferences mPrefs;

    @Inject
    Context mAppContext;

    public BillingPeriodModel(App app, SQLiteDatabase database) {
        super(app, database);
    }

    public synchronized List<BillingPeriod> load(){
        return new Select().from(BillingPeriod.class).queryList();
    }
    public synchronized void save(BillingPeriod billingPeriod) {
        saveValid(billingPeriod);
    }

    public synchronized void saveAll(final List<BillingPeriod> billingPeriods) {
        if (billingPeriods.isEmpty()) {
            return;
        }
        TransactionManager.transact(mSQLiteDatabase, new Runnable() {
            @Override
            public void run() {
                for (BillingPeriod billingPeriod : billingPeriods) {
                    saveValid(billingPeriod);
                }
            }
        });
    }

    public String getLatestTimestamp(@Nullable int meterId) {
        return getPref().getString(createTimestampKey(meterId),null);
    }

    public synchronized long generateIdForNewLocalReading() {
        long id = getPref().getLong(KEY_LOCAL_READING_ID, Long.MIN_VALUE);
        getPref().edit().putLong(KEY_LOCAL_READING_ID, id + 1).apply();
        return id;
    }

    public void saveTimestamp(String timestamp, int meterId) {
        getPref().edit().putString(createTimestampKey(meterId), timestamp).apply();
    }

    private static String createTimestampKey(int meterId) {
        return KEY_LAST_READING_TIMESTAMP + "_" + meterId;
    }

    private void saveValid(BillingPeriod billingPeriod) {
        BillingPeriod existing = loadById(billingPeriod.getId());
        if (existing == null) {
            billingPeriod.save();
        } else {
            billingPeriod.setId(existing.getId());
            billingPeriod.update();
        }
    }
    @Nullable
    public synchronized BillingPeriod loadById(int Id) {
        if (StringUtils.isEmpty(String.valueOf(Id))) {
            return null;
        }
        return new Select().from(BillingPeriod.class)
                .where(Condition.column(BillingPeriod$Table.ID).eq(Id))
                .querySingle();
    }

    private SharedPreferences getPref() {
        return mAppContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public void clear() {
        getPref().edit().clear().apply();
    }

    public void delete(BillingPeriod billingPeriod) {
        billingPeriod.delete();
    }
}
