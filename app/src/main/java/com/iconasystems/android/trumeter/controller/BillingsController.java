package com.iconasystems.android.trumeter.controller;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.SubscriberPriority;

import javax.inject.Inject;

import dagger.Lazy;
import de.greenrobot.event.EventBus;

/**
 * Created by christoandrew on 11/28/16.
 */

public class BillingsController {

    @Inject
    JobManager mJobManager;
    @Inject
    Config config;
    @Inject
    EventBus mEventBus;
    @Inject
    Context mAppContext;
    @Inject
    Lazy<NotificationManagerCompat> mNotificationManagerCompat;

    public BillingsController(AppComponent appComponent) {
        appComponent.inject(this);
        mEventBus.register(this, SubscriberPriority.LOW);
    }

    /*public void sendPostAsync(int meterId, float currentReading, String isMeteredEntry,
                              MeterReading newReading, float previousReading, String expected_range,
                              boolean isRegularReading, int billingPeriodId, String meterPhoto,
                              float readingLongitude, float lastReadingQuantity, float quantity,
                              float distance, int meterReaderId, float readingLatitude,
                              int readingCode, String reason) {
        mJobManager.addJobInBackground(new PostMeterReadingJob(meterId,currentReading,isMeteredEntry,
                previousReading,expected_range,isRegularReading,billingPeriodId,meterPhoto,readingLongitude,
                lastReadingQuantity,quantity,distance,meterReaderId,readingLatitude,readingCode,reason));
    }*/

    public void saveReadingAsync(int meterId, float currentReading, String isMeteredEntry,
                                 float previousReading, String expected_range,
                                 boolean isRegularReading, int billingPeriodId, String meterPhoto,
                                 float readingLongitude, float lastReadingQuantity, float quantity,
                                 float distance, int meterReaderId, float readingLatitude,
                                 int readingCode, String reason) {
        Log.d("Start Post", "Starting post");
        /*jobManager.addJobInBackground(new PostMeterReadingJob(meterId,currentReading,isMeteredEntry,
        previousReading,expected_range,isRegularReading,billingPeriodId,meterPhoto,readingLongitude,
                lastReadingQuantity,quantity,distance,meterReaderId,readingLatitude,readingCode,reason));*/
    }

    public void saveAsync(){
        Log.d("Start Post", "Starting post");
    }
}
