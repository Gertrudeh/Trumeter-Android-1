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
public class MeterReader extends BaseModel {
    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("name")
    String name;
    @Column
    @SerializedName("username")
    String username;
    @Column
    @SerializedName("email")
    String email;
    @Column
    @SerializedName("created_at")
    String created_at;
    @Column
    @SerializedName("updated_at")
    String updated_at;
    @Column
    @SerializedName("password_digest")
    String password_digest;
    @Column
    String password;

    public MeterReader() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "MeterReader{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
