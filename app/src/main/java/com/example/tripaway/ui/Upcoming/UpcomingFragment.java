package com.example.tripaway.ui.Upcoming;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
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
import com.example.tripaway.utils.FireStoreHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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




                holder.tvTripName.setText(model.getTripName());
                holder.tvStartPoint.setText("From "+ model.getStartPoint());
                holder.tvEndPoint.setText("to "+model.getEndPoint());
                holder.tvDate.setText(model.getDate());
                holder.tvTime.setText(model.getTime());
                holder.setAlarm(holder.tvDate.getText().toString()+" "+holder.tvTime.getText().toString(),position);
//                DatabaseAdapter adapter = new DatabaseAdapter(getApplicationContext());
//                UpcomingTripModel selected[] = new UpcomingTripModel[adapter.getAllTrips().length];
//                String date_time = new String();
//                for (int i = 0; i < adapter.getAllTrips().length; i++) {
//                    selected[i] = adapter.getAllTrips()[i];
//                    date_time = selected[i].getDate()+" "+selected[i].getTime();
//                    holder.setAlarm(date_time,i);
//
//                }


                holder.btnNotes.setOnClickListener(view1 -> {

                    FireStoreHelper.openNotesDialog(getSnapshots().getSnapshot(position).getId(),
                            getActivity(),
                            "upcoming");



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
                            String documentId = getSnapshots().getSnapshot(position).getId();
                            switch (item.getItemId()) {
                                case R.id.menuActionNotes:
                                    openNotesActivity(documentId);
                                    return true;

                                case R.id.menuActionEdit:
                                    openEditActivity(documentId);
                                    return true;

                                case R.id.menuActionDelete:
                                    FireStoreHelper.deleteUpcomingTripFromFireStore(documentId);
                                    return true;

                                case R.id.menuActionCancel:
                                    FireStoreHelper.sendDataFromUpcomingToHistory(documentId, false);


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

                        FireStoreHelper.sendDataFromUpcomingToHistory(getSnapshots().getSnapshot(position).getId(), true);



                    }
                });

            }
        };
        //view holder
        //TODO:
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        return view;
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
            public void setAlarm(String dt,int reqCode) {
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reqCode, intent, 0);

                SimpleDateFormat yourDateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy HH:mm");
                java.util.Date date = new Date();
                try {
                    date = yourDateFormat.parse(dt);
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