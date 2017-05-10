package com.iconasystems.android.trumeter.event.billing_period;

/**
 * Created by christoandrew on 12/13/16.
 */

public class FetchedBillingPeriodEvent {
    boolean success;

    public FetchedBillingPeriodEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
