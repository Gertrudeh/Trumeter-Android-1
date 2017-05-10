package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/12/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class MeterReading extends BaseModel {
    @Column
    @PrimaryKey
    @SerializedName("id")
    long id;
    @Column
    @SerializedName("meter_id")
    int meter_id;
    @Column
    @SerializedName("current_reading")
    float current_reading;
    @Column
    @SerializedName("previous_reading")
    float previous_reading;
    @Column
    @SerializedName("photo")
    String photo;
    @Column
    @SerializedName("reading_code")
    int reading_code;
    @Column
    @SerializedName("latitude")
    float latitude;
    @Column
    @SerializedName("longitude")
    float longitude;
    @Column
    @SerializedName("meter_reader_id")
    int meter_reader_id;
    @Column
    @SerializedName("distance")
    float distance;
    @Column
    @SerializedName("quantity")
    float quantity;
    @Column
    @SerializedName("billing_period_id")
    int billing_period_id;
    @Column
    @SerializedName("regular")
    boolean regular;
    @Column
    @SerializedName("reason")
    String reason;
    @Column
    @SerializedName("created_at")
    String created_at;
    @Column
    @SerializedName("customer_details")
    String customer_details;
    @Column
    @SerializedName("posted")
    String posted;
    @Column
    @SerializedName("previous_consumption")
    float previous_consumption;
    @Column
    @SerializedName("expected_range")
    String expected_range;
    @Column
    @SerializedName("is_metered_entry")
    private String is_metered_entry;
    @Column
    boolean pending;
    @Column
    @SerializedName("client_id")
    String client_id;
    @Column
    @SerializedName("last_quantity")
    float last_quantity;

    public MeterReading() {
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMeter_id() {
        return meter_id;
    }

    public void setMeter_id(int meter_id) {
        this.meter_id = meter_id;
    }

    public float getCurrent_reading() {
        return current_reading;
    }

    public void setCurrent_reading(float current_reading) {
        this.current_reading = current_reading;
    }

    public float getPrevious_reading() {
        return previous_reading;
    }

    public void setPrevious_reading(float previous_reading) {
        this.previous_reading = previous_reading;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getReading_code() {
        return reading_code;
    }

    public void setReading_code(int reading_code) {
        this.reading_code = reading_code;
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

    public int getMeter_reader_id() {
        return meter_reader_id;
    }

    public void setMeter_reader_id(int meter_reader_id) {
        this.meter_reader_id = meter_reader_id;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getBilling_period_id() {
        return billing_period_id;
    }

    public void setBilling_period_id(int billing_period_id) {
        this.billing_period_id = billing_period_id;
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(String customer_details) {
        this.customer_details = customer_details;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public float getPrevious_consumption() {
        return previous_consumption;
    }

    public void setPrevious_consumption(float previous_consumption) {
        this.previous_consumption = previous_consumption;
    }

    public String getExpected_range() {
        return expected_range;
    }

    public void setExpected_range(String expected_range) {
        this.expected_range = expected_range;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getIs_metered_entry() {
        return is_metered_entry;
    }

    public void setIs_metered_entry(String is_metered_entry) {
        this.is_metered_entry = is_metered_entry;
    }

    public void setCreated(long created_at) {
        created_at = created_at;
    }

    public float getLast_quantity() {
        return last_quantity;
    }

    public void setLast_quantity(float last_quantity) {
        this.last_quantity = last_quantity;
    }

    @Override
    public String toString() {
        return "MeterReading{" +
                "meter_id=" + meter_id +
                ", current_reading=" + current_reading +
                ", previous_reading=" + previous_reading +
                ", photo='" + photo + '\'' +
                ", reading_code=" + reading_code +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", meter_reader_id=" + meter_reader_id +
                ", distance=" + distance +
                ", quantity=" + quantity +
                ", billing_period_id=" + billing_period_id +
                ", regular=" + regular +
                ", reason='" + reason + '\'' +
                ", customer_details='" + customer_details + '\'' +
                ", posted='" + posted + '\'' +
                ", previous_consumption=" + previous_consumption +
                ", expected_range='" + expected_range + '\'' +
                '}';
    }


}
