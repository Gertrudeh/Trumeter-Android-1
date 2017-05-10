

package com.iconasystems.android.trumeter.util;

public class ValidationFailedException extends RuntimeException {

    public ValidationFailedException() {
    }

    public ValidationFailedException(String detailMessage) {
        super(detailMessage);
    }
}
