package com.iconasystems.android.trumeter.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.adapters.BillingPeriodsAdapter;
import com.iconasystems.android.trumeter.controller.BillingPeriodsController;
import com.iconasystems.android.trumeter.controller.MetersController;
import com.iconasystems.android.trumeter.controller.ReadingsController;
import com.iconasystems.android.trumeter.model.BillingPeriodModel;
import com.iconasystems.android.trumeter.vo.BillingPeriod;

import javax.inject.Inject;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class BillingPeriodsActivity extends BaseActivity {
    private Button mFinish;
    private Spinner mBillingPeriod;
    private int billingPeriodId;
    private String endDate;
    private String startDate;
    private AppPreferences preferences;

    @Inject
    Context context;
    @Inject
    BillingPeriodsController billingPeriodsController;
    @Inject
    BillingPeriodModel billingPeriodModel;
    @Inject
    MetersController metersController;
    @Inject
    ReadingsController readingsController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_select_billing_period);
        mFinish = (Button) findViewById(R.id.finish_select_bp);
        mBillingPeriod = (Spinner) findViewById(R.id.billing_period);
        //mBillingPeriod.setPopupBackgroundResource(android.R.color.white);

        preferences = AppPreferences.get(this);

        BillingPeriodsAdapter bpAdapter = new BillingPeriodsAdapter(context,billingPeriodModel.load());
        bpAdapter.notifyDataSetChanged();
        mBillingPeriod.setAdapter(bpAdapter);

        mBillingPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView idTextView = (TextView) view.findViewById(R.id.billing_period_id);
                TextView startDateTextView = (TextView) view.findViewById(R.id.start_date);
                TextView endDateTextView = (TextView) view.findViewById(R.id.end_date);
                billingPeriodId = Integer.parseInt(idTextView.getText().toString());
                startDate = startDateTextView.getText().toString();
                endDate = endDateTextView.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingPeriod billingPeriod = new BillingPeriod();
                billingPeriod.setStart_date(startDate);
                billingPeriod.setEnd_date(endDate);
                billingPeriod.setId(billingPeriodId);

                preferences.setBillingPeriod(billingPeriod);
                startActivity(new Intent(BillingPeriodsActivity.this, TasksActivity.class));
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        readingsController.fetchReadingsAsync(false);
        metersController.fetchMetersAsync(false);
        billingPeriodsController.fetchBillingPeriodsAsync(false);
    }
}
