package com.iconasystems.android.trumeter.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.controller.ReadingsController;
import com.iconasystems.android.trumeter.model.CustomerModel;
import com.iconasystems.android.trumeter.model.MeterModel;
import com.iconasystems.android.trumeter.model.MeterReadingModel;
import com.iconasystems.android.trumeter.utils.MapUtils;
import com.iconasystems.android.trumeter.vo.Customer;
import com.iconasystems.android.trumeter.vo.Meter;
import com.iconasystems.android.trumeter.vo.Meter$Table;
import com.iconasystems.android.trumeter.vo.MeterReader;
import com.iconasystems.android.trumeter.vo.MeterReading;
import com.iconasystems.android.trumeter.vo.MeterReading$Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BillingActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    @Inject
    ReadingsController readingsController;

    @Inject
    MeterModel meterModel;

    @Inject
    MeterReadingModel meterReadingModel;

    @Inject
    EventBus mEventBus;

    @Inject
    CustomerModel customerModel;

    @Inject
    Context context;

    boolean isLess;
    public String expected_range;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView mMeterPicture;
    private Button mTakePicture;
    private Spinner spinner;
    private TextView mCustomerName;
    private TextView mAddress;
    private EditText mCurrentReading;
    private TextView mMeterNumber;
    private FloatingActionButton mBill;
    private TextView mBillingPeriod;
    private int meterId;
    String reason;
    private float currentReading = 0;
    private float previousReading = 0;
    private float readingLatitude = 0;
    private float readingLongitude = 0;
    private float meterLatitude = 0;
    private float meterLongitude = 0;
    private float distance = 0;
    private float quantity = 0;
    private int meterReaderId;
    private int customerId;
    private int readingCode;
    private String meterPhoto = null;
    private Meter meter = null;
    private MeterReading lastReading = null;
    private int billingPeriod;
    private AppPreferences prefs;
    private int billingPeriodId;
    private ProgressDialog progressDialog;
    private TextView mBillingGroup;
    private boolean isRegularReading;
    @Nullable
    private float lastReadingQuantity;
    private EditText mReason;
    private float minus10percent;
    private float plus10percent;
    private SwitchCompat isMetered;
    private String isMeteredEntry;
    private RelativeLayout container;
    private float lastQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_bill);
        final ApiService apiService = ApiModule.getClient().create(ApiService.class);
        mCustomerName = (TextView) findViewById(R.id.customer_bill_name);
        mAddress = (TextView) findViewById(R.id.customer_bill_address);
        mBill = (FloatingActionButton) findViewById(R.id.fab_bill);
        mCurrentReading = (EditText) findViewById(R.id.meter_reading);
        mMeterNumber = (TextView) findViewById(R.id.meter_number);
        mBillingGroup = (TextView) findViewById(R.id.posting_group);
        isMetered = (SwitchCompat) findViewById(R.id.is_metered);
        isMetered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMeteredEntry = "Yes";
                } else {
                    isMeteredEntry = "No";
                }
            }
        });
        mReason = (EditText) findViewById(R.id.reason);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving meter reading");
        progressDialog.setCancelable(true);


        prefs = AppPreferences.get(this);
        final MeterReader reader = prefs.getUser();
        billingPeriodId = prefs.getBillingPeriod().getId();

        Location location = MapUtils.getLocation(this, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, this);

        readingLatitude = (float) location.getLatitude();
        readingLongitude = (float) location.getLongitude();
        int customerId = getIntent().getIntExtra("customer", 0);

        final Customer customer = customerModel.load(customerId);

        MeterReading lastReading = new Select().from(MeterReading.class)
                .where(Condition.column(MeterReading$Table.METER_ID).eq(customer.getMeter_id()))
                .orderBy("created_at DESC")
                .limit(1)
                .querySingle();
//        Log.d("Last reading", lastReading.toString());

        mCustomerName.setText(customer.getName());
        mAddress.setText(customer.getAddress());
        mMeterNumber.setText(customer.getMeter_number());
        mBillingGroup.setText(customer.getPosting_group());

        Log.d("customer", customer.toString());

        spinner = (Spinner) findViewById(R.id.billing_code);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.billing_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        readingCode = 1;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.VISIBLE);

                        break;
                    case 1:
                        readingCode = 4;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        readingCode = 7;
                        mCurrentReading.setVisibility(View.VISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        readingCode = 8;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        readingCode = 10;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        readingCode = 11;
                        mCurrentReading.setVisibility(View.VISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 6:
                        readingCode = 13;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        readingCode = 15;
                        mCurrentReading.setVisibility(View.VISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 8:
                        readingCode = 17;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 9:
                        readingCode = 18;
                        mCurrentReading.setVisibility(View.VISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 10:
                        readingCode = 19;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 11:
                        readingCode = 5;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        readingCode = 0;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMeterPicture = (ImageView) this.findViewById(R.id.meter_picture);
        mTakePicture = (Button) this.findViewById(R.id.photo_button);

        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        mBill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                //progressDialog.show();
                Meter meter = new Select().from(Meter.class)
                        .where(Condition.column(Meter$Table.CUSTOMER_ID).eq(customer.getId()))
                        .querySingle();
                meterId = customer.getMeter_id();
                meterLatitude = 0;
                meterLongitude = 0;

                float[] results = new float[1];
                ///Location.distanceBetween(meterLatitude, meterLongitude, readingLatitude, readingLongitude, results);
                // REVERT THIS BACK
                distance = results[0];
                //distance = 0;
                meterReaderId = reader.getId();


                MeterReading lastReading = new Select().from(MeterReading.class)
                        .where(Condition.column(MeterReading$Table.METER_ID).eq(customer.getMeter_id()))
                        .orderBy("created_at DESC")
                        .limit(1)
                        .querySingle();
                if (lastReading == null) {
                    lastReadingQuantity = 0;
                    previousReading = 0;
                } else {
                    lastReadingQuantity = lastReading.getQuantity();
                    Log.d("Meter Reading Response", lastReading.toString());
                    previousReading = lastReading.getCurrent_reading();

                }
                String currentReadingRead = mCurrentReading.getText().toString();
                if (!TextUtils.isEmpty(currentReadingRead)) {
                    currentReading = Float.parseFloat(mCurrentReading.getText().toString());
                    if (currentReading < previousReading) {
                        Snackbar.make(v, "Current reading is less than previous reading!",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else if (TextUtils.isEmpty(currentReadingRead)) {
                    currentReading = previousReading;
                }

                Log.d("Previous Reading", "= " + previousReading);
                if (currentReading > previousReading) {
                    quantity = currentReading - previousReading;
                    plus10percent = (float) (lastReadingQuantity + (0.1 * lastReadingQuantity));
                    minus10percent = (float) (lastReadingQuantity - (0.1 * lastReadingQuantity));
                    isLess = false;
                    Log.d("Range", " " + plus10percent + " " + minus10percent);

                    if ((quantity > plus10percent) || (quantity < minus10percent)) {
                        isRegularReading = false;
                    } else {
                        isRegularReading = true;
                    }
                } else {
                    quantity = 0;
                    isRegularReading = true;
                    if(spinner.getSelectedItemPosition() != 2 ){
                        isLess = false;
                    }else {
                        isLess = true;
                    }

                }

                if (mReason.getVisibility() == View.VISIBLE) {
                    reason = mReason.getText().toString();
                }

                expected_range = String.valueOf(minus10percent).concat(" to ").concat(String.valueOf(plus10percent));

                MeterReading newReading = new MeterReading();
                newReading.setMeter_id(meterId);
                newReading.setCurrent_reading(currentReading);
                newReading.setPrevious_reading(previousReading);
                newReading.setPhoto(meterPhoto);
                newReading.setReading_code(readingCode);
                newReading.setLatitude(readingLatitude);
                newReading.setLongitude(readingLongitude);
                newReading.setMeter_reader_id(meterReaderId);
                newReading.setDistance(distance);
                newReading.setQuantity(quantity);
                newReading.setBilling_period_id(billingPeriodId);
                newReading.setRegular(isRegularReading);
                newReading.setReason(reason);
                newReading.setPosted("No");
                newReading.setCustomer_details("Correct");
                newReading.setPrevious_consumption(lastReadingQuantity);
                newReading.setExpected_range(expected_range);
                newReading.setIs_metered_entry(isMeteredEntry);

                Log.d("Meter Reading", newReading.toString());
                if (isRegularReading && !isLess) {
                    sendPost();
                } else if (isLess) {
                    showSnackbar("Current Reading is less than previous", "", v);
                } else if (!isRegularReading) {
                    showSnackbar("Reading is irregular", "Continue", v);
                }


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mMeterPicture.setImageBitmap(photo);
            meterPhoto = encodeTobase64(photo);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // mEventBus.register(this, SubscriberPriority.HIGH);
    }

    public String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void showSnackbar(String message, String action, View v) {
        final Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
        if (message.equals("Current Reading is less than previous")) {

        } else {
            snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPost();
                    snackbar.dismiss();
                }
            });
        }

        snackbar.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                readingCode = 1;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.VISIBLE);

                break;
            case 1:
                readingCode = 4;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 2:
                readingCode = 7;
                mCurrentReading.setVisibility(View.VISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 3:
                readingCode = 8;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 4:
                readingCode = 10;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 5:
                readingCode = 11;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 6:
                readingCode = 13;
                mCurrentReading.setVisibility(View.INVISIBLE);
                break;
            case 7:
                readingCode = 15;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 8:
                readingCode = 17;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 9:
                readingCode = 18;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            case 10:
                readingCode = 19;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;
            default:
                readingCode = 0;
                mCurrentReading.setVisibility(View.INVISIBLE);
                mReason.setVisibility(View.INVISIBLE);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static Intent intentForSendPost(Context context) {
        return new Intent(context, BillingActivity.class);
    }

    private void sendPost() {
        readingsController.saveReadingAsync(meterId, currentReading, "No",
                previousReading, expected_range, isRegularReading, billingPeriodId, meterPhoto, readingLongitude,
                lastReadingQuantity, quantity, distance, meterReaderId, readingLatitude,
                readingCode, reason, UUID.randomUUID().toString(), lastReadingQuantity);
        startActivity(new Intent(context, TasksActivity.class));
    }
}
