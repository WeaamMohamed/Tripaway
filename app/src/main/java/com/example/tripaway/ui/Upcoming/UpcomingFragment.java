package com.example.tripaway.ui.Upcoming;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    String clickedDocumentId;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
//    private static final String ST_POINT = "START";
//    private static final String END_POINT = "END";
    // onTripItemClickListener listener;



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
//                Log.i("WEAAM", "document id: " +  getSnapshots().getSnapshot(position).getId());
//                Log.i("WEAAM", "document : " +   getItem(position));
                getSnapshots().getSnapshot(position).getId();
                holder.tvTripName.setText(model.getTripName());
                holder.tvStartPoint.setText(model.getStartPoint());
                holder.tvEndPoint.setText(model.getEndPoint());
                holder.tvDate.setText(model.getDate());
                holder.tvTime.setText(model.getTime());
                holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(view.getContext(), holder.buttonViewOption);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.card);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menuActionNotes:
                                        //handle menu1 click
                                        Intent intent = new Intent(getContext(), NotesActivity.class);
                                        startActivity(intent);
                                        return true;
                                    case R.id.menuActionEdit:
                                        //handle menu2 click
                                        return true;
                                    case R.id.menuActionDelete:
                                        deleteUpcomingTripFromFireStore(getSnapshots().getSnapshot(position).getId());
                                        return true;
                                    case R.id.menuActionCancel:
                                        //handle menu3 click
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });
                holder.startMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(), model.getStartPoint()+model.getEndPoint() , Toast.LENGTH_SHORT).show();
//                        String source = model.getStartPoint();
//                        String dest = model.getEndPoint();
//                        Uri uri = Uri.parse("http://www.google.com/maps/dir/" + source + "/" + dest);
//                        Intent MapIntent = new Intent(Intent.ACTION_VIEW, uri);
//                        MapIntent.setPackage("com.google.android.apps.maps");
//                        if(MapIntent.resolveActivity(getContext().getPackageManager())  != null){
//                            startActivity(MapIntent);
//                        }
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {
//                            //If the draw over permission is not available open the settings screen
//                            //to grant the permission.
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                    Uri.parse("package:" + getContext().getPackageName()));
//                            getActivity().startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
//                        }
//                        else {
//                            //If permission is granted start floating widget service
//                            //startFloatingWidgetService();
//                            getActivity().startService(new Intent(getActivity(), FloatingWidgetService.class));
//                            getActivity().finish();
//                        }
                        clickedDocumentId = getSnapshots().getSnapshot(position).getId();

                        Intent intent = new Intent(getActivity(), FloatingWidgetActivity.class);
                        String stPoint = model.getStartPoint();
                        intent.putExtra("START", stPoint);
                        String endPoint = model.getEndPoint();
                        intent.putExtra("END", endPoint);
                        startActivity(intent);


                        sendDataFromUpcomingToHistory(clickedDocumentId);

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

    private void sendDataFromUpcomingToHistory(String documentId) {



        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        upcomingMapData =  documentSnapshot.getData();
                        sendingDataToOldTripsFireStore(upcomingMapData);
                        deleteUpcomingTripFromFireStore(clickedDocumentId);
                    }
                });









    }

    private void sendingDataToOldTripsFireStore(Map<String, Object> upcomingMapData) {


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

            public UpcomingTripsViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTripName = itemView.findViewById(R.id.txtTripName);
                tvStartPoint = itemView.findViewById(R.id.txtStartPoint);
                tvEndPoint = itemView.findViewById(R.id.txtEndPoint);
                tvTime = itemView.findViewById(R.id.textViewTime);
                tvDate = itemView.findViewById(R.id.txtViewDate);
                buttonViewOption = itemView.findViewById(R.id.btnViewOption);
                startMap = itemView.findViewById(R.id.btnStartTrip);


//


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