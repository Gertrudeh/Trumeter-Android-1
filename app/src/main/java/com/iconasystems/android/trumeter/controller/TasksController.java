package com.iconasystems.android.trumeter.controller;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.birbit.android.jobqueue.JobManager;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.SubscriberPriority;
import com.iconasystems.android.trumeter.event.task.FetchedTaskEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.task.FetchTasksJob;

import javax.inject.Inject;

import dagger.Lazy;
import de.greenrobot.event.EventBus;

/**
 * Created by christoandrew on 11/28/16.
 */

public class TasksController {
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

    public TasksController(AppComponent appComponent) {
        appComponent.inject(this);
        eventBus.register(this, SubscriberPriority.LOW);
    }

    public void fetchTasksAsync(boolean fromUI,int meterReaderId) {
        jobManager.addJobInBackground(
                new FetchTasksJob(fromUI ? BaseJob.UI_HIGH : BaseJob.BACKGROUND, meterReaderId));
    }

    public void onEventMainThread(FetchedTaskEvent event) {

    }
}
