package com.iconasystems.android.trumeter.event.reading;

import com.iconasystems.android.trumeter.vo.MeterReading;

/**
 * Created by christoandrew on 11/29/16.
 */

public class UpdateReadingEvent {
    MeterReading meterReading;

    public UpdateReadingEvent(MeterReading meterReading) {
        this.meterReading = meterReading;
    }

    public MeterReading getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(MeterReading meterReading) {
        this.meterReading = meterReading;
    }
}
