package com.iconasystems.android.trumeter.event.customer;

import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.vo.Customer;

/**
 * Created by christoandrew on 12/4/16.
 */

public class FetchedCustomersEvent {

    @Nullable
    private final int customerId;
    private final Customer mOldest;
    private final boolean mSuccess;

    public FetchedCustomersEvent(boolean mSuccess, int customerId, Customer mOldest) {
        this.mSuccess = mSuccess;
        this.customerId = customerId;
        this.mOldest = mOldest;
    }

    public boolean ismSuccess() {
        return mSuccess;
    }

    @Nullable
    public int getUserId() {
        return customerId;
    }

    public Customer getOldest() {
        return mOldest;
    }
}
