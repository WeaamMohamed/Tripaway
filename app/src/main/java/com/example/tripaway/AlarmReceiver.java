package com.example.tripaway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
//        Uri alarmUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if( alarmUri==null){
//            alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); }
//        Ringtone ringtone=RingtoneManager.getRingtone(context,alarmUri);
//        ringtone.play();
        Intent intent2 = new Intent(context, AlertDialogue.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);

    }
}