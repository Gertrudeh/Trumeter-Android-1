package com.iconasystems.android.trumeter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.vo.BillingPeriod;

import java.util.List;

/**
 * Created by christoandrew on 10/18/16.
 */

public class BillingPeriodsAdapter extends BaseAdapter {
    private Context context;
    private List<BillingPeriod> billingPeriodList;

    public BillingPeriodsAdapter(Context context, List<BillingPeriod> billingPeriodList) {
        this.context = context;
        this.billingPeriodList = billingPeriodList;
    }

    @Override
    public int getCount() {
        return billingPeriodList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.billing_period_spinner_item,parent,false);
        TextView mBillingPeriod = (TextView) convertView.findViewById(R.id.billing_period);
        TextView mBillingPeriodId = (TextView) convertView.findViewById(R.id.billing_period_id);
        TextView mStartDate = (TextView) convertView.findViewById(R.id.start_date);
        TextView mEndDate = (TextView) convertView.findViewById(R.id.end_date);


        BillingPeriod billingPeriod = billingPeriodList.get(position);
        mStartDate.setText(billingPeriod.getStart_date());
        mEndDate.setText(billingPeriod.getEnd_date());
        String period = new StringBuilder().append(billingPeriod.getStart_date())
                .append(" to ").append(billingPeriod.getEnd_date()).toString();
        mBillingPeriod.setText(period);
        mBillingPeriodId.setText(String.valueOf(billingPeriod.getId()));

        return convertView;
    }
}
