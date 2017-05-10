package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by christoandrew on 10/22/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Task extends BaseModel {
    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("meter_reader_id")
    int meter_reader_id;
    @Column
    @SerializedName("town_name")
    String town_name;
    @Column
    @SerializedName("area_code")
    String area_code;
    @Column
    @SerializedName("zone_name")
    String zone_name;
    @Column
    @SerializedName("zone_code")
    String zone_code;
    @Column
    @SerializedName("status")
    boolean status;
    @Column
    @SerializedName("town_id")
    int town_id;
    @Column
    @SerializedName("zone_id")
    int zone_id;
    @Column
    @SerializedName("route_id")
    int route_id;
    @Column
    @SerializedName("route_name")
    String route_name;
    @Column
    @SerializedName("created_at")
    String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTown_name() {
        return town_name;
    }

    public void setTown_name(String town_name) {
        this.town_name = town_name;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getZone_code() {
        return zone_code;
    }

    public void setZone_code(String zone_code) {
        this.zone_code = zone_code;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTown_id() {
        return town_id;
    }

    public void setTown_id(int town_id) {
        this.town_id = town_id;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public int getMeter_reader_id() {
        return meter_reader_id;
    }

    public void setMeter_reader_id(int meter_reader_id) {
        this.meter_reader_id = meter_reader_id;
    }

    public boolean isStatus() {
        return status;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public long getCreated_at() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return 0;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String compositeUniqueKey() {
        return id + "/" + meter_reader_id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", town_name='" + town_name + '\'' +
                ", area_code='" + area_code + '\'' +
                ", zone_name='" + zone_name + '\'' +
                ", zone_code='" + zone_code + '\'' +
                ", status=" + status +
                ", town_id=" + town_id +
                ", zone_id=" + zone_id +
                ", route_id=" + route_id +
                ", route_name='" + route_name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (getId() != task.getId()) return false;
        if (getMeter_reader_id() != task.getMeter_reader_id()) return false;
        if (isStatus() != task.isStatus()) return false;
        if (getTown_id() != task.getTown_id()) return false;
        if (getZone_id() != task.getZone_id()) return false;
        if (getRoute_id() != task.getRoute_id()) return false;
        if (!getTown_name().equals(task.getTown_name())) return false;
        if (!getArea_code().equals(task.getArea_code())) return false;
        if (!getZone_name().equals(task.getZone_name())) return false;
        if (!getZone_code().equals(task.getZone_code())) return false;
        if (!getRoute_name().equals(task.getRoute_name())) return false;
        return getCreated_at() == task.getCreated_at();

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getMeter_reader_id();
        result = 31 * result + getTown_name().hashCode();
        result = 31 * result + getArea_code().hashCode();
        result = 31 * result + getZone_name().hashCode();
        result = 31 * result + getZone_code().hashCode();
        result = 31 * result + (isStatus() ? 1 : 0);
        result = 31 * result + getTown_id();
        result = 31 * result + getZone_id();
        result = 31 * result + getRoute_id();
        result = 31 * result + getRoute_name().hashCode();
        return result;
    }
}
