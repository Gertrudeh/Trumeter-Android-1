package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.vo.BillingPeriod;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.Issue;
import com.iconasystems.android.trumeter.vo.Meter;
import com.iconasystems.android.trumeter.vo.MeterReader;
import com.iconasystems.android.trumeter.vo.MeterReading;
import com.iconasystems.android.trumeter.vo.Route;
import com.iconasystems.android.trumeter.vo.Task;
import com.iconasystems.android.trumeter.vo.Town;
import com.iconasystems.android.trumeter.vo.TownZone;
import com.iconasystems.android.trumeter.vo.Zone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by christoandrew on 10/7/16.
 */

public interface ApiService {
    @GET("admin/towns.json")
    Call<List<Town>> getTowns();

    @GET("admin/town/zones.json")
    Call<List<TownZone>> getTownZones();

    @GET("admin/zones/{route_id}/customers.json")
    Call<List<Customer>> getCustomers(@Path("route_id") int zoneId);

    @GET("admin/meter/{customer_id}.json")
    Call<Meter> getMeter(@Path("customer_id") int customer_id);

    @GET("admin/meters/{route_id}.json")
    Call<MeterResponse> meters(@Path("route_id") int route_id);

    @GET("admin/meter_reading/{meter_id}.json")
    Call<MeterReading> getLastReading(@Path("meter_id") int meter_id);

    @GET("admin/billing_periods.json")
    Call<List<BillingPeriod>> getBillingPeriods();

    @GET("admin/towns/{town_id}/zones.json")
    Call<List<Zone>> getZones(@Path("town_id") int townId);

    @GET("admin/billing_period/{billing_period_id}.json")
    Call<BillingPeriod> getBillingPeriod(@Path("billing_period_id") int billingPeriodId);

    @GET("admin/zones/{zone_id}/routes.json")
    Call<List<Route>> getRoutes(@Path("zone_id") int zone_id);

    @GET("admin/reader_tasks/{meter_reader_id}.json")
    Call<List<Task>> getTasks(@Path("meter_reader_id") int meter_reader_id);

    @GET("admin/reader_tasks/{meter_reader_id}.json")
    Call<TaskResponse> tasks(@Path("meter_reader_id") int meter_reader_id);

    @POST("admin/meter_readers/auth.json")
    Call<MeterReader> authenticateUser(@Query("username") String username, @Query("password") String password);

    @POST("admin/new_reading.json")
    Call<MeterReading> saveReading(@Body MeterReading meterReading);

    @GET("admin/readings.json")
    Call<List<MeterReading>> readings();

    @POST("admin/new_reading.json")
    Call<MeterReadingResponse> postReading(@Query("meter_id") int meter_id, @Query("current_reading")
                                           float current_reading, @Query("previous_reading") float previous_reading,
                                           @Query("photo") String photo, @Query("reading_code") int reading_code,
                                           @Query("latitude") float latitude, @Query("longitude") float longitude,
                                           @Query("meter_reader_id") int meter_reader_id, @Query("distance") float distance,
                                           @Query("quantity") float quantity, @Query("billing_period_id") int billing_period_id,
                                           @Query("regular") String regular, @Query("reason") String reason, @Query("customer_details")
                                           String customer_details, @Query("posted") String posted,
                                           @Query("previous_consumption") float previous_consumption, @Query("expected_range") String
                                           expected_range, @Query("is_metered_entry") String is_metered_entry);

    @POST("admin/issues.json")
    Call<Issue> reportIssue(@Body Issue issue);

    @GET("admin/zones/{route_id}/customers.json")
    Call<CustomerResponse> customers(@Path("route_id") int routeId);

    @GET("admin/meters.json")
    Call<MeterResponse> getMeters();

    @GET("admin/meters.json")
    Call<List<Meter>> meters();


}
