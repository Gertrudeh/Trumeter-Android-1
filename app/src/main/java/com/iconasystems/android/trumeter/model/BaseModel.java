

package com.iconasystems.android.trumeter.model;


import android.database.sqlite.SQLiteDatabase;

import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.di.component.AppComponent;

public class BaseModel {

    protected final SQLiteDatabase mSQLiteDatabase;
    protected final AppComponent mComponent;

    public BaseModel(App app, SQLiteDatabase database) {
        mComponent = app.getAppComponent();
        mSQLiteDatabase = database;
    }
}
