package com.example.tripaway;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.tripaway.ui.Upcoming.FloatingWidgetActivity;
import com.example.tripaway.utils.FireStoreHelper;

public class AlertDialogue extends AppCompatActivity {
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
                intent2.putExtra("START", startPoint);
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
}