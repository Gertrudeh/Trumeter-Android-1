package com.iconasystems.android.trumeter.event.reading;

import com.iconasystems.android.trumeter.vo.MeterReading;

/**
 * Created by christoandrew on 11/29/16.
 */

public class DeleteReadingEvent {
    private final MeterReading meterReading;
    private final boolean mSyncFailure;
    private boolean mNotifiedUser;

    public DeleteReadingEvent(MeterReading meterReading, boolean mSyncFailure) {
        this.meterReading = meterReading;
        this.mSyncFailure = mSyncFailure;
    }

    public MeterReading getMeterReading() {
        return meterReading;
    }

    public boolean isSyncFailure() {
        return mSyncFailure;
    }

    public void markAsNotifiedUser() {
        mNotifiedUser = true;
    }

    public boolean didNotifyUser() {
        return mNotifiedUser;
    }
}
