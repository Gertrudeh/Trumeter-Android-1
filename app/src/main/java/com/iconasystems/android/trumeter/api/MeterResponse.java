package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.vo.Meter;

import java.util.List;

/**
 * Created by christoandrew on 12/5/16.
 */

public class MeterResponse {
    private List<Meter> meters;

    public List<Meter> getMeters() {
        return meters;
    }

    public void setMeters(List<Meter> meters) {
        this.meters = meters;
    }
}
