package com.iconasystems.android.trumeter.event.task;

import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.vo.Task;

/**
 * Created by christoandrew on 11/29/16.
 */

public class DeleteTaskEvent {

    @Nullable
    private final Task mTask;
    private final boolean mSyncFailure;
    private boolean mNotifiedUser;

    public DeleteTaskEvent(boolean syncFailure, @Nullable Task task) {
        mTask = task;
        mSyncFailure = syncFailure;
    }

    public void markAsNotifiedUser() {
        mNotifiedUser = true;
    }

    public boolean didNotifyUser() {
        return mNotifiedUser;
    }

    @Nullable
    public Task getTask() {
        return mTask;
    }

    public boolean isSyncFailure() {
        return mSyncFailure;
    }
}
