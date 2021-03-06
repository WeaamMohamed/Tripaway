package com.example.tripaway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

    int code;
    String name = null,alarmId = null,startPoint=null,endPoint = null;
    boolean delete = true;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Uri alarmUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if( alarmUri==null){
//            alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); }
//        Ringtone ringtone=RingtoneManager.getRingtone(context,alarmUri);
//        ringtone.play();
        code= intent.getIntExtra("requestCode", 1);
        name = intent.getStringExtra("tripName");
        alarmId = intent.getStringExtra("alarmId");
        startPoint = intent.getStringExtra("startPoint");
        endPoint = intent.getStringExtra("endPoint");
        delete = intent.getBooleanExtra("delete",true);
        Intent intent2 = new Intent(context, AlertDialogue.class);
        intent2.putExtra("requestCode",code);
        intent2.putExtra("tripName",name);
        intent2.putExtra("alarmId",alarmId);
        intent2.putExtra("startPoint",startPoint);
        intent2.putExtra("endPoint",endPoint);
        intent2.putExtra("delete",delete);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);

    }

}