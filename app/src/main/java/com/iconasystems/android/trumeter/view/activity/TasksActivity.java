package com.iconasystems.android.trumeter.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.iconasystems.android.trumeter.About;
import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.Report;
import com.iconasystems.android.trumeter.adapters.TownZoneAdapter;
import com.iconasystems.android.trumeter.controller.MetersController;
import com.iconasystems.android.trumeter.controller.ReadingsController;
import com.iconasystems.android.trumeter.controller.TasksController;
import com.iconasystems.android.trumeter.databinding.ActivityTasksBinding;
import com.iconasystems.android.trumeter.event.SubscriberPriority;
import com.iconasystems.android.trumeter.event.task.FetchedTaskEvent;
import com.iconasystems.android.trumeter.model.TaskModel;
import com.iconasystems.android.trumeter.util.TimestampTracker;
import com.iconasystems.android.trumeter.utils.DateUtils;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;
import com.iconasystems.android.trumeter.utils.InterfaceUtils;
import com.iconasystems.android.trumeter.view.TasksAdapter;
import com.iconasystems.android.trumeter.vo.MeterReader;
import com.iconasystems.android.trumeter.vo.Route;
import com.iconasystems.android.trumeter.vo.Task;
import com.iconasystems.android.trumeter.vo.TownZone;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class TasksActivity extends BaseActivity {

    @Inject
    EventBus mEventBus;
    @Inject
    TaskModel taskModel;
    @Inject
    Config config;

    @Inject
    ReadingsController readingsController;

    @Inject
    Context context;

    @Inject
    MetersController metersController;


    private ActivityTasksBinding taskBinding;
    private TasksAdapter tasksAdapter;

    @Inject
    TasksController tasksController;

    LinearLayoutManager mLinearLayoutManager;

    // Lock to avoid creating multiple refresh jobs
    private boolean mPendingRefresh = false;

    // Tracks event timestamps which will be used when querying disk for new items
    private final TimestampTracker mTimestampTracker = new TimestampTracker();

    // Whose feed is this. COMMON_FEED_USER_ID is common feed
    private int mReaderId;

    // Set when Activity starts. Since activity does not listen for events after being stopped, we
    // need to do a full sync on return. Event cycle can be moved between onCreate/onDestroy to
    // avoid this but that will require additional complexity of checking when to update the views.
    private boolean mRefreshFull;
    private MeterReader reader;
    private Set<TownZone> towns;
    private  AppPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        taskBinding = DataBindingUtil.setContentView(this, R.layout.activity_tasks);
        preferences = AppPreferences.get(this);
        taskBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               tasksController.fetchTasksAsync(true, reader.getId());
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        TextView tmDate = (TextView) findViewById(R.id.time_date);
        LocalDate localDateTime = LocalDate.now();
        int day = localDateTime.getDayOfWeek();
        int dayMonth = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthOfYear();
        int year = localDateTime.getYear();
        String dayOfWeek = DateUtils.parseDayToString(day);
        String monthOfYear = DateUtils.parseMonthToString(month);
        String dt = DateUtils.dateBuilder(dayOfWeek, dayMonth, monthOfYear, year);
        tmDate.setText(dt);


        AppPreferences prefs = AppPreferences.get(this);
        reader = prefs.getUser();

        final List<TownZone> townZoneList = new ArrayList<>();
        final List<Route> routeList = new ArrayList<>();
        final Set<TownZone> townZoneSet = new HashSet<>();
        final Set<Route> routeSet = new HashSet<>();
        towns = new HashSet<>();

        List<Task> tasks = taskModel.loadTasks(reader.getId());

                    for (Task task : tasks) {
                        String townName = task.getTown_name();
                        String zoneName = task.getZone_name();
                        String zoneCode = task.getZone_code();
                        String areaCode = task.getArea_code();
                        int townId = task.getTown_id();
                        int zoneId = task.getZone_id();
                        String routeName = task.getRoute_name();
                        int routeId = task.getRoute_id();
                        Route route = new Route();
                        route.setId(routeId);
                        route.setName(routeName);

                        routeSet.add(route);

                        TownZone townZone = new TownZone();
                        townZone.setArea_code(areaCode);
                        townZone.setTown_name(townName);
                        townZone.setTown_id(townId);
                        townZone.setZone_code(zoneCode);
                        townZone.setZone_id(zoneId);
                        townZone.setZone_name(zoneName);

                        towns.add(townZone);
                        townZoneSet.add(townZone);
                    }

                    for(TownZone zone : towns){
                        townZoneList.add(zone);
                    }

                    for (Route route :routeSet){
                        routeList.add(route);
                    }
                    InterfaceUtils.showProgress(getApplicationContext(), false, taskBinding.townsList, taskBinding.loadTownsProgress);

                    taskBinding.townsList.setAdapter(new TownZoneAdapter(townZoneList,
                            new TownZoneAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(TownZone townZone) {
                                    Intent zonesIntent = new Intent(context, RoutesActivity.class);
                                    startActivity(zonesIntent);
                                }
                            }));






       // initRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_report:
                startActivity(new Intent(this, Report.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, About.class));
            case R.id.action_logout:
                preferences.setLoggedIn(null);
                startActivity(new Intent(this, LoginActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, BillingPeriodsActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRefreshFull = true;
        mEventBus.register(this, SubscriberPriority.HIGH);
       // refresh(null);
        tasksController.fetchTasksAsync(true, reader.getId());
    }

   /* private void refresh(Task referenceTask) {
        if (mPendingRefresh) {
            if (referenceTask != null) {
                mTimestampTracker.updateNext(referenceTask.getCreated_at() - 1);
            }
            return;
        }
        if (referenceTask != null) {
            mTimestampTracker.updateCurrent(referenceTask.getCreated_at() - 1);
        }
        final long reference;
        final boolean swapList = mRefreshFull;
        mRefreshFull = false;
        if (swapList) {
            reference = 0L;
        } else if (mTimestampTracker.hasTimestamp()) {
            reference = Math.min(mTimestampTracker.getCurrent(),
                    tasksAdapter.getReferenceTimestamp());
        } else {
            reference = tasksAdapter.getReferenceTimestamp();
        }

        L.d("refreshing with reference time %s", reference);
        new AutoCancelAsyncTask<Void, List<Task>>(this) {
            @Override
            protected void onResult(List<Task> tasks) {
                L.d("feed model returned with %s items", tasks.size());
                if (swapList) {
                    tasksAdapter.swapList(tasks);
                } else {
                    tasksAdapter.insertAll(tasks);
                }

                if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    mLinearLayoutManager.scrollToPosition(0);
                }
                mTimestampTracker.swap();
                mPendingRefresh = false;
                if (mTimestampTracker.hasTimestamp()) {
                    taskBinding.getRoot().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh(null);
                        }
                    }, 1);
                }
            }

            @Override
            protected List<Task> onDoInBackground(Void... params) {
                L.d("time to query feed model");
                return taskModel.loadTasks(reader.getId());
            }
        }.execute();
    }*/

    public void onEventMainThread(FetchedTaskEvent event) {
        //noinspection NumberEquality
        if (event.ismSuccess()) {
            //refresh(event.getmOldest());
        } else {
            Snackbar.make(taskBinding.container,
                    "Cannot refresh tasks", Snackbar.LENGTH_SHORT).show();
        }
        taskBinding.swipeContainer.setRefreshing(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
        taskBinding.swipeContainer.setRefreshing(false);
        mPendingRefresh = false;
        mTimestampTracker.reset();
    }

    private void initRecyclerView() {
        tasksAdapter = new TasksAdapter(this);
        final List<Route> routeList = new ArrayList<>();
        tasksAdapter.setCallback(new TasksAdapter.Callback() {
            @Override
            public void onTaskClick(Task task) {
                Intent zonesIntent = new Intent(TasksActivity.this, RoutesActivity.class);
                startActivity(zonesIntent);
            }
        });
        tasksAdapter.notifyDataSetChanged();
        taskBinding.townsList.setAdapter(tasksAdapter);
        taskBinding.townsList.setHasFixedSize(true);
        taskBinding.townsList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mLinearLayoutManager = (LinearLayoutManager) taskBinding.townsList.getLayoutManager();
    }
}
