package com.iconasystems.android.trumeter.config;

/**
 * Created by christoandrew on 11/20/16.
 */
import com.raizlabs.android.dbflow.annotation.Database;
@Database(name = TrumeterDatabase.NAME,version = TrumeterDatabase.VERSION)
public class TrumeterDatabase {
    public static final String NAME = "TrumeterDataBase";
    public static final int VERSION = 1;
}
