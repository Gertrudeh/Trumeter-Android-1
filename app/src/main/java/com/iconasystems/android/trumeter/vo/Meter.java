package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/14/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Meter extends BaseModel{
    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("customer_id")
    int customer_id;
    @Column
    @SerializedName("meter_number")
    String meter_umber;
    @Column
    @SerializedName("posting_group")
    String posting_group;
    @Column
    @SerializedName("zone_id")
    int zone_id;
    @Column
    @SerializedName("latitude")
    float latitude;
    @Column
    @SerializedName("longitude")
    float longitude;
    @Column
    @SerializedName("route_id")
    int route_id;
    @Column
    @SerializedName("created_at")
    String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getMeter_umber() {
        return meter_umber;
    }

    public void setMeter_umber(String meter_umber) {
        this.meter_umber = meter_umber;
    }

    public String getPosting_group() {
        return posting_group;
    }

    public void setPosting_group(String posting_group) {
        this.posting_group = posting_group;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public long getCreated_at() {
        return 0;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Meter{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", meter_umber='" + meter_umber + '\'' +
                ", posting_group='" + posting_group + '\'' +
                ", zone_id=" + zone_id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
