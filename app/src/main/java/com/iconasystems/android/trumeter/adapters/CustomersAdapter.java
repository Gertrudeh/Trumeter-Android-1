package com.iconasystems.android.trumeter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.utils.InterfaceUtils;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.MeterReading;
import com.iconasystems.android.trumeter.vo.MeterReading$Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

/**
 * Created by christoandrew on 10/7/16.
 */

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {
    private List<Customer> customerList;
    private Context _context;
    private OnItemClickListener onItemClickListener;
    private AppPreferences preferences;

    public CustomersAdapter(List<Customer> customerList, Context _context, AppPreferences preferences,
                            OnItemClickListener onItemClickListener) {
        this.customerList = customerList;
        this._context = _context;
        this.onItemClickListener = onItemClickListener;
        this.preferences = preferences;
    }

    @Override
    public CustomersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_item,
                parent, false);
        return new CustomersAdapter.ViewHolder(v, preferences, _context);
    }

    @Override
    public void onBindViewHolder(CustomersAdapter.ViewHolder holder, int position) {
        holder.bind(customerList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCustomerName;
        public TextView mCustomerAddress;
        public View itemView;
        public TextView mCustomerId;
        public TextView mCustomerRoute;
        public TextView mCustomerRouteId;
        public TextView mCustomerZoneName;
        public ImageView mReadingIndicator;
        public ImageView redIndicator;
        public ImageView darkBlueIndicator;
        public ImageView lightBlueIndicator;
        public ImageView blackIndicator;
        public ImageView pinkIndicator;
        public ImageView defaultIndicator;
        public AppPreferences prefs;
        public Context context;
        private MeterReading lastReading;
        private TextView mPostingGroup;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View itemView, AppPreferences prefs, Context context) {
            super(itemView);
            InterfaceUtils.resetUI(itemView);
            this.prefs = prefs;
            this.itemView = itemView;
            this.context = context;
            mCustomerName = (TextView) itemView.findViewById(R.id.customer_name);
            mCustomerAddress = (TextView) itemView.findViewById(R.id.customer_address);
            mCustomerId = (TextView) itemView.findViewById(R.id.cust_id);
            mCustomerRoute = (TextView) itemView.findViewById(R.id.route_name);
            mCustomerRouteId = (TextView) itemView.findViewById(R.id.route_id);
            mCustomerZoneName = (TextView) itemView.findViewById(R.id.zone_name);
            mPostingGroup = (TextView) itemView.findViewById(R.id.posting_group);
            //mReadingIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator);
            redIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_red);
            darkBlueIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_dark_blue);
            lightBlueIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_light_blue);
            blackIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_black);
            pinkIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_pink);
            defaultIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_default);


        }

        public void bind(final Customer customer, final OnItemClickListener onItemClickListener) {
            mCustomerName.setText(customer.getName());
            mCustomerAddress.setText(customer.getAddress());
            mCustomerId.setText(String.valueOf(customer.getId()));
            mCustomerRoute.setText(customer.getMeter_number());
            mCustomerRouteId.setText(String.valueOf(customer.getId()));
            mPostingGroup.setText(customer.getPosting_group());

            try {

                MeterReading existing = new Select().from(MeterReading.class)
                        .where(Condition.column(MeterReading$Table.METER_ID).eq(customer.getMeter_id()),
                                Condition.column(MeterReading$Table.BILLING_PERIOD_ID).eq(prefs.getBillingPeriod().getId()))
                        .querySingle();
                if (existing != null) {
                    int readingCode = existing.getReading_code();

                    Log.d("Existing", "True");
                    if (readingCode == 1) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 4) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 7) {
                        darkBlueIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                        itemView.setClickable(false);
                    } else if (readingCode == 8) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 10) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 11) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 13) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 15) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 17) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 18) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 19) {
                        redIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    } else if (readingCode == 5) {
                        darkBlueIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                        itemView.setClickable(false);
                    }
                    else {
                        blackIndicator.setVisibility(View.VISIBLE);
                        defaultIndicator.setVisibility(View.GONE);
                    }

                } else {
                    Log.d("Existing", "False");
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(customer);
                        }
                    });
                }

                Log.d("redIndicator Visible", " " + redIndicator.getVisibility());
                Log.d("default Visible", " " + defaultIndicator.getVisibility());
                Log.d("black Visible", " " + blackIndicator.getVisibility());
                Log.d("darkBlue Visible", " " + darkBlueIndicator.getVisibility());


            } catch (NullPointerException e) {
                blackIndicator.setVisibility(View.VISIBLE);
                defaultIndicator.setVisibility(View.GONE);
            }


        }

    }

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }

    private void resetUI() {

    }
}
