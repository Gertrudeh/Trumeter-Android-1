package com.iconasystems.android.trumeter.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.MeterReading;
import com.iconasystems.android.trumeter.vo.MeterReading$Table;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import static com.iconasystems.android.trumeter.AppPreferences.PREFS_NAME;

/**
 * Created by christoandrew on 11/28/16.
 */

public class MeterReadingModel extends BaseModel {
    private static final String KEY_LAST_READING_TIMESTAMP = "timestamp";
    private static final String KEY_LOCAL_READING_ID = "local_reading_id";
    private static final String PREF_NAME = "reading_pref";
    private SharedPreferences mPrefs;
    private AppPreferences preferences;
    SharedPreferences.Editor editor;

    @Inject
    Context mAppContext;

    public MeterReadingModel(App app, SQLiteDatabase database) {
        super(app, database);
        app.getAppComponent().inject(this);
    }

    public synchronized void save(MeterReading reading) {
        saveValid(reading);
    }

    public synchronized void saveAll(final List<MeterReading> readings) {
        if (readings.isEmpty()) {
            return;
        }
        TransactionManager.transact(mSQLiteDatabase, new Runnable() {
            @Override
            public void run() {
                for (MeterReading reading : readings) {
                    saveValid(reading);
                }
            }
        });
    }
    public long getLatestTimestamp(@Nullable Long meterId) {
        return getPref().getLong(createReadingTimestampKey(meterId), 0);
    }

    public synchronized long generateIdForNewLocalReading() {
        long id = getPref().getLong(KEY_LOCAL_READING_ID, Long.MIN_VALUE);
        getPref().edit().putLong(KEY_LOCAL_READING_ID, id + 1).apply();
        return id;
    }

    public synchronized MeterReading loadByBillingPeriodAndMeterId(int billingPeriodId, int meterId) {
        Condition conditions[] = new Condition[]
                {Condition.column(MeterReading$Table.METER_ID).eq(meterId),
                        Condition.column(MeterReading$Table.BILLING_PERIOD_ID)
                                .eq(billingPeriodId)};
        return new Select().from(MeterReading.class)
                .where((SQLCondition[]) conditions)
                .querySingle();
    }

    public void saveTaskTimestamp(String timestamp, Long readerId) {
        getPref().edit().putString(createReadingTimestampKey(readerId), timestamp).apply();
    }

    public MeterReading loadByClientIdAndMeterId(String clientId, int meterId){
        return new Select().from(MeterReading.class)
                .where(Condition.column(MeterReading$Table.CLIENT_ID).eq(clientId),
                        Condition.column(MeterReading$Table.METER_ID).eq(meterId))
                .querySingle();
    }
    private static String createReadingTimestampKey(Long readingId) {
        return KEY_LAST_READING_TIMESTAMP + "_" + readingId;
    }

    private void saveValid(MeterReading reading) {
        MeterReading existing = loadReadingById((int) reading.getId());
        if (existing == null) {
            reading.save();
        } else {
            reading.setId(existing.getId());
            reading.update();
        }
    }

    @Nullable
    public synchronized MeterReading loadReadingById(int readingId) {
        if (StringUtils.isEmpty(String.valueOf(readingId))) {
            return null;
        }
        return new Select().from(MeterReading.class)
                .where(Condition.column(MeterReading$Table.ID).eq(readingId))
                .querySingle();
    }

    public synchronized MeterReading loadLastReading(Customer customer) {
        return new Select().from(MeterReading.class)
                .where(Condition.column(MeterReading$Table.METER_ID).eq(customer.getMeter_id()))
                .orderBy("created_at DESC")
                .limit(1)
                .querySingle();
    }

    public synchronized MeterReading lastReading(int meterId) {
        return new Select().from(MeterReading.class)
                .where(Condition.column(MeterReading$Table.METER_ID).eq(meterId))
                .orderBy("created_at DESC")
                .limit(1)
                .querySingle();
    }

    private SharedPreferences getPref() {
        return mAppContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        getPref().edit().clear().apply();
    }

    public void delete(MeterReading reading) {
        reading.delete();
    }
}
