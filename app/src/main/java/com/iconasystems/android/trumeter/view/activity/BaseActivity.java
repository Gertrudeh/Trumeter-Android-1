package com.iconasystems.android.trumeter.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.birbit.android.jobqueue.TagConstraint;
import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.di.component.ActivityComponent;
import com.iconasystems.android.trumeter.di.component.DaggerActivityComponent;
import com.iconasystems.android.trumeter.util.LifecycleListener;
import com.iconasystems.android.trumeter.util.LifecycleProvider;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by christoandrew on 11/28/16.
 */

public class BaseActivity extends AppCompatActivity implements LifecycleProvider {
    private ActivityComponent mComponent;
    private String mSessionId;
    private final CopyOnWriteArrayList<LifecycleListener> mLifecycleListeners
            = new CopyOnWriteArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .appComponent(getApp().getAppComponent()).build();
    }
    protected App getApp() {
        return (App) getApplicationContext();
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSessionId = UUID.randomUUID().toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (LifecycleListener callback : mLifecycleListeners) {
            callback.onProviderStopped();
        }
        getComponent().jobManager().cancelJobsInBackground(null, TagConstraint.ALL, mSessionId);
        mLifecycleListeners.clear();
    }

    public String getSessionId() {
        return mSessionId;
    }


    public void addLifecycleListener(LifecycleListener listener) {
        mLifecycleListeners.add(listener);
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        mLifecycleListeners.remove(listener);
    }
}
