

package com.iconasystems.android.trumeter.di.component;


import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.birbit.android.jobqueue.JobManager;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.controller.BillingPeriodsController;
import com.iconasystems.android.trumeter.controller.BillingsController;
import com.iconasystems.android.trumeter.controller.CustomersController;
import com.iconasystems.android.trumeter.controller.MetersController;
import com.iconasystems.android.trumeter.controller.ReadingsController;
import com.iconasystems.android.trumeter.controller.TasksController;
import com.iconasystems.android.trumeter.di.module.ApplicationModule;
import com.iconasystems.android.trumeter.job.billing_periods.FetchBillingPeriodsJob;
import com.iconasystems.android.trumeter.job.customer.FetchCustomersJob;
import com.iconasystems.android.trumeter.job.meter.FetchMetersJob;
import com.iconasystems.android.trumeter.job.reading.FetchMeterReadingJob;
import com.iconasystems.android.trumeter.job.reading.PostMeterReadingJob;
import com.iconasystems.android.trumeter.job.task.FetchTasksJob;
import com.iconasystems.android.trumeter.model.BillingPeriodModel;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.model.MeterModel;
import com.iconasystems.android.trumeter.model.MeterReadingModel;
import com.iconasystems.android.trumeter.model.TaskModel;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface AppComponent {

    JobManager jobManager();

    EventBus eventBus();

    ApiService apiService();

    Context appContext();

    Config config();

    MeterReadingModel meterReadingModel();

    TaskModel taskModel();

    CustomerModel customerModel();

    MeterModel meterModel();

    BillingPeriodModel billingPeriodModel();

    BillingPeriodsController billingPeriodsController();

    CustomersController customersController();

    MetersController metersController();

    TasksController tasksController();

    BillingsController billingsController();

    ReadingsController readingsController();

    NotificationManagerCompat notificationManagerCompat();

    void inject(TasksController tasksController);

    void inject(CustomersController customersController);

    void inject(BillingsController billingsController);

    void inject(ReadingsController readingsController);

    void inject(MetersController metersController);

    void inject(BillingPeriodsController billingPeriodsController);

    void inject(FetchTasksJob fetchTasksJob);

    void inject(FetchCustomersJob fetchCustomersJob);

    void inject(PostMeterReadingJob postMeterReadingJob);

    void inject(FetchMetersJob fetchMeterJob);

    void inject(FetchMeterReadingJob fetchMeterReadingJob);

    void inject(FetchBillingPeriodsJob fetchBillingPeriodsJob);

    void inject(TaskModel taskModel);

    void inject(BillingPeriodModel billingPeriodModel);

    void inject(MeterReadingModel meterReadingModel);


}
