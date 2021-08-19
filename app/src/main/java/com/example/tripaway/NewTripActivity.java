package com.example.tripaway;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.tripaway.models.UpcomingTripModel;
import com.example.tripaway.utils.FireStoreHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewTripActivity extends AppCompatActivity  {
    Button btnFindLocation;
    LocationManager manager;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST_SEND_SMS =2 ;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private FusedLocationProviderClient mFusedLocationClient;
    TextView txtLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Button btnGetAddress;
    TextView txtAddress;
    Button btnSMS;
    String phoneNo;
    String message;
    List<Address> addresses;
    public static final String lat = "lat";
    public static final String lon = "lon";
    public static final String add = "add";


    EditText tripTitle,startPoint,endPoint,txtTimePicker,txtDatePicker,
    txtDatePicker2, txtTimePicker2;
    Button btnCurrentLocation,btnAddTrip;
    String[] direction = {"ONE_WAY", "ROUND"};
    String[] repeat = {"NO_REPEAT", "DAILY","WEAKLY","MONTHLY"};
    TextView j_spinner_selected_direction,j_spinner_selected_repeat;
    Spinner j_spinner_direction,j_spinner_repeat;
    FirebaseFirestore dbFireStore;
    FirebaseAuth mAuth;
    UpcomingTripModel upcomingTripModel;

    int repeatedAlarm = 0;
    boolean isOneDirection = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        String apiKey = getString(R.string.api_key);

        Places.initialize(getApplicationContext(),apiKey);
        tripTitle = (EditText) findViewById(R.id.edtTitleNew);
        tripTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
        startPoint =  findViewById(R.id.edtStartPoint);
        startPoint.setFocusable(false);
        FindCurrentLocation();

        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace(1);
            }

        });
        endPoint = (EditText) findViewById(R.id.edtEndPoint);
        endPoint.setFocusable(false);
        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace(2);
            }

        });
        btnCurrentLocation = (Button) findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCurrentAddress();
            }

        });
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnAddTrip = (Button) findViewById(R.id.btnAddTrip);
        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               //RoundTripHelper roundTripHelper = new RoundTripHelper();
//
//               ArrayList<Timestamp> timestampsList =
//                       new ArrayList<Timestamp> ();
//                timestampsList.add(new Timestamp(System.currentTimeMillis()));
//                timestampsList.add(new Timestamp(System.currentTimeMillis()));


                //TODO: validation

                if(validation())
                {
                    if(isOneDirection)
                        saveDataInFireStore();

                    if(!isOneDirection)
                        saveRoundTripToFireStore();


                    NewTripActivity.this.finish();
                }
                else
                {

//                    UpcomingTripModel round = new UpcomingTripModel(
//                            (List<String>) Arrays.asList("round first","round second"),
//                            (List<String>)Arrays.asList("startPoint1","startPoint2"),
//                            (List<String>)Arrays.asList("endPoint1","endPoint2"),
//                            (List<String>)Arrays.asList("round date","round date"),
//                            (List<String>)Arrays.asList("round time","time"),
//                            false,
//                            1,
//                            null,
//                            0,
//                            0,
//                            new Timestamp(System.currentTimeMillis())
//                    );
//
//                    dbFireStore.collection("users")
//                            .document(mAuth.getUid())
//                            .collection("upcoming")
//                            .add(round.getRoundTripsMap())
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.i("WEAAM", "Round added to firstore successfully");
//                                }
//                            });
//
//                    NewTripActivity.this.finish();
                }

               // saveOLDDataToFireStore();


             //   startActivity(new Intent(NewTripActivity.this, UpcomingTripsActivity.class));



            }

        });
        txtTimePicker = (EditText) findViewById(R.id.timePicker);
        txtTimePicker.setFocusable(false);
        txtTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTime(1);
            }
        });
        txtTimePicker2 = (EditText) findViewById(R.id.timePicker3);
        txtTimePicker2.setFocusable(false);
        txtTimePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTime(2);
            }
        });
        txtDatePicker = (EditText) findViewById(R.id.datePicker);
        txtDatePicker.setFocusable(false);
        txtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDate(1);
            }

        });
        txtDatePicker2 = (EditText) findViewById(R.id.datePicker3);
        txtDatePicker2.setFocusable(false);
        txtDatePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDate(2);
            }

        });
        j_spinner_direction = findViewById(R.id.directionSpinner);
        j_spinner_selected_direction = findViewById(R.id.txtDirectionSpinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, direction);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        j_spinner_direction.setAdapter(adapter1);
        j_spinner_direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                j_spinner_selected_direction.setText(j_spinner_direction.getSelectedItem().toString());

                Log.i("WEAAM  direction", j_spinner_direction.getSelectedItemPosition() +" ");
                isOneDirection = j_spinner_direction.getSelectedItemPosition() == 0? true: false ;

                if(isOneDirection)
                {

                    txtDatePicker2.setVisibility( View.GONE);
                    txtTimePicker2.setVisibility( View.GONE);

                }
                else
                {
                    txtDatePicker2.setVisibility( View.VISIBLE);
                    txtTimePicker2.setVisibility( View.VISIBLE);



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        j_spinner_repeat = findViewById(R.id.repeaterSpinner);
        j_spinner_selected_repeat = findViewById(R.id.txtRepeaterSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repeat);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        j_spinner_repeat.setAdapter(adapter2);
        j_spinner_repeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                j_spinner_selected_repeat.setText(j_spinner_repeat.getSelectedItem().toString());
                repeatedAlarm = j_spinner_repeat.getSelectedItemPosition();
                Log.i("WEAAM  repeated", j_spinner_repeat.getSelectedItemPosition() +" ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void saveRoundTripToFireStore() {

        UpcomingTripModel round = new UpcomingTripModel(
                (List<String>) Arrays.asList(tripTitle.getText().toString(), tripTitle.getText().toString()),
                (List<String>)Arrays.asList(startPoint.getText().toString(), endPoint.getText().toString()),
                (List<String>)Arrays.asList( endPoint.getText().toString(), startPoint.getText().toString()),
                (List<String>)Arrays.asList(txtDatePicker.getText().toString(), txtDatePicker2.getText().toString()),
                (List<String>)Arrays.asList(txtTimePicker.getText().toString(), txtTimePicker2.getText().toString()),
                false,
                0,
                null,
                0,
                0,
                new Timestamp(System.currentTimeMillis()));

        FireStoreHelper.saveUpcomingTrip(round.getRoundTripsMap(), "round trip");


    }

    private boolean validation() {
        boolean valid = true;

        String tripName = tripTitle.getText().toString();
        String start = startPoint.getText().toString();

        String end = endPoint.getText().toString();
        String time = txtTimePicker.getText().toString();
        String date = txtDatePicker.getText().toString();


        if (tripName.isEmpty()) {
            tripTitle.setError("Please enter your trip title");
            valid = false;
        }
        else {
            tripTitle.setError(null);
        }


        if (start.isEmpty()) {
            startPoint.setError("Please enter your start point");
            valid = false;
        } else {
            startPoint.setError(null);
        }


        if (end.isEmpty()) {
            endPoint.setError("Please enter your end point");
            valid = false;
        } else {
            endPoint.setError(null);
        }


        if (time.isEmpty()) {
            txtTimePicker.setError("Please enter your time");
            valid = false;
        } else {
            txtTimePicker.setError(null);
        }

        if (date.isEmpty()) {
            txtDatePicker.setError("Please enter your date");
            valid = false;
        } else {
            txtDatePicker.setError(null);
        }

        return  valid;
    }

    private void saveDataInFireStore() {


        upcomingTripModel = new UpcomingTripModel(tripTitle.getText().toString(),
                startPoint.getText().toString(), endPoint.getText().toString(),
                txtDatePicker.getText().toString(),
                txtTimePicker.getText().toString(),true
                , repeatedAlarm ,
                null,
                new Timestamp( System.currentTimeMillis())

        );

        FireStoreHelper.saveUpcomingTrip(upcomingTripModel.getUpcomingTripsMap(), null);


    }


    private void handleDate(int id) {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
                if (id==1){
                    txtDatePicker.setText(dateText);
                }else{
                    txtDatePicker2.setText(dateText);
                }


            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void handleTime(int id) {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timePicker.setIs24HourView(true);
                Log.i(TAG, "onTimeSet: " + hour + minute);
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.set(Calendar.HOUR_OF_DAY, hour);
//                calendar1.set(Calendar.MINUTE, minute);
//                String dateText = DateFormat.format("h:mm a", calendar1).toString();
//
//                time = calendar1.getTimeInMillis() - (calendar1.getTimeInMillis() % 60000);
//               apm = calendar1.AM_PM;
//               ////////////
//                String myDate = hour+":"+minute;
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                Date date = null;
//                try {
//                    date = sdf.parse(myDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                long millis = date.getTime();
//                time2 = millis + System.currentTimeMillis();
//                ///////////////
                if (id==1){
                    txtTimePicker.setText(hour+":"+minute);
                }else{
                    txtTimePicker2.setText(hour+":"+minute);
                }

            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }
    private void searchPlace(int request_code) {
        Places.initialize(this, getApplication().getString(R.string.api_key));
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder( AutocompleteActivityMode.FULLSCREEN, fields) .build(this);
        //noinspection deprecation
        startActivityForResult(intent, request_code);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                startPoint.setText(place.getAddress());

            }else if (requestCode == 2 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            endPoint.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Some went wrong. Search again", Toast.LENGTH_SHORT).show();
                Log.i(TAG, status.getStatusMessage());
            }
        }
    public  void FindCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(NewTripActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(NewTripActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(NewTripActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(NewTripActivity.this);
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(500)
                    .setFastestInterval(0)
                    .setMaxWaitTime(0)
                    .setSmallestDisplacement(0)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
//                            startPoint.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                        }
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        }
        ;
    }
    public void GetCurrentAddress(){
        try{
            Geocoder geo = new Geocoder(NewTripActivity.this.getApplicationContext(), Locale.getDefault());
            addresses = geo.getFromLocation(wayLatitude, wayLongitude, 1);
            if (addresses.isEmpty()) {
                startPoint.setText("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {
                    startPoint.setText(addresses.get(0).getFeatureName() + ", "
                            + addresses.get(0).getLocality() +", "
                            + addresses.get(0).getAdminArea() + ", "
                            + addresses.get(0).getCountryName());

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(NewTripActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewTripActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewTripActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

                } else {
                    Toast.makeText(NewTripActivity.this, "Location refused", Toast.LENGTH_SHORT).show();
                }
                break;
            case SEND_SMS_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS refused, please try again.", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }

}