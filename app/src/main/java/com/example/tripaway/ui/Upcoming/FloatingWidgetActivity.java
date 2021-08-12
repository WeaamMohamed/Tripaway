package com.example.tripaway.ui.Upcoming;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripaway.R;

import java.util.Vector;

public class FloatingWidgetActivity extends AppCompatActivity {

    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
//    private static final String ST_POINT = "START";
//    private static final String END_POINT = "END";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);

//        LinearLayout layout = findViewById(R.id.checkbox_layout);
//        Vector<String> notes = new Vector<>();
//        notes.add("note 1");
//        notes.add("note 2");
//        notes.add("note 3");
//        notes.add("note 4");
//
//        for (int i = 0; i < notes.size(); i++) {
//            CheckBox checkBox = new CheckBox(this);
//            checkBox.setText(notes.get(i));
//            layout.addView(checkBox);
//        }

        intent = getIntent();
        String source = intent.getStringExtra("START");
        String dest = intent.getStringExtra("END");
        Uri uri = Uri.parse("http://www.google.com/maps/dir/" + source + "/" + dest);
        Intent MapIntent = new Intent(Intent.ACTION_VIEW, uri);
        MapIntent.setPackage("com.google.android.apps.maps");
        if(MapIntent.resolveActivity(getPackageManager()) != null){
            startActivity(MapIntent);
        }

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(FloatingWidgetActivity.this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else {
            //If permission is granted start floating widget service
            //startFloatingWidgetService();
            startService(new Intent(FloatingWidgetActivity.this, FloatingWidgetService.class));
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK){
                //If permission granted start floating widget service
                //startFloatingWidgetService();
                startService(new Intent(FloatingWidgetActivity.this, FloatingWidgetService.class));
                finish();
            }
            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
