package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.vo.Customer;

import java.util.List;

/**
 * Created by christoandrew on 11/28/16.
 */

public class CustomerResponse {
    private List<Customer> customerList;

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
