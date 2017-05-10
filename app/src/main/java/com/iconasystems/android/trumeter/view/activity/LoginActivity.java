package com.iconasystems.android.trumeter.view.activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.controller.MetersController;
import com.iconasystems.android.trumeter.utils.GPSTracker;
import com.iconasystems.android.trumeter.vo.MeterReader;
import com.iconasystems.android.trumeter.vo.MeterReader$Table;
import com.iconasystems.android.trumeter.vo.Session;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button mLoginButton;
    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mProgress;
    private AppPreferences preferences;

    @Inject
    MetersController metersController;
    private GPSTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = AppPreferences.get(this);

        Log.d("Current reader", preferences.getUser().toString());

        mUsername = (EditText) findViewById(R.id.account_name);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) this.findViewById(R.id.login_button);
        mProgress = (ProgressBar) findViewById(R.id.login_progress);
        final RelativeLayout scrollingView = (RelativeLayout) findViewById(R.id.login_content);

        tracker = new GPSTracker(this,this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mProgress.setVisibility(View.VISIBLE);
                scrollingView.setVisibility(View.GONE);
                ApiService apiService = ApiModule.getClient().create(ApiService.class);
                Session session = new Session();
                session.setUsername(mUsername.getText().toString());
                session.setPassword(mPassword.getText().toString());

                MeterReader existing = new Select().from(MeterReader.class)
                        .where(Condition.column(MeterReader$Table.USERNAME).eq(session.getUsername()))
                        .querySingle();
                if (existing != null){
                    Log.d("Reader", existing.toString());
                    preferences.setLoggedIn(existing);
                    startActivity(new Intent(getApplicationContext(), BillingPeriodsActivity.class));
                    scrollingView.setVisibility(View.VISIBLE);
                }else {
                    Call<MeterReader> loginSessionCall = apiService.authenticateUser(mUsername.getText().toString(),
                            mPassword.getText().toString());
                    loginSessionCall.enqueue(new Callback<MeterReader>() {
                        @Override
                        public void onResponse(Call<MeterReader> call, Response<MeterReader> response) {

                            if (response.body() != null) {
                                mProgress.setVisibility(View.GONE);

                                MeterReader reader = response.body();
                                reader.save();
                                Log.d("Reader", reader.toString());
                                preferences.setLoggedIn(reader);
                                startActivity(new Intent(getApplicationContext(), BillingPeriodsActivity.class));
                                scrollingView.setVisibility(View.VISIBLE);

                                AppPreferences preferences = AppPreferences.get(getApplicationContext());
                                preferences.clear();
                            } else {
                                Snackbar.make(v, "Invalid Login", Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MeterReader> call, Throwable t) {
                            Log.e("Error logging in", t.getMessage());
                            Snackbar.make(v, "Network Error", Snackbar.LENGTH_LONG)
                                    .show();
                            mProgress.setVisibility(View.GONE);
                            scrollingView.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            tracker.showSettingsAlert();
        }
        Log.d("Is location enabled", ""+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        /*if (preferences.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), BillingPeriodsActivity.class));
        }*/
    }


}
