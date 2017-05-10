package com.iconasystems.android.trumeter.job.reading;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.reading.FetchedReadingsEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.NetworkException;
import com.iconasystems.android.trumeter.model.MeterReadingModel;
import com.iconasystems.android.trumeter.vo.MeterReading;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by christoandrew on 11/28/16.
 */

public class FetchMeterReadingJob extends BaseJob {
    private static final String GROUP = "FetchMeterReadingJob";

    @Inject
    transient EventBus eventBus;
    @Inject
    transient Config config;
    @Inject
    transient MeterReadingModel meterReadingModel;
    @Inject
    ApiService apiService;
    private long timestamp = 0;

    public FetchMeterReadingJob(@Priority int priority) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        final Call<List<MeterReading>> readings = apiService.readings();
        Response<List<MeterReading>> response = readings.execute();

        if (response.isSuccessful()){
            Log.d("Total readings", response.body().size()+"");
            //MeterReading oldest = handleResponse(response.body());
            //eventBus.post(new FetchedReadingsEvent(true,oldest));
            doResponse(response.body());
        }else {
            throw new NetworkException(response.code());
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        eventBus.post(new FetchedReadingsEvent(false,null));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (shouldRetry(throwable)) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }
        return RetryConstraint.CANCEL;
    }

    @Override
    protected int getRetryLimit() {
        return 2;
    }

  /*  @Nullable
    private MeterReading handleResponse(MeterReadingResponse body) {
        // We could put these two into a transaction but it is OK to save a user even if we could
        // not save their post so we don't care.
        MeterReading oldest = null;
        if (body.getMeterReadings() != null) {
            Log.d("Total Readings", body.getMeterReadings().size()+"");
            meterReadingModel.saveAll(body.getMeterReadings());
            long since = 0;
            for (MeterReading reading : body.getMeterReadings()) {
                if (reading.getCreated_at() > since) {
                    since = reading.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > reading.getCreated_at()) {
                    oldest = reading;
                }
            }
            if (since > 0) {
                // taskModel.saveTaskTimestamp(since,mMeterReaderId);
            }
        }
        return oldest;
    }*/

    private void doResponse(List<MeterReading> readings){
        MeterReading oldest = null;
        if (readings != null) {
            Log.d("Total Readings", readings.size()+"");
            meterReadingModel.saveAll(readings);
           /* long since = 0;
            for (MeterReading reading : readings) {
                if (reading.getCreated_at() > since) {
                    since = reading.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > reading.getCreated_at()) {
                    oldest = reading;
                }
            }
            if (since > 0) {
                // taskModel.saveTaskTimestamp(since,mMeterReaderId);
            }*/
        }
        //return oldest;
    }

}
