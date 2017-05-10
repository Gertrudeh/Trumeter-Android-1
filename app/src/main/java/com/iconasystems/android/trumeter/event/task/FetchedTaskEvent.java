package com.iconasystems.android.trumeter.event.task;

import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.vo.Task;

/**
 * Created by christoandrew on 11/28/16.
 */

public class FetchedTaskEvent {
    private final boolean mSuccess;
    @Nullable
    private final int mReaderId;
    private final Task mOldest;

    public FetchedTaskEvent(boolean mSuccess, int mUserId, Task mOldest) {
        this.mSuccess = mSuccess;
        this.mReaderId = mUserId;
        this.mOldest = mOldest;
    }

    public boolean ismSuccess() {
        return mSuccess;
    }

    @Nullable
    public int getmUserId() {
        return mReaderId;
    }

    public Task getmOldest() {
        return mOldest;
    }
}
