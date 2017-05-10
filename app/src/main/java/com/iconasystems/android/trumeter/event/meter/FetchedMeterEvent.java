package com.iconasystems.android.trumeter.event.meter;

import com.iconasystems.android.trumeter.vo.Meter;

/**
 * Created by christoandrew on 12/5/16.
 */

public class FetchedMeterEvent {
    private int customerId;
    private Meter oldest;
    private boolean success;

    public FetchedMeterEvent(int customerId, Meter oldest, boolean success) {
        this.customerId = customerId;
        this.oldest = oldest;
        this.success = success;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Meter getOldest() {
        return oldest;
    }

    public void setOldest(Meter oldest) {
        this.oldest = oldest;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
