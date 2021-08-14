package com.example.tripaway;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;

public class NotesActivity extends AppCompatActivity {
    EditText editText;
    Vector<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Button btnAdd = findViewById(R.id.btn_add);
        Button btnPlus = findViewById(R.id.btn_plus);
        Button btnMinus = findViewById(R.id.btn_minus);

        notes = new Vector<>();

        LinearLayout linearLayout = findViewById(R.id.add_notes_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = new EditText(NotesActivity.this);
                editText.setHint("YOUR NOTE");
                layoutParams.leftMargin = 11;
                linearLayout.addView(editText, layoutParams);

            }
        });


        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout.getChildCount() > 0){
                    linearLayout.removeViewAt(0);
                }
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
                // When clicking this button, the data will be sent to firebase
                // or we may store these notes into a list then we send the list to firebase
            }
        });

    }
}