package com.iconasystems.android.trumeter.controller;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.birbit.android.jobqueue.JobManager;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.SubscriberPriority;
import com.iconasystems.android.trumeter.event.billing_period.FetchedBillingPeriodEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.billing_periods.FetchBillingPeriodsJob;

import javax.inject.Inject;

import dagger.Lazy;
import de.greenrobot.event.EventBus;

/**
 * Created by christoandrew on 12/12/16.
 */

public class BillingPeriodsController {
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

    public BillingPeriodsController(AppComponent appComponent) {
        appComponent.inject(this);
        eventBus.register(this, SubscriberPriority.LOW);
    }

    public void fetchBillingPeriodsAsync(boolean fromUI) {
        jobManager.addJobInBackground(
                new FetchBillingPeriodsJob(fromUI ? BaseJob.UI_HIGH : BaseJob.BACKGROUND));
    }

    public void onEventMainThread(FetchedBillingPeriodEvent event) {

    }
}
