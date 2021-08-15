package com.example.tripaway;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.tripaway.models.OldTripsModel;
import com.example.tripaway.models.UpcomingTripModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class NewTripActivity extends AppCompatActivity  {

    EditText tripTitle,startPoint,endPoint,txtTimePicker,txtDatePicker;
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
        startPoint = (EditText) findViewById(R.id.edtStartPoint);
        startPoint.setFocusable(false);
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

            }

        });
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnAddTrip = (Button) findViewById(R.id.btnAddTrip);
        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO: validation

                if(validation())
                {
                    saveDataInFireStore();
                    NewTripActivity.this.finish();
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
                handleTime();
            }
        });
        txtDatePicker = (EditText) findViewById(R.id.datePicker);
        txtDatePicker.setFocusable(false);
        txtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDate();
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



//
//        ArrayList<String > notesList = new ArrayList<>();
//        notesList.add("My note");

        upcomingTripModel = new UpcomingTripModel(tripTitle.getText().toString(),
                startPoint.getText().toString(), endPoint.getText().toString(),
                txtDatePicker.getText().toString(),
                txtTimePicker.getText().toString(),isOneDirection
                , repeatedAlarm ,
                null,
                new Timestamp( System.currentTimeMillis())

        );

        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .add(upcomingTripModel.getUpcomingTripsMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.i("WEAAM", "Data Added to FireStore successfully.");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {


                Log.i("WEAAM", "failed to Add data to FireStore: " + e.getMessage());


            }
        });



    }


//    private void saveOLDDataToFireStore() {
//
//
//
//
//        ArrayList<String > notesList = new ArrayList<>();
//        notesList.add("My note");
//
//        OldTripsModel oldTripsModel = new OldTripsModel(false,
//                "my trip","start","end","duration",
//                "34km",
//                notesList,
//                "date",
//                "time",
//                new Timestamp( System.currentTimeMillis())
//
//        );
//
//        dbFireStore.collection("users")
//                .document(mAuth.getUid())
//                .collection("oldTrips")
//                .add(oldTripsModel.getOldTripsMap())
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//                        Log.i("WEAAM", "Data Added to FireStore successfully.");
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure( Exception e) {
//
//
//                Log.i("WEAAM", "failed to Add data to FireStore: " + e.getMessage());
//
//
//            }
//        });
//
//
//
//    }

    private void handleDate() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                txtDatePicker.setText(dateText);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }
    private void handleTime() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                txtTimePicker.setText(dateText);
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
}