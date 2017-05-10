package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/23/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Issue extends BaseModel {
    @PrimaryKey
    @Column
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("meter_reader_id")
    int meter_reader_id;
    @Column
    @SerializedName("text")
    String text;
    @Column
    @SerializedName("title")
    String title;

    public Issue() {
    }

    public int getMeter_reader_id() {
        return meter_reader_id;
    }

    public void setMeter_reader_id(int meter_reader_id) {
        this.meter_reader_id = meter_reader_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "meter_reader_id=" + meter_reader_id +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
