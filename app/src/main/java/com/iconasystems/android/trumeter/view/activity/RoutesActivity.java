package com.iconasystems.android.trumeter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iconasystems.android.trumeter.About;
import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.Report;
import com.iconasystems.android.trumeter.adapters.RoutesAdapter;
import com.iconasystems.android.trumeter.controller.ReadingsController;
import com.iconasystems.android.trumeter.model.MeterReadingModel;
import com.iconasystems.android.trumeter.model.TaskModel;
import com.iconasystems.android.trumeter.utils.DateUtils;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;
import com.iconasystems.android.trumeter.utils.GPSTracker;
import com.iconasystems.android.trumeter.vo.MeterReader;
import com.iconasystems.android.trumeter.vo.Route;
import com.iconasystems.android.trumeter.vo.Task;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class RoutesActivity extends BaseActivity {
    @Inject
    TaskModel taskModel;
    Set<Route> routeSet = new HashSet<Route>();
    private MeterReader reader;
    AppPreferences preferences;

    @Inject
    MeterReadingModel meterReadingModel;

    @Inject
    ReadingsController readingsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_routes);

        preferences = AppPreferences.get(this);
        RecyclerView mRouteRecyclerView = (RecyclerView) findViewById(R.id.routes_list);
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.load_progress);
        mRouteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRouteRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

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
        GPSTracker tracker = new GPSTracker(this,this);

        AppPreferences prefs = AppPreferences.get(this);
        reader = prefs.getUser();

        List<Task> taskList;
        taskList = taskModel.loadTasks(reader.getId());

        final List<Route> routesList = new ArrayList<>();
        for (Task task : taskList){
            String townName = task.getTown_name();
            String zoneName = task.getZone_name();
            String zoneCode = task.getZone_code();
            String areaCode = task.getArea_code();
            String routeName = task.getRoute_name();
            int townId = task.getTown_id();
            int zoneId = task.getZone_id();
            int routeId = task.getRoute_id();

            Route route = new Route();
            route.setId(routeId);
            route.setName(routeName);
            //routesList.add(route);
            routeSet.add(route);
            Log.v("Route data", route.toString());

        }
        for (Route route : routeSet){
            routesList.add(route);
            Log.d("Route", route.toString());
        }

        mRouteRecyclerView.setAdapter(new RoutesAdapter(routesList,
                RoutesActivity.this, new RoutesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route zone) {
                Intent customers = new Intent(RoutesActivity.this, CustomersActivity.class);
                customers.putExtra("route_id",String.valueOf(zone.getId()));
                startActivity(customers);
            }
        }));

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
    protected void onStart() {
        super.onStart();
    }
}
