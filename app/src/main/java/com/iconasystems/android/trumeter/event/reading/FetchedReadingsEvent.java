package com.iconasystems.android.trumeter.event.reading;

import com.iconasystems.android.trumeter.vo.MeterReading;

/**
 * Created by christoandrew on 12/12/16.
 */

public class FetchedReadingsEvent {
    boolean isSuccess;
    MeterReading oldest;

    public FetchedReadingsEvent(boolean isSuccess, MeterReading oldest) {
        this.isSuccess = isSuccess;
        this.oldest = oldest;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public MeterReading getOldest() {
        return oldest;
    }

    public void setOldest(MeterReading oldest) {
        this.oldest = oldest;
    }
}
