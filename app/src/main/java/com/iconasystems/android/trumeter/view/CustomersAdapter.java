package com.iconasystems.android.trumeter.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.databinding.CustomerListItemBinding;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.MeterReading;
import com.iconasystems.android.trumeter.vo.MeterReading$Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by christoandrew on 11/28/16.
 */

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomerItemViewHolder> {
    final LayoutInflater mLayoutInflater;

    final SortedList<Customer> mList;

    final Map<String, Customer> mUniqueMapping = new HashMap<>();

    private Callback mCallback;
    private MeterReading lastReading;

    AppPreferences prefs;

    public CustomersAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        prefs = AppPreferences.get(context);
        mList = new SortedList<>(Customer.class,
                new SortedListAdapterCallback<Customer>(this) {
                    @Override
                    public int compare(Customer cs1, Customer cs2) {
                        return 0;
                    }

                    @SuppressWarnings("SimplifiableIfStatement")
                    @Override
                    public boolean areContentsTheSame(Customer oldCustomer,
                                                      Customer newCustomer) {

                        return oldCustomer.getName().equals(newCustomer.getName());
                    }

                    @Override
                    public boolean areItemsTheSame(Customer task1, Customer task2) {
                        return task1.getId() == task2.getId();
                    }
                });
    }

    @Override
    public CustomersAdapter.CustomerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final CustomerListItemBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                R.layout.customer_list_item, parent, false);
        CustomerItemViewHolder holder = new CustomerItemViewHolder(binding);
        Customer customer = binding.getModel();


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback == null) {
                    return;
                }
                Customer customer = binding.getModel();
                mCallback.onCustomerClick(customer);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomersAdapter.CustomerItemViewHolder holder, int position) {
        Log.d("Customers Adapter", mList.get(position).toString());
        holder.binding.setModel(mList.get(position));
        holder.binding.executePendingBindings();
        Customer customer = mList.get(position);

        final ImageView redIndicator = holder.binding.readingIndicatorRed;
        final ImageView defaultIndicator = holder.binding.readingIndicatorRed;
        final ImageView darkBlueIndicator = holder.binding.readingIndicatorDarkBlue;
        final ImageView blackIndicator = holder.binding.readingIndicatorBlack;

        try {
            MeterReading lastReading = new Select().from(MeterReading.class)
                    .where(Condition.column(MeterReading$Table.METER_ID).eq(customer.getMeter_id()))
                    .orderBy("created_at DESC")
                    .limit(1)
                    .querySingle();
            if(lastReading != null){
                int readingCode = lastReading.getReading_code();

                if (readingCode == 1) {
                    redIndicator.setVisibility(View.VISIBLE);
                    defaultIndicator.setVisibility(View.GONE);
                } else if (readingCode == 4) {
                    redIndicator.setVisibility(View.VISIBLE);
                    defaultIndicator.setVisibility(View.GONE);
                } else if (readingCode == 7) {
                    darkBlueIndicator.setVisibility(View.VISIBLE);
                    defaultIndicator.setVisibility(View.GONE);
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
                }else{
                    blackIndicator.setVisibility(View.VISIBLE);
                    defaultIndicator.setVisibility(View.GONE);
                }
            }
            else {
                blackIndicator.setVisibility(View.VISIBLE);
                defaultIndicator.setVisibility(View.GONE);
            }

        } catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CustomerItemViewHolder extends RecyclerView.ViewHolder {
        private final CustomerListItemBinding binding;

        public CustomerItemViewHolder(CustomerListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onCustomerClick(Customer customer);
    }

    public long getReferenceTimestamp() {
        int size = mList.size();
        if (size == 0) {
            return 0;
        }
        return mList.get(0).getId();
    }

    public void insert(Customer customer) {
        String key = createKeyFor(customer);
        Customer existing = mUniqueMapping.put(key, customer);
        if (existing == null) {
            mList.add(customer);
        } else {
            int pos = mList.indexOf(existing);
            mList.updateItemAt(pos, customer);
        }
    }

    public void insertAll(List<Customer> items) {
        for (Customer item : items) {
            insert(item);
        }
    }

    public void swapList(List<Customer> customers) {
        Set<String> newListKeys = new HashSet<>();
        for (Customer item : customers) {
            newListKeys.add(createKeyFor(item));
        }
        for (int i = mList.size() - 1; i >= 0; i--) {
            Customer item = mList.get(i);
            String key = createKeyFor(item);
            if (!newListKeys.contains(key)) {
                mUniqueMapping.remove(key);
                mList.removeItemAt(i);
            }
        }
        insertAll(customers);
    }

    public void remove(Customer customer) {
        Customer model = mUniqueMapping.remove(createKeyFor(customer));
        if (model != null) {
            mList.remove(model);
        }
    }

    public void clear() {
        mList.clear();
        mUniqueMapping.clear();
    }

    private static String createKeyFor(Customer customer) {
        return customer.compositeUniqueKey();
    }
}
