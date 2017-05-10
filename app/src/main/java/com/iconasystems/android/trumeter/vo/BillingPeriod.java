package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/18/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class BillingPeriod extends BaseModel{
    @Column
    @SerializedName("start_date")
    String start_date;
    @Column
    @SerializedName("end_date")
    String end_date;
    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;

    public BillingPeriod() {
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BillingPeriod{" +
                "start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", id=" + id +
                '}';
    }
}
