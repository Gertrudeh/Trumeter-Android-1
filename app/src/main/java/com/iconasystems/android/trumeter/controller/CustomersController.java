package com.iconasystems.android.trumeter.controller;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.birbit.android.jobqueue.JobManager;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.SubscriberPriority;
import com.iconasystems.android.trumeter.event.customer.FetchedCustomersEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.customer.FetchCustomersJob;

import javax.inject.Inject;

import dagger.Lazy;
import de.greenrobot.event.EventBus;

/**
 * Created by christoandrew on 11/28/16.
 */

public class CustomersController {

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

    public CustomersController(AppComponent appComponent) {
        appComponent.inject(this);
        eventBus.register(this, SubscriberPriority.LOW);
    }

    public void fetchCustomersAsync(boolean fromUI,int routeId) {
        jobManager.addJobInBackground(
                new FetchCustomersJob(fromUI ? BaseJob.UI_HIGH : BaseJob.BACKGROUND, routeId));
    }

    public void onEventMainThread(FetchedCustomersEvent event) {

    }

}
