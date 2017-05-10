package com.iconasystems.android.trumeter.job.billing_periods;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.billing_period.FetchedBillingPeriodEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.NetworkException;
import com.iconasystems.android.trumeter.model.BillingPeriodModel;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.vo.BillingPeriod;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by christoandrew on 12/12/16.
 */

public class FetchBillingPeriodsJob extends BaseJob {
    private static final String GROUP = "FetchBillingPeriodsJob";

    @Nullable
    private int customerId;

    private long timestamp = 0;

    @Inject
    transient EventBus eventBus;
    @Inject
    transient Config config;
    @Inject
    transient CustomerModel customerModel;
    @Inject
    transient BillingPeriodModel billingPeriodModel;
    @Inject
    ApiService apiService;


    public FetchBillingPeriodsJob(@Priority int priority, @NonNull int customerId) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
        this.customerId = customerId;
    }

    public FetchBillingPeriodsJob(@Priority int priority) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
        this.customerId = customerId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }


    @Override
    public void onRun() throws Throwable {
        final Call<List<BillingPeriod>> biListCall = apiService.getBillingPeriods();
        Response<List<BillingPeriod>> response = biListCall.execute();

        if (response.isSuccessful()) {
            Log.d("Billing Periods", response.body().get(0).toString());
            doResponse(response.body());
            //Meter oldest = handleResponse(response.body());
            //eventBus.post(new FetchedMeterEvent(customerId, oldest, true));
        } else {
            throw new NetworkException(response.code());
        }

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        eventBus.post(new FetchedBillingPeriodEvent(false));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    @Override
    protected int getRetryLimit() {
        return 2;
    }


    private void doResponse(List<BillingPeriod>  billingPeriods){
        if (billingPeriods != null) {
            billingPeriodModel.saveAll(billingPeriods);
        }
        //return oldest;
    }
}
