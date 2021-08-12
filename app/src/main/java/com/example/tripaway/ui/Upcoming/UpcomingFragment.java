package com.example.tripaway.ui.Upcoming;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.R;
import com.example.tripaway.databinding.FragmentUpcomingBinding;
import com.example.tripaway.models.UpcomingTripModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

public class UpcomingFragment extends Fragment {
    List<UpcomingTripModel> upcomingList ;
//    UOCOMINGRecyclerViewAdapter myAdapter;
    RecyclerView recyclerView;
    private FirebaseFirestore dbFireStore;
    private FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;
    private UpcomingViewModel upcomingViewModel;
    private FragmentUpcomingBinding binding;
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
                                        return true;
                                    case R.id.menuActionEdit:
                                        //handle menu2 click
                                        return true;
                                    case R.id.menuActionDelete:
                                        deleteTripFromFireStore(getSnapshots().getSnapshot(position).getId());
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
                        Toast.makeText(getContext(), model.getStartPoint()+model.getEndPoint() , Toast.LENGTH_SHORT).show();
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

    private void deleteTripFromFireStore(String documentId) {
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
                        Log.e("WEAAM", "Error deleting document: "+  e.getMessage());
                    }
                });


    }

    //viewHolder
    private class UpcomingTripsViewHolder extends  RecyclerView.ViewHolder{

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



//            Log.i("Upcomomingview holder: ",  getItemId() +" ");
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//
//                    Log.i("WEAAM", "item Clicked"  + itemView.getId());
//
//
//
//
//
//                }
//            });


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        }
      //  adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
        //adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
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



    //To send data from adapter to Activity
//    public  interface onTripItemClickListener{
//
//
//        //DocumentSnapshot contains data read from a document in your Cloud FireStore database.
//        // The data can be extracted with the getData() or get(String) methods.
//        void onItemClick(DocumentSnapshot documentSnapshot, int position);
//
//
//
//    }
//
//    public  void setOnItemClickListener(onTripItemClickListener listener){
//        this.listener = listener;
//    };





}