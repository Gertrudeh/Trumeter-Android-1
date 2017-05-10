package com.iconasystems.android.trumeter.vo;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/10/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Session extends BaseModel{
    @PrimaryKey
    @Column
    @SerializedName("username")
    String username;
    @Column
    @SerializedName("password")
    String password;

    public Session() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
