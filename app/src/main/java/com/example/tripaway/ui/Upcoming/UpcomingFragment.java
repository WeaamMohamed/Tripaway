package com.example.tripaway.ui.Upcoming;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.AlarmReceiver;
import com.example.tripaway.EditTripActivity;
import com.example.tripaway.NotesActivity;
import com.example.tripaway.R;
import com.example.tripaway.databinding.FragmentUpcomingBinding;
import com.example.tripaway.models.UpcomingTripModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpcomingFragment extends Fragment {
    List<UpcomingTripModel> upcomingList ;
    //    UOCOMINGRecyclerViewAdapter myAdapter;
    RecyclerView recyclerView;
    private FirebaseFirestore dbFireStore;
    private FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;

    private UpcomingViewModel upcomingViewModel;
    private FragmentUpcomingBinding binding;
    private Map<String, Object>upcomingMapData = new HashMap<>();
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//
//        //TODO: String date? String time?
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.idRVTrips);
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //Query
        Query query =
                dbFireStore.collection("users").document(mAuth.getUid())
                        .collection("upcoming")
                        .orderBy("timestamp", Query.Direction.ASCENDING);
        //recycler options
        FirestoreRecyclerOptions<UpcomingTripModel> options = new FirestoreRecyclerOptions.Builder<UpcomingTripModel>()
                .setQuery(query,UpcomingTripModel.class)
                .build();
        //Recycler adapter
        adapter  = new FirestoreRecyclerAdapter<UpcomingTripModel, UpcomingTripsViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
            }
            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                // Called when there is an error getting data. You may want to update
                // your UI to display an error message to the user.
                // ...
            }
            @NonNull
            @Override
            public UpcomingTripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

                return new UpcomingTripsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull UpcomingTripsViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull UpcomingTripModel model) {






                getSnapshots().getSnapshot(position).getId();
                getSnapshots().getSnapshot(position).getId();
                holder.tvTripName.setText(model.getTripName());
                holder.tvStartPoint.setText("From "+ model.getStartPoint());
                holder.tvEndPoint.setText("to "+model.getEndPoint());
                holder.tvDate.setText(model.getDate());
                holder.tvTime.setText(model.getTime());
                holder.setAlarm(holder.tvDate.getText().toString(),holder.tvTime.getText().toString());


                holder.btnNotes.setOnClickListener(view1 -> {

                    openNotesDialog(getSnapshots().getSnapshot(position).getId());

                 //   openNotesActivity(getSnapshots().getSnapshot(position).getId());


                });
                holder.buttonViewOption.setOnClickListener(view12 -> {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(view12.getContext(), holder.buttonViewOption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.card);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menuActionNotes:
                                    openNotesActivity(getSnapshots().getSnapshot(position).getId());
                                    return true;

                                case R.id.menuActionEdit:
                                    openEditActivity(getSnapshots().getSnapshot(position).getId());
                                    return true;

                                case R.id.menuActionDelete:
                                    deleteUpcomingTripFromFireStore(getSnapshots().getSnapshot(position).getId());
                                    return true;

                                case R.id.menuActionCancel:

                                    sendDataFromUpcomingToHistory(getSnapshots().getSnapshot(position).getId(), false);


                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                });
                holder.startMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(getActivity(), FloatingWidgetActivity.class);
                        String stPoint = model.getStartPoint();
                        intent.putExtra("START", stPoint);
                        String endPoint = model.getEndPoint();
                        intent.putExtra("END", endPoint);
                        startActivity(intent);

                        sendDataFromUpcomingToHistory(getSnapshots().getSnapshot(position).getId(), true);

                        // deleteUpcomingTripFromFireStore(clickedDocumentId);









                    }
                });

            }
        };
        //view holder
        // recyclerView.setHasFixedSize(false);
        //TODO:
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        return view;
    }


    private void openNotesDialog(String selectedDocumentId) {



        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(selectedDocumentId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String message ="";
                ArrayList<String> oldNotes = (ArrayList<String>) documentSnapshot.get("notes");
                if(oldNotes != null)
                    for(int i =0; i< oldNotes.size(); i++)
                    {
//                        //addNoteEditText();
//                        editTextList.get(i).setText(oldNotes.get(i));

                        message += oldNotes.get(i) + "\n";

                    }

                if(message.equals(""))
                    message = "You haven't added any notes";


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set title
                alertDialogBuilder.setTitle("Notes");

                // set dialog message
                alertDialogBuilder.setMessage(message).setCancelable(false);

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                // To cancel dialog
                alertDialog.setCanceledOnTouchOutside(true);


                // alertDialog.dismiss();
            }
        });


    }


    private void openNotesActivity(String clickedDocumentId) {
        Intent intent = new Intent(getActivity(), NotesActivity.class);
        intent.putExtra("documentId", clickedDocumentId);
        startActivity(intent);

    }

    private void openEditActivity(String clickedDocumentId) {

        Intent intent = new Intent(getActivity(), EditTripActivity.class);
        intent.putExtra("documentId", clickedDocumentId);
        startActivity(intent);

    }

    private void sendDataFromUpcomingToHistory(String documentId, boolean done) {



        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        upcomingMapData =  documentSnapshot.getData();
                        sendingDataToOldTripsFireStore(upcomingMapData, done);
                        deleteUpcomingTripFromFireStore(documentId);
                    }
                });









    }

    private void sendingDataToOldTripsFireStore(Map<String, Object> upcomingMapData, boolean done) {


        upcomingMapData.put("done", done);
        upcomingMapData.put("timestamp", new Timestamp( System.currentTimeMillis()));
        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("oldTrips")
                .add(upcomingMapData)
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
//            //Check if the permission is granted or not.
//            if (resultCode == RESULT_OK){
//                //If permission granted start floating widget service
//                //startFloatingWidgetService();
//                requireActivity().startService(new Intent(getActivity(), FloatingWidgetService.class));
////              finish();
//            }
//            else
//                //Permission is not available then display toast
//                Toast.makeText(getActivity(), getResources().getString(R.string.draw_other_app_permission_denied),
//                        Toast.LENGTH_SHORT).show();
//
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

        private void deleteUpcomingTripFromFireStore (String documentId){
            dbFireStore.collection("users")
                    .document(mAuth.getUid())
                    .collection("upcoming")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("WEAAM", "document successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("WEAAM", "Error deleting document: " + e.getMessage());
                        }
                    });
        }
        //viewHolder
        private class UpcomingTripsViewHolder extends RecyclerView.ViewHolder {
            TextView tvTripName, tvStartPoint, tvEndPoint,
                    tvDate, tvTime;
            TextView buttonViewOption;
            Button startMap;
            ImageButton btnNotes;

            public UpcomingTripsViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTripName = itemView.findViewById(R.id.txtTripName);
                tvStartPoint = itemView.findViewById(R.id.txtStartPoint);
                tvEndPoint = itemView.findViewById(R.id.txtEndPoint);
                tvTime = itemView.findViewById(R.id.textViewTime);
                tvDate = itemView.findViewById(R.id.txtViewDate);
                buttonViewOption = itemView.findViewById(R.id.btnViewOption);
                startMap = itemView.findViewById(R.id.btnStartTrip);
                btnNotes = itemView.findViewById(R.id.btnNotes);

            }
            public void setAlarm(String Date ,String time) {
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                String dateTime = Date +" "+ time;
                SimpleDateFormat yourDateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy HH:mm");
                java.util.Date date = new Date();
                try {
                    date = yourDateFormat.parse(dateTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing date time failed", e);
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Calendar current = Calendar.getInstance();

                if(cal.compareTo(current) <= 0)
                {
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(), "Invalid Date/Time", Toast.LENGTH_LONG).show();
                }
                else{
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 0, pendingIntent);
                }
            }
        }

        @Override
        public void onStart () {
            super.onStart();
            if (adapter != null) {
                adapter.startListening();
            }
            //  adapter.startListening();
        }
        @Override
        public void onStop () {
            super.onStop();
            if (adapter != null) {
                adapter.stopListening();
            }
            //adapter.stopListening();
        }
        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
//    This problem is caused by RecyclerView Data modified in different thread. The best way is checking all data access. And a workaround is wrapping LinearLayoutManager.
//
//    Previous answer
//    There was actually a bug in RecyclerView and the support 23.1.1 still not fixed.
//
//    For a workaround, notice that backtrace stacks, if we can catch this Exception in one of some class it may skip this crash. For me, I create a LinearLayoutManagerWrapper and override the onLayoutChildren:
        public class WrapContentLinearLayoutManager extends LinearLayoutManager {
            public WrapContentLinearLayoutManager(Context context) {
                super(context);
            }

            //... constructor
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    Log.e("TAG", "meet a IOOBE in RecyclerView");
                }
            }
        }




    }