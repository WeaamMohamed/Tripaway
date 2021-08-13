package com.example.tripaway.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.R;
import com.example.tripaway.databinding.FragmentUpcomingBinding;
import com.example.tripaway.models.OldTripsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    //ArrayList<OldTripsModel> historyList ;
    //
    RecyclerView recyclerView;
    private HistoryViewModel historyViewModel;
    private FragmentUpcomingBinding binding;
    View view;
    ArrayList<OldTripsModel> old;


   // RecyclerView recyclerView;
    private FirebaseFirestore dbFireStore;
    private FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.idRVTrips);
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //Query


        Query query =
                dbFireStore.collection("users").document(mAuth.getUid())
                        .collection("oldTrips")
                        .orderBy("timestamp", Query.Direction.ASCENDING);







        //recycler options
        FirestoreRecyclerOptions<OldTripsModel> options = new FirestoreRecyclerOptions.Builder<OldTripsModel>()
                .setQuery(query,OldTripsModel.class)
                .build();


        //Recycler adapter
        adapter  = new FirestoreRecyclerAdapter<OldTripsModel, OldTripsViewHolder>(options) {



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
            public OldTripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card_view, parent, false);

                return new OldTripsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OldTripsViewHolder holder, int position, @NonNull OldTripsModel model) {

                getSnapshots().getSnapshot(position).getId();


//                Log.i("WEAAM", "document id: " +  getSnapshots().getSnapshot(position).getId());
//                Log.i("WEAAM", "document : " +   getItem(position));
                getSnapshots().getSnapshot(position).getId();


                holder.tvTripName.setText(model.getTripName());
                holder.tvStartPoint.setText("From " + model.getStartPoint());
                holder.tvEndPoint.setText("To "+ model.getEndPoint());
                holder.tvDate.setText(model.getDate());
                holder.tvTime.setText(model.getTime());
                holder.tvDistance.setText(model.getDistance());
                holder.tvDuration.setText(model.getDuration());


                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //handle indexOutOfBoundsException .. Activity main thread
                        try {
                            deleteTripFromFireStore(getSnapshots().getSnapshot(position).getId());

                        }
                        catch (Exception e)
                        {
                            Log.e("WEAAM", e.getMessage());
                        }



                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View hiddenView = holder.itemView.findViewById(R.id.lytHidden);
                        hiddenView.setVisibility( hiddenView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
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




    //viewHolder
    private class OldTripsViewHolder extends  RecyclerView.ViewHolder{


        TextView tvTripName, tvDate, tvTime, tvStartPoint,
        tvEndPoint, tvDuration, tvDistance;

        Button btnDelete, btnShowNotes;

        public OldTripsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTripName = itemView.findViewById(R.id.txtTripName);
            tvStartPoint = itemView.findViewById(R.id.txtStartPoint);
            tvEndPoint = itemView.findViewById(R.id.txtEndPoint);
            tvTime = itemView.findViewById(R.id.textViewTime);
            tvDate = itemView.findViewById(R.id.txtViewDate);
            tvDuration = itemView.findViewById(R.id.txtDuration);
            tvDistance = itemView.findViewById(R.id.txtDistance);
            btnDelete = itemView.findViewById(R.id.btnStartTrip);
            btnShowNotes = itemView.findViewById(R.id.btnNotes);





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



    private void deleteTripFromFireStore(String documentId) {
        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("oldTrips")
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