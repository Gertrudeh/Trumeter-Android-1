package com.iconasystems.android.trumeter.job.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.api.TaskResponse;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.task.FetchedTaskEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.job.NetworkException;
import com.iconasystems.android.trumeter.model.TaskModel;
import com.iconasystems.android.trumeter.vo.Route;
import com.iconasystems.android.trumeter.vo.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by christoandrew on 11/28/16.
 */

public class FetchTasksJob extends BaseJob {

    private static final String GROUP = "FetchTasksJob";
    private int mMeterReaderId;

    @Inject
    transient EventBus eventBus;
    @Inject
    transient Config config;
    @Inject
    transient TaskModel taskModel;
    @Inject
    ApiService apiService;
    private long timestamp = 0;

    private final List<Route> routeList = new ArrayList<>();

    public FetchTasksJob(@Priority int priority, @NonNull int  mMeterReaderId) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
        this.mMeterReaderId = mMeterReaderId;
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        final Call<TaskResponse> tasks = apiService.tasks(mMeterReaderId);
        Response<TaskResponse> response = tasks.execute();

        if (response.isSuccessful()){
            Task oldest = handleResponse(response.body());
            eventBus.post(new FetchedTaskEvent(true,mMeterReaderId,oldest));
        }else {
            throw new NetworkException(response.code());
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        eventBus.post(new FetchedTaskEvent(false, mMeterReaderId, null));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (shouldRetry(throwable)) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }
        return RetryConstraint.CANCEL;
    }

    @Override
    protected int getRetryLimit() {
        return 2;
    }

    @Nullable
    private Task handleResponse(TaskResponse body) {
        // We could put these two into a transaction but it is OK to save a user even if we could
        // not save their post so we don't care.
        Task oldest = null;
        if (body.getTasks() != null) {
            taskModel.saveAll(body.getTasks());
            long since = 0;
            for (Task task : body.getTasks()) {
                Route route = new Route();
                route.setName(task.getRoute_name());
                route.setId(task.getRoute_id());
                routeList.add(route);
                if (task.getCreated_at() > since) {
                    since = task.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > task.getCreated_at()) {
                    oldest = task;
                }
            }
            if (since > 0) {
               // taskModel.saveTaskTimestamp(since,mMeterReaderId);
            }
        }
        return oldest;
    }

    public List<Route> getRouteList() {
        return routeList;
    }
}
