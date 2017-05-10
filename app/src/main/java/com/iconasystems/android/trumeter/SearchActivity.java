package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.iconasystems.android.trumeter.adapters.CustomersAdapter;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;
import com.iconasystems.android.trumeter.view.activity.BaseActivity;
import com.iconasystems.android.trumeter.view.activity.BillingActivity;
import com.iconasystems.android.trumeter.vo.Customer;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private MaterialSearchView searchView;
    private RecyclerView customerRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getComponent().inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AppPreferences prefs = AppPreferences.get(this);
        toolbar.setTitle("Search Customers");

        customerRecycler = (RecyclerView) findViewById(R.id.customers_list_view);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StringQuery stringQuery = new StringQuery(Customer.class, "select * from customer where name like '%" + query + "%'");
                List<Customer> customersList = stringQuery.queryList();
                final CustomersAdapter customersAdapter = new CustomersAdapter(customersList, getApplicationContext(), prefs, new CustomersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Customer customer) {
                        Intent bill = new Intent(SearchActivity.this, BillingActivity.class);
                        bill.putExtra("customer", customer.getId());
                        startActivity(bill);
                    }
                });
                customerRecycler.setAdapter(customersAdapter);
                customerRecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                        DividerItemDecoration.VERTICAL_LIST));


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}
