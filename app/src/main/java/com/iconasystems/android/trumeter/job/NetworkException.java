

package com.iconasystems.android.trumeter.job;

import java.net.ConnectException;

public class NetworkException extends ConnectException {

    private final int mErrorCode;

    public NetworkException(int errorCode) {
        mErrorCode = errorCode;
    }

    public boolean shouldRetry() {
        return mErrorCode < 400 || mErrorCode > 499;
    }
}
