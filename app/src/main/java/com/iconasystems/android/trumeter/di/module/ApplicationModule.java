

package com.iconasystems.android.trumeter.di.module;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationManagerCompat;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.iconasystems.android.trumeter.controller.BillingPeriodsController;
import com.iconasystems.android.trumeter.controller.BillingsController;
import com.iconasystems.android.trumeter.controller.CustomersController;
import com.iconasystems.android.trumeter.controller.MetersController;
import com.iconasystems.android.trumeter.controller.ReadingsController;
import com.iconasystems.android.trumeter.controller.TasksController;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.model.BillingPeriodModel;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.model.MeterModel;
import com.iconasystems.android.trumeter.model.MeterReadingModel;
import com.iconasystems.android.trumeter.model.TaskModel;
import com.iconasystems.android.trumeter.util.L;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module
public class ApplicationModule {
    private final App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }


    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public SQLiteDatabase database() {
        return FlowManager.getDatabase(TrumeterDatabase.NAME).getWritableDatabase();
    }

    @Provides
    @Singleton
    public TaskModel taskModel(SQLiteDatabase database) {
        return new TaskModel(mApp, database);
    }

    @Provides
    @Singleton
    public MeterReadingModel meterReadingModel(SQLiteDatabase database) {
        return new MeterReadingModel(mApp, database);
    }

    @Provides
    @Singleton
    public CustomerModel customerModel(SQLiteDatabase database) {
        return new CustomerModel(mApp, database);
    }

    @Provides
    @Singleton
    public MeterModel meterModel(SQLiteDatabase database) {
        return new MeterModel(mApp, database);
    }

    @Provides
    @Singleton
    public BillingPeriodModel billingPeriodModel(SQLiteDatabase database) {
        return new BillingPeriodModel(mApp, database);
    }

    @Provides
    @Singleton
    public TasksController tasksController() {
        return new TasksController(
                mApp.getAppComponent());
    }

    @Provides
    @Singleton
    public BillingPeriodsController billingPeriodsController() {
        return new BillingPeriodsController(
                mApp.getAppComponent());
    }

    @Provides
    @Singleton
    public CustomersController customersController() {
        return new CustomersController(mApp.getAppComponent());
    }

    @Provides
    @Singleton
    public BillingsController billingsController() {
        return new BillingsController(mApp.getAppComponent());
    }

    @Provides
    @Singleton
    public ReadingsController readingsController() {
        return new ReadingsController(mApp.getAppComponent());
    }

    @Provides
    @Singleton
    public MetersController metersController() {
        return new MetersController(mApp.getAppComponent());
    }


    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public Config config() {
        return new Config(mApp);
    }

    @Provides
    @Singleton
    public JobManager jobManager() {
        Configuration config = new Configuration.Builder(mApp)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .customLogger(L.getJobLogger())
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApp.getAppComponent());
                        }
                    }
                })
                .build();
        return new JobManager(config);
    }

    @Provides
    @Singleton
    public NotificationManagerCompat notificationCompat() {
        return NotificationManagerCompat.from(mApp);
    }
}
