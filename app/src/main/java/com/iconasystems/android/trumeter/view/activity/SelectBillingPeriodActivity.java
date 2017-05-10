package com.iconasystems.android.trumeter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iconasystems.android.trumeter.About;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.Report;
import com.iconasystems.android.trumeter.controller.BillingPeriodsController;

import javax.inject.Inject;

/**
 * Created by christoandrew on 12/13/16.
 */

public class SelectBillingPeriodActivity  extends BaseActivity {
    @Inject
    BillingPeriodsController billingPeriodsController;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_billing_period);


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

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        billingPeriodsController.fetchBillingPeriodsAsync(false);
    }
}
