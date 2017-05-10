package com.iconasystems.android.trumeter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.vo.Issue;
import com.iconasystems.android.trumeter.vo.MeterReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Report extends AppCompatActivity {
    private EditText mTitle;
    private EditText mDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = (EditText) findViewById(R.id.title_issue);
        mDetails = (EditText) findViewById(R.id.issue_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AppPreferences preferences = AppPreferences.get(getApplicationContext());
                MeterReader reader = preferences.getUser();
                String title = mTitle.getText().toString();
                String details = mDetails.getText().toString();
                int meter_reader_id = reader.getId();

                Issue issue = new Issue();
                issue.setMeter_reader_id(meter_reader_id);
                issue.setText(details);
                issue.setTitle(title);
                ApiService apiService = ApiModule.getClient().create(ApiService.class);
                Call<Issue> issueCall = apiService.reportIssue(issue);
                issueCall.enqueue(new Callback<Issue>() {
                    @Override
                    public void onResponse(Call<Issue> call, Response<Issue> response) {
                        Snackbar.make(view, "Issue posted", Snackbar.LENGTH_LONG)
                                .show();

                    }

                    @Override
                    public void onFailure(Call<Issue> call, Throwable t) {
                        Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                    }

                });

            }
        });
    }

}
