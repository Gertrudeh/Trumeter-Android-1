package com.iconasystems.android.trumeter.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.Meter;
import com.iconasystems.android.trumeter.vo.Meter$Table;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import static com.iconasystems.android.trumeter.AppPreferences.PREFS_NAME;

/**
 * Created by christoandrew on 11/28/16.
 */

public class MeterModel extends BaseModel {
    private static final String KEY_LAST_READING_TIMESTAMP = "timestamp";
    private static final String KEY_LOCAL_READING_ID = "local_meter_id";
    private static final String PREF_NAME = "meter_pref";
    private SharedPreferences mPrefs;

    @Inject
    Context mAppContext;

    public MeterModel(App app, SQLiteDatabase database) {
        super(app, database);
    }

    public synchronized void save(Meter meter) {
        saveValid(meter);
    }

    public synchronized void saveAll(final List<Meter> meters) {
        if (meters.isEmpty()) {
            return;
        }
        TransactionManager.transact(mSQLiteDatabase, new Runnable() {
            @Override
            public void run() {
                for (Meter meter : meters) {
                    saveValid(meter);
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

    private void saveValid(Meter meter) {
        Meter existing = loadMeterById(meter.getId());
        if (existing == null) {
            meter.save();
        } else {
            meter.setId(existing.getId());
            meter.update();
        }
    }
    @Nullable
    public synchronized Meter loadMeterById(int meterId) {
        if (StringUtils.isEmpty(String.valueOf(meterId))) {
            return null;
        }
        return new Select().from(Meter.class)
                .where(Condition.column(Meter$Table.ID).eq(meterId))
                .querySingle();
    }

    public synchronized Meter loadCustomerMeter(Customer customer){
        if (customer == null) {
            return null;
        }
        return new Select().from(Meter.class)
                .where(Condition.column(Meter$Table.CUSTOMER_ID).eq(customer.getId()))
                .querySingle();
    }

    private SharedPreferences getPref() {
        return mAppContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public void clear() {
        getPref().edit().clear().apply();
    }

    public void delete(Meter meter) {
        meter.delete();
    }

}
