
package com.iconasystems.android.trumeter.di.component;
import com.iconasystems.android.trumeter.SearchActivity;
import com.iconasystems.android.trumeter.di.scope.ActivityScope;
import com.iconasystems.android.trumeter.view.activity.BillingActivity;
import com.iconasystems.android.trumeter.view.activity.BillingPeriodsActivity;
import com.iconasystems.android.trumeter.view.activity.CustomersActivity;
import com.iconasystems.android.trumeter.view.activity.LoginActivity;
import com.iconasystems.android.trumeter.view.activity.RoutesActivity;
import com.iconasystems.android.trumeter.view.activity.SelectBillingPeriodActivity;
import com.iconasystems.android.trumeter.view.activity.TasksActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {

    void inject(TasksActivity tasksActivity);

    void inject(RoutesActivity routesActivity);

    void inject(BillingActivity billingActivity);

    void inject(CustomersActivity customersActivity);

    void inject(BillingPeriodsActivity billingPeriodsActivity);

    void inject(SelectBillingPeriodActivity selectBillingPeriodActivity);

    void inject(LoginActivity loginActivity);

    void inject(SearchActivity searchActivity);
}
