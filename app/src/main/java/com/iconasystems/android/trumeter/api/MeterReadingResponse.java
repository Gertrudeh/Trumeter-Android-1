package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.vo.MeterReading;

/**
 * Created by christoandrew on 11/28/16.
 */

public class MeterReadingResponse {
  MeterReading reading;

    public MeterReading getReading() {
        return reading;
    }

    public void setReading(MeterReading reading) {
        this.reading = reading;
    }
}
