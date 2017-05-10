package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/7/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Customer extends BaseModel {
    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("customer_name")
    String name;
    @Column
    @SerializedName("address")
    String address;
    @Column
    @SerializedName("no")
    String meter_number;
    @Column
    @SerializedName("route_name")
    String route_name;
    @Column
    @SerializedName("route_id")
    int route_id;
    @Column
    @SerializedName("meter_id")
    int meter_id;
    @Column
    @SerializedName("created_at")
    String created_at;
    @Column
    @SerializedName("posting_group")
    String posting_group;

    @Column
    @SerializedName("last_reading_code")
    int last_reading_code;

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getMeter_id() {
        return meter_id;
    }

    public void setMeter_id(int meter_id) {
        this.meter_id = meter_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPosting_group() {
        return posting_group;
    }

    public void setPosting_group(String posting_group) {
        this.posting_group = posting_group;
    }

    public String compositeUniqueKey() {
        return route_id + "/" + id;
    }

    public int getLast_reading_code() {
        return last_reading_code;
    }

    public void setLast_reading_code(int last_reading_code) {
        this.last_reading_code = last_reading_code;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", meter_number='" + meter_number + '\'' +
                ", route_name='" + route_name + '\'' +
                ", route_id=" + route_id +
                ", meter_id=" + meter_id +
                ", created_at='" + created_at + '\'' +
                ", posting_group='" + posting_group + '\'' +
                ", last_reading_code=" + last_reading_code +
                '}';
    }


}
