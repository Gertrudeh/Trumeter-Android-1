package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/17/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class TownZone extends BaseModel{
    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("id")
    int town_zone;
    @Column
    @SerializedName("town_name")
    String town_name;
    @Column
    @SerializedName("area_code")
    String area_code;
    @Column
    @SerializedName("town_id")
    int town_id;
    @Column
    @SerializedName("zone_id")
    int zone_id;
    @Column
    @SerializedName("zone_name")
    String zone_name;
    @Column
    @SerializedName("zone_code")
    String zone_code;

    public TownZone() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TownZone)) return false;

        TownZone townZone = (TownZone) o;

        if (getTown_id() != townZone.getTown_id()) return false;
        if (getZone_id() != townZone.getZone_id()) return false;
        if (!getTown_name().equals(townZone.getTown_name())) return false;
        if (!getArea_code().equals(townZone.getArea_code())) return false;
        if (!getZone_name().equals(townZone.getZone_name())) return false;
        return getZone_code().equals(townZone.getZone_code());

    }

    @Override
    public int hashCode() {
        int result = getTown_name().hashCode();
        result = 31 * result + getArea_code().hashCode();
        result = 31 * result + getTown_id();
        result = 31 * result + getZone_id();
        result = 31 * result + getZone_name().hashCode();
        result = 31 * result + getZone_code().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TownZone{" +
                "town_name='" + town_name + '\'' +
                ", area_code='" + area_code + '\'' +
                ", town_id=" + town_id +
                ", zone_id=" + zone_id +
                ", zone_name='" + zone_name + '\'' +
                ", zone_code='" + zone_code + '\'' +
                '}';
    }


}
