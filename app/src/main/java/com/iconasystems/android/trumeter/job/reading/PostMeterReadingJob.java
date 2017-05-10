package com.iconasystems.android.trumeter.job.reading;

import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.reading.DeleteReadingEvent;
import com.iconasystems.android.trumeter.event.reading.NewReadingEvent;
import com.iconasystems.android.trumeter.event.reading.UpdateReadingEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.model.MeterReadingModel;
import com.iconasystems.android.trumeter.util.L;
import com.iconasystems.android.trumeter.vo.MeterReading;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by christoandrew on 11/28/16.
 */

public class PostMeterReadingJob extends BaseJob {
    @Inject
    transient EventBus mEventBus;

    @Inject
    Config config;

    private int meterId;
    private float currentReading;
    private String isMeteredEntry;
    private MeterReading newReading;
    private float previousReading;
    private String expected_range;
    private boolean isRegularReading;
    private int billingPeriodId;
    private String meterPhoto;
    private float readingLongitude;
    private float lastReadingQuantity;
    private float quantity;
    private float distance;
    private int meterReaderId;
    private float readingLatitude;
    private int readingCode;
    private String reason;
    private String clientId;
    private float lastQuantity;
    MeterReading reading;

    private static final String GROUP = "new_reading";
    @Inject
    transient ApiService mApiService;

    @Inject
    transient MeterReadingModel meterReadingModel;

    @Inject
    transient Config mConfig;

    public PostMeterReadingJob(int meterId, float currentReading, String isMeteredEntry,
                               float previousReading, String expected_range,
                               boolean isRegularReading, int billingPeriodId, String meterPhoto,
                               float readingLongitude, float lastReadingQuantity, float quantity,
                               float distance, int meterReaderId, float readingLatitude,
                               int readingCode, String reason, String clientId, float lastQuantity) {
        super(new Params(BACKGROUND).groupBy(GROUP).requireNetwork().persist());
        this.meterId = meterId;
        this.currentReading = currentReading;
        this.isMeteredEntry = isMeteredEntry;
        this.previousReading = previousReading;
        this.expected_range = expected_range;
        this.isRegularReading = isRegularReading;
        this.billingPeriodId = billingPeriodId;
        this.meterPhoto = meterPhoto;
        this.lastQuantity = lastQuantity;
        this.readingLongitude = readingLongitude;
        this.lastReadingQuantity = lastReadingQuantity;
        this.quantity = quantity;
        this.distance = distance;
        this.meterReaderId = meterReaderId;
        this.readingLatitude = readingLatitude;
        this.readingCode = readingCode;
        this.reason = reason;
        this.clientId = clientId;
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {
        MeterReading newReading = new MeterReading();
        newReading.setMeter_id(meterId);
        newReading.setCurrent_reading(currentReading);
        newReading.setPrevious_reading(previousReading);
        newReading.setPhoto(meterPhoto);
        newReading.setReading_code(readingCode);
        newReading.setLatitude(readingLatitude);
        newReading.setLongitude(readingLongitude);
        newReading.setMeter_reader_id(meterReaderId);
        newReading.setDistance(distance);
        newReading.setQuantity(quantity);
        newReading.setBilling_period_id(billingPeriodId);
        newReading.setRegular(isRegularReading);
        newReading.setReason(reason);
        newReading.setPosted("No");
        newReading.setCustomer_details("Correct");
        newReading.setPrevious_consumption(lastReadingQuantity);
        newReading.setExpected_range(expected_range);
        newReading.setIs_metered_entry(isMeteredEntry);
        newReading.setId(meterReadingModel.generateIdForNewLocalReading());
        newReading.setClient_id(clientId);
        newReading.setPending(true);
        newReading.setLast_quantity(lastQuantity);
        // make sure whatever time we put here is greater / eq to last known time in database.
        // this will work around issues related to client's time.
        // this time is temporary anyways as it will be overriden when it is synched to server
        long readingTs = meterReadingModel.getLatestTimestamp(null);
        long now = System.currentTimeMillis();
        newReading.setCreated(Math.max(readingTs, now) + 1);
        L.d("assigned timestamp %s to the reading", newReading.getCreatedAt());
        meterReadingModel.save(newReading);
        mEventBus.post(new NewReadingEvent(newReading));
    }

    @Override
    public void onRun() throws Throwable {
        MeterReading newReading = new MeterReading();
        newReading.setMeter_id(meterId);
        newReading.setCurrent_reading(currentReading);
        newReading.setPrevious_reading(previousReading);
        newReading.setPhoto(meterPhoto);
        newReading.setReading_code(readingCode);
        newReading.setLatitude(readingLatitude);
        newReading.setLongitude(readingLongitude);
        newReading.setMeter_reader_id(meterReaderId);
        newReading.setDistance(distance);
        newReading.setQuantity(quantity);
        newReading.setBilling_period_id(billingPeriodId);
        newReading.setRegular(isRegularReading);
        newReading.setReason(reason);
        newReading.setPosted("No");
        newReading.setCustomer_details("Correct");
        newReading.setPrevious_consumption(lastReadingQuantity);
        newReading.setExpected_range(expected_range);
        newReading.setIs_metered_entry(isMeteredEntry);
        newReading.setClient_id(clientId);
        newReading.setLast_quantity(lastQuantity);
        Log.d("Starus", "Starting posting job");
        MeterReading reading = meterReadingModel.loadByClientIdAndMeterId(clientId, meterId);
        if (reading != null && !reading.isPending()) {
            // looks like post probably arrived from somewhere else. Good Job!
            mEventBus.post(new UpdateReadingEvent(reading));
            return;
        }
       /* Response<MeterReadingResponse> response = mApiService.postReading(meterId,currentReading,previousReading,
                meterPhoto,readingCode,readingLatitude,readingLongitude,meterReaderId,distance,quantity,billingPeriodId,
                String.valueOf(isRegularReading),reason,"Correct","No",lastReadingQuantity,expected_range,isMeteredEntry)
                .execute();*/

        Response<MeterReading> response = mApiService.saveReading(newReading).execute();
        Call<MeterReading> call = mApiService.saveReading(newReading);
        call.enqueue(new Callback<MeterReading>() {
            @Override
            public void onResponse(Call<MeterReading> call, Response<MeterReading> response) {
                MeterReading body = response.body();
                body.setPending(false);
                meterReadingModel.save(body);
                mEventBus.post(new UpdateReadingEvent(body));
            }

            @Override
            public void onFailure(Call<MeterReading> call, Throwable t) {
              Log.d("Network Error", "Job pushed to background");
            }
        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        MeterReading reading = meterReadingModel.loadByBillingPeriodAndMeterId(billingPeriodId, meterId);
        if (reading != null) {
            meterReadingModel.delete(reading);
        }
        mEventBus.post(new DeleteReadingEvent(reading, true));
    }

    @Override
    protected int getRetryLimit() {
        return mConfig.getNewReadingRetryCount();
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        if (shouldRetry(throwable)) {
            // For the purposes of the demo, just back off 250 ms.
            RetryConstraint constraint = RetryConstraint
                    .createExponentialBackoff(runCount, 250);
            constraint.setApplyNewDelayToGroup(true);
            return constraint;
        }
        return RetryConstraint.CANCEL;
    }

}
