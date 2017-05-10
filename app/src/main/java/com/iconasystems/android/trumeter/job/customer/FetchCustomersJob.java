package com.iconasystems.android.trumeter.job.customer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.iconasystems.android.trumeter.Config;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.api.CustomerResponse;
import com.iconasystems.android.trumeter.di.component.AppComponent;
import com.iconasystems.android.trumeter.event.customer.FetchedCustomersEvent;
import com.iconasystems.android.trumeter.job.BaseJob;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.vo.Customer;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;

/**
 * Created by christoandrew on 12/4/16.
 */

public class FetchCustomersJob extends BaseJob {

    private static final String GROUP = "FetchCustomersJob";
    private int routeId;

    @Inject
    transient EventBus eventBus;
    @Inject
    transient Config config;
    @Inject
    transient CustomerModel customerModel;
    @Inject
    ApiService apiService;
    private long timestamp = 0;

    public FetchCustomersJob(@Priority int priority, @NonNull int routeId) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
        this.routeId = routeId;
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

        final Call<List<Customer>> customers = apiService.getCustomers(routeId);
        List<Customer> customerList = customers.execute().body();

        doResponse(customerList);

        //Response<CustomerResponse> response = customers.execute();


        /*if (response.isSuccessful()){
            Log.d("http response",response.isSuccessful()+"");
           // Customer oldest = handleResponse(response.body());
           // eventBus.post(new FetchedCustomersEvent(true,routeId,oldest));

            Log.d("Customer 1",response.body().getCustomerList().get(0).toString());
        }else {

        }*/
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        eventBus.post(new FetchedCustomersEvent(false, routeId, null));
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
    private Customer handleResponse(CustomerResponse body) {
        // We could put these two into a transaction but it is OK to save a user even if we could
        // not save their post so we don't care.

        Log.d("Response body", body.getCustomerList().size()+" ");
        Customer oldest = null;
        if (body.getCustomerList() != null) {
            customerModel.saveAll(body.getCustomerList());
            long since = 0;
            /*for (Customer customer : body.getCustomerList()) {
                if (customer.getCreated_at() > since) {
                    since = customer.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > customer.getCreated_at()) {
                    oldest = customer;
                }
            }*/
            if (since > 0) {
                // taskModel.saveTaskTimestamp(since,mMeterReaderId);
                //customerModel.saveTimestamp(since,);
            }
        }
        return oldest;
    }

    private void doResponse(List<Customer> customers){
        Log.d("Response body", customers.size()+" ");
        Customer oldest = null;
        if (customers != null) {
            customerModel.saveAll(customers);
            long since = 0;
            /*for (Customer customer :customers) {
                if (customer.getCreated_at() > since) {
                    since = customer.getCreated_at();
                }
                if (oldest == null || oldest.getCreated_at() > customer.getCreated_at()) {
                    oldest = customer;
                }
            }
            if (since > 0) {
                // taskModel.saveTaskTimestamp(since,mMeterReaderId);
                //customerModel.saveTimestamp(since,);
            }*/
        }
    }
}
