package com.iconasystems.android.trumeter.config;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.di.component.DaggerAppComponent;
import com.iconasystems.android.trumeter.di.module.ApplicationModule;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by christoandrew on 11/20/16.
 */

@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://192.169.150.106:5984/acra-trumeter/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "trumeter",
        formUriBasicAuthPassword = "admin123",
        resToastText = R.string.crash_toast_text,
        // Your usual ACRA configuration
        mode = ReportingInteractionMode.TOAST



)
public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // This instantiates DBFlow
        FlowManager.init(this);
        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        // add for verbose logging
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        ACRA.init(this);

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @VisibleForTesting
    public void setAppComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
