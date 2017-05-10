package com.iconasystems.android.trumeter.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.iconasystems.android.trumeter.SearchActivity;
import com.iconasystems.android.trumeter.adapters.CustomersAdapter;
import com.iconasystems.android.trumeter.controller.CustomersController;
import com.iconasystems.android.trumeter.databinding.ActivityCustomersBinding;
import com.iconasystems.android.trumeter.event.SubscriberPriority;
import com.iconasystems.android.trumeter.event.customer.FetchedCustomersEvent;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.util.TimestampTracker;
import com.iconasystems.android.trumeter.utils.DateUtils;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.MeterReader;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;


/**
 * Created by christoandrew on 12/4/16.
 */

public class CustomersActivity extends BaseActivity {
    @Inject
    EventBus mEventBus;
    @Inject
    CustomerModel customerModel;
    @Inject
    Config config;
    @Inject
    Context context;

    private ActivityCustomersBinding binding;
    private CustomersAdapter customersAdapter;

    AppPreferences preferences;
    @Inject
    CustomersController customersController;

    LinearLayoutManager mLinearLayoutManager;

    // Lock to avoid creating multiple refresh jobs
    private boolean mPendingRefresh = false;

    // Tracks event timestamps which will be used when querying disk for new items
    private final TimestampTracker mTimestampTracker = new TimestampTracker();

    // Whose feed is this. COMMON_FEED_USER_ID is common feed
    private int routeId;

    // Set when Activity starts. Since activity does not listen for events after being stopped, we
    // need to do a full sync on return. Event cycle can be moved between onCreate/onDestroy to
    // avoid this but that will require additional complexity of checking when to update the views.
    private boolean mRefreshFull;
    private MeterReader reader;
    private MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        routeId = Integer.valueOf(getIntent().getStringExtra("route_id"));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customers);

        preferences = AppPreferences.get(this);

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               customersController.fetchCustomersAsync(true, routeId);
                binding.swipeContainer.setRefreshing(false);
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

        initRecyclerView(routeId,prefs);

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
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
            case R.id.action_logout:
                preferences.setLoggedIn(null);
                startActivity(new Intent(this, LoginActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRefreshFull = true;
        mEventBus.register(this, SubscriberPriority.HIGH);
       // refresh(null);
        customersController.fetchCustomersAsync(true, routeId);
    }

    /*private void refresh(Customer referenceCustomer) {
        if (mPendingRefresh) {
            if (referenceCustomer != null) {
               // mTimestampTracker.updateNext(referenceCustomer.getCreated_at() - 1);
            }
            return;
        }
        if (referenceCustomer != null) {
            //mTimestampTracker.updateCurrent(referenceCustomer.getCreated_at() - 1);
        }
        final long reference;
        final boolean swapList = mRefreshFull;
        mRefreshFull = false;
        if (swapList) {
            reference = 0L;
        } else if (mTimestampTracker.hasTimestamp()) {
            reference = Math.min(mTimestampTracker.getCurrent(),
                    customersAdapter.getReferenceTimestamp());
        } else {
            reference = customersAdapter.getReferenceTimestamp();
        }

        L.d("refreshing with reference time %s", reference);
        new AutoCancelAsyncTask<Void, List<Customer>>(this) {
            @Override
            protected void onResult(List<Customer> customers) {
                L.d("feed model returned with %s items", customers.size());
                if (swapList) {
                    //customersAdapter.swapList(customers);
                } else {
                    //customersAdapter.insertAll(customers);
                }

                customersAdapter.insertAll(customers);
                if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    mLinearLayoutManager.scrollToPosition(0);
                }
                mTimestampTracker.swap();
                mPendingRefresh = false;
                if (mTimestampTracker.hasTimestamp()) {
                    binding.getRoot().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh(null);
                        }
                    }, 1);
                }
            }

            @Override
            protected List<Customer> onDoInBackground(Void... params) {
                L.d("time to query feed model");
                return customerModel.loadCustomers(routeId);
            }
        }.execute();
    }*/

    public void onEventMainThread(FetchedCustomersEvent event) {
        //noinspection NumberEquality
        /*if (event.ismSuccess()) {
           // refresh(event.getOldest());
        } else {
            Snackbar.make(binding.container,
                    "Cannot refresh tasks", Snackbar.LENGTH_SHORT).show();
        }*/
        binding.swipeContainer.setRefreshing(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
        binding.swipeContainer.setRefreshing(false);
        mPendingRefresh = false;
        mTimestampTracker.reset();
    }

    private void initRecyclerView(int routeId, AppPreferences prefs) {
        List<Customer> customerList = customerModel.loadCustomers(routeId);
        customersAdapter = new CustomersAdapter(customerList, context, prefs, new CustomersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Customer customer) {
                Intent bill = new Intent(CustomersActivity.this, BillingActivity.class);
                bill.putExtra("customer",customer.getId());
                startActivity(bill);
            }
        });

        customersAdapter.notifyDataSetChanged();

        binding.customersList.setAdapter(customersAdapter);
        binding.customersList.setHasFixedSize(true);
        binding.customersList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mLinearLayoutManager = (LinearLayoutManager) binding.customersList.getLayoutManager();
    }

}
