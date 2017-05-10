package com.iconasystems.android.trumeter.job.meter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.api.MeterResponse;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.meter.FetchedMeterEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.NetworkException;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.model.MeterModel;
import com.iconasystems.android.trumeter.vo.Meter;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by christoandrew on 12/5/16.
 */

public class FetchMetersJob extends BaseJob {
    private static final String GROUP = "FetchMetersJob";

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
    transient MeterModel meterModel;
    @Inject
    ApiService apiService;


    public FetchMetersJob(@Priority int priority, @NonNull int customerId) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
        this.customerId = customerId;
    }

    public FetchMetersJob(@Priority int priority) {
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
        final Call<List<Meter>> meterCall = apiService.meters();
        Response<List<Meter>> response = meterCall.execute();

        if (response.isSuccessful()) {
            doResponse(response.body());
            //Meter oldest = handleResponse(response.body());
            //eventBus.post(new FetchedMeterEvent(customerId, oldest, true));
        } else {
            throw new NetworkException(response.code());
        }

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        eventBus.post(new FetchedMeterEvent(customerId, null, false));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    @Override
    protected int getRetryLimit() {
        return 2;
    }

    @Nullable
    private Meter handleResponse(MeterResponse body) {
        // We could put these two into a transaction but it is OK to save a user even if we could
        // not save their post so we don't care.
        Meter oldest = null;
        if (body.getMeters() != null) {
            meterModel.saveAll(body.getMeters());
            long since = 0;
            for (Meter meter : body.getMeters()) {
                if (meter.getCreated_at() > since) {
                    since = meter.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > meter.getCreated_at()) {
                    oldest = meter;
                }
            }
            if (since > 0) {
                // meterModel.saveMeterTimestamp(since,mMeterReaderId);
            }
        }
        return oldest;
    }

    private void doResponse(List<Meter>  meters){
        Meter oldest = null;
        if (meters != null) {
            meterModel.saveAll(meters);
            long since = 0;
            for (Meter meter : meters) {
                if (meter.getCreated_at() > since) {
                    since = meter.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > meter.getCreated_at()) {
                    oldest = meter;
                }
            }
            if (since > 0) {
                // meterModel.saveMeterTimestamp(since,mMeterReaderId);
            }
        }
        //return oldest;
    }
}
