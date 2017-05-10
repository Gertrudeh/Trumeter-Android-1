package com.iconasystems.android.trumeter.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.SubscriberPriority;
import com.iconasystems.android.trumeter.event.reading.DeleteReadingEvent;
import com.iconasystems.android.trumeter.event.reading.FetchedReadingsEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.reading.FetchMeterReadingJob;
import com.iconasystems.android.trumeter.job.reading.PostMeterReadingJob;
import com.iconasystems.android.trumeter.view.activity.BillingActivity;

import javax.inject.Inject;

import dagger.Lazy;
import de.greenrobot.event.EventBus;

/**
 * Created by christoandrew on 12/12/16.
 */

public class ReadingsController {
    @Inject
    JobManager jobManager;
    @Inject
    EventBus eventBus;
    @Inject
    Context context;
    @Inject
    Lazy<NotificationManagerCompat> notificationManagerCompat;
    @Inject
    Config config;

    public ReadingsController(AppComponent appComponent) {
        appComponent.inject(this);
        eventBus.register(this, SubscriberPriority.LOW);
    }

    public void fetchReadingsAsync(boolean fromUI) {
        jobManager.addJobInBackground(
                new FetchMeterReadingJob(fromUI ? BaseJob.UI_HIGH : BaseJob.BACKGROUND));
    }


    public void saveReadingAsync(int meterId, float currentReading, String isMeteredEntry,
                               float previousReading, String expected_range,
                               boolean isRegularReading, int billingPeriodId, String meterPhoto,
                               float readingLongitude, float lastReadingQuantity, float quantity,
                               float distance, int meterReaderId, float readingLatitude,
                               int readingCode, String reason, String clientId, float lastQuantity) {
        Log.d("Start Post", "Starting post");
        jobManager.addJobInBackground(new PostMeterReadingJob(meterId,currentReading,isMeteredEntry,
        previousReading,expected_range,isRegularReading,billingPeriodId,meterPhoto,readingLongitude,
                lastReadingQuantity,quantity,distance,meterReaderId,readingLatitude,readingCode,reason, clientId, lastQuantity));
    }
    public void onEventMainThread(FetchedReadingsEvent event) {

    }
    public void onEventMainThread(DeleteReadingEvent event) {
        if (event.didNotifyUser() || !event.isSyncFailure()) {
            return;
        }
        Intent intent = BillingActivity.intentForSendPost(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_action_backup)
                .setContentTitle(context.getString(R.string.cannot_sync_post))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManagerCompat.get().notify(1, builder.build());
    }
    public void saveAsync(){
        Log.d("Start Post", "Starting post");
    }

}
