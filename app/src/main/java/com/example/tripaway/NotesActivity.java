package com.example.tripaway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class NotesActivity extends AppCompatActivity {
//
    ArrayList<EditText> editTextList;
    ArrayList<String> noteList;
    String selectedDocumentId;

    FirebaseFirestore dbFireStore;
    FirebaseAuth mAuth;
    int lastIndex = -1;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Button btnAdd = findViewById(R.id.btn_add);
        Button btnPlus = findViewById(R.id.btn_plus);
        Button btnMinus = findViewById(R.id.btn_minus);

        dbFireStore  = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        editTextList =  new ArrayList<>();
        noteList =  new ArrayList<>();
        Intent intent = getIntent();
        selectedDocumentId = intent.getStringExtra("documentId");

        linearLayout = findViewById(R.id.add_notes_layout);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);



        //to show old note if exist.
        showOldNotes();




        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNoteEditText();
//                lastIndex ++;
//                editText = new EditText(NotesActivity.this);
//                editText.setHint("YOUR NOTE");
//               // editText.setPadding(10, 10,10,0);
//                editText.setLeft(10);
//                layoutParams.setMargins(5, 10, 5, 0);
//
//                editTextList.add(editText);
//                linearLayout.addView(editTextList.get(lastIndex), layoutParams);


            }
        });



        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastIndex > -1){
                    linearLayout.removeViewAt(lastIndex);
                    editTextList.remove(lastIndex);
                  //  noteList.remove(lastIndex);
                    lastIndex--;
                }
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
                // When clicking this button, the data will be sent to firebase
                // or we may store these notes into a list then we send the list to firebase

//change3
                for(int i=0; i <= lastIndex; i++)
                {
                    Log.i("WEAAM", editTextList.get(i).getText().toString());
                    if(editTextList.get(i).getText().toString().isEmpty())
                    {
                        continue;
                    }
                    noteList.add(editTextList.get(i).getText().toString());
                }


                addNoteListToFireStore();
                NotesActivity.this.finish();
            }
        });

    }

    private void addNoteEditText() {

        EditText editText;
        lastIndex ++;
        editText = new EditText(NotesActivity.this);
        editText.setHint("YOUR NOTE");
        // editText.setPadding(10, 10,10,0);
        editText.setLeft(10);
        layoutParams.setMargins(5, 10, 5, 0);

        editTextList.add(editText);
        linearLayout.addView(editTextList.get(lastIndex), layoutParams);
    }

    private void showOldNotes() {

        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(selectedDocumentId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

              ArrayList<String> oldNotes = (ArrayList<String>) documentSnapshot.get("notes");
              if(oldNotes != null)
               for(int i =0; i< oldNotes.size(); i++)
               {
                   addNoteEditText();
                   editTextList.get(i).setText(oldNotes.get(i));
               }
            }
        });
    }

    private void addNoteListToFireStore() {

        Map<String , Object> notes = new HashMap<>();
        notes.put("notes", noteList);
        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(selectedDocumentId)
                .update(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("WEAAM", "notes list added to fireStore successfully");
            }
        });


    }


}