package com.example.tripaway;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.tripaway.ui.Upcoming.FloatingWidgetActivity;
import com.example.tripaway.utils.FireStoreHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class AlertDialogue extends AppCompatActivity {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    List<Address> addresses;
    String sPoint = null;
    int i = 0;
    int reqCode;
    NotificationCompat.Builder mBuilder;
    Intent ii;
    PendingIntent pendingIntent;

    String tripName = null,alarmId = null,startPoint = null,endpoint= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //comment this line if you need to show Title.
        setContentView(R.layout.activity_alert_dialogue);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Uri alarmUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if( alarmUri==null){
            alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); }
        Ringtone ringtone=RingtoneManager.getRingtone(this,alarmUri);
        ringtone.play();
        Intent intent = getIntent();
        reqCode = intent.getIntExtra("requestCode", 1);
        tripName = intent.getStringExtra("tripName");
        alarmId = intent.getStringExtra("alarmId");
        startPoint = intent.getStringExtra("startPoint");
        endpoint = intent.getStringExtra("endPoint");



        // Setting Dialog Title
        builder.setTitle(tripName);

        // Setting Dialog Message
        builder.setMessage("reminder your trip...");
        // Add the buttons
        NotificationManager mNotificationManager;
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                FireStoreHelper.sendDataFromUpcomingToHistory(alarmId, true);
                Intent intent2 = new Intent(getApplicationContext(), FloatingWidgetActivity.class);
                if (sPoint==null){
                    intent2.putExtra("START", startPoint);
                }else {
                    intent2.putExtra("START", sPoint);
                }
//                intent2.putExtra("START", startPoint);
                intent2.putExtra("END", endpoint);
                startActivity(intent2);
                mNotificationManager.cancel(reqCode);
                ringtone.stop();
                finish();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                FireStoreHelper.sendDataFromUpcomingToHistory(alarmId, false);
                mNotificationManager.cancel(reqCode);
                ringtone.stop();
                finish();
            }

        });
        builder.setNeutralButton(R.string.snooze, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked snooze button
                mBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                ii = new Intent(getApplicationContext(), AlarmReceiver.class);
                ii.putExtra("requestCode",reqCode);
                ii.putExtra("tripName",tripName);
                ii.putExtra("alarmId",alarmId);
                ii.putExtra("startPoint",startPoint);
                ii.putExtra("endPoint",endpoint);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reqCode, ii, 0);

                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(tripName);
                bigText.setBigContentTitle("Today's your trip");
                bigText.setSummaryText(tripName);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                mBuilder.setContentTitle(tripName);
                mBuilder.setContentText("reminder");
                mBuilder.setPriority(Notification.PRIORITY_MAX);
                mBuilder.setStyle(bigText);


                // === Removed some obsoletes
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    String channelId = "Your_channel_id";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(channelId);
                }
                mBuilder.setOngoing(true);
                mNotificationManager.notify(reqCode, mBuilder.build());


                //stop ring
                BroadcastReceiver br = new AlarmReceiver();
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                getApplicationContext().registerReceiver(br, filter);
                unregisterReceiver(br);
                finish();


            }
        });
        // Set other dialog properties
        //....
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public  void FindCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
            Geocoder geo = new Geocoder(getApplicationContext().getApplicationContext(), Locale.getDefault());
            addresses = geo.getFromLocation(wayLatitude, wayLongitude, 1);
            if (addresses.isEmpty()) {
//                startPoint.setText("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {
                    startPoint = addresses.get(0).getFeatureName() + ", "
                            + addresses.get(0).getLocality() +", "
                            + addresses.get(0).getAdminArea() + ", "
                            + addresses.get(0).getCountryName();

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}