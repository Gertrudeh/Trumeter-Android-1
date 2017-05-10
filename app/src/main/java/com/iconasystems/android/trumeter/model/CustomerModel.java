package com.iconasystems.android.trumeter.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.Customer$Table;
import com.iconasystems.android.trumeter.vo.MeterReading;
import com.iconasystems.android.trumeter.vo.MeterReading$Table;
import com.iconasystems.android.trumeter.vo.Task;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by christoandrew on 11/28/16.
 */

public class CustomerModel extends BaseModel {
    private static final String KEY_LOCAL_CUSTOMER_ID = "_local_customer_id";
    private static final String KEY_LAST_CUSTOMER_TIMESTAMP = "timestamp";
    private SharedPreferences mSharedPreferences;

    @Inject
    Context context;
    public CustomerModel(App app, SQLiteDatabase database) {
        super(app, database);
    }

    public List<Customer> loadCustomers(int routeId) {
        return new Select().from(Customer.class).where(
                Condition.column(Customer$Table.ROUTE_ID).eq(routeId)
        ).queryList();
    }

    public Customer load(int customerId) {
        return new Select().from(Customer.class)
                .where(Condition.column(Customer$Table.ID).eq(customerId)).querySingle();
    }

    public synchronized void saveAll(final List<Customer> customers) {
        Log.d("Total Customers", customers.size()+"");
        if (customers.isEmpty()) {
            return;
        }
        TransactionManager.transact(mSQLiteDatabase, new Runnable() {
            @Override
            public void run() {
                for (Customer customer : customers) {
                    saveValid(customer);
                }
            }
        });
    }

    private void saveValid(Customer customer) {
        Customer existing = loadCustomerById(customer.getId());

        if (existing == null) {
            customer.save();
        } else {
            customer.setId(existing.getId());
            customer.update();
        }
    }

    @Nullable
    public synchronized Customer loadCustomerById(int customerId) {
        if (StringUtils.isEmpty(String.valueOf(customerId))) {
            return null;
        }
        return new Select().from(Customer.class)
                .where(Condition.column(Customer$Table.ID).eq(customerId))
                .querySingle();
    }

    public MeterReading lastReading(Customer customer){
        return new Select().from(MeterReading.class)
                .where(Condition.column(MeterReading$Table.METER_ID).eq(customer.getMeter_id()))
                .orderBy("created_at DESC")
                .limit(1)
                .querySingle();
    }



    public String getLatestTimestamp(int customerId) {
        return getPref().getString(createTimestampKey(customerId), null);
    }

    private SharedPreferences getPref() {
        return context.getSharedPreferences("customer_prefs", 0);
    }

    public synchronized long generateIdForNewLocal() {
        long id = getPref().getLong(KEY_LOCAL_CUSTOMER_ID, Long.MIN_VALUE);
        getPref().edit().putLong(KEY_LOCAL_CUSTOMER_ID, id + 1).apply();
        return id;
    }

    public void saveTimestamp(String timestamp, int customerId) {
        getPref().edit().putString(createTimestampKey(customerId), timestamp).apply();
    }

    private static String createTimestampKey(int customerId) {
        return KEY_LAST_CUSTOMER_TIMESTAMP + "_" + customerId;
    }

    public void clear() {
        getPref().edit().clear().apply();
    }

    public void delete(Task task) {
        task.delete();
    }

}
