package com.example.tripaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tripaway.models.OldTripsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class UpcomingTripsActivity extends AppCompatActivity {


    private RecyclerView fireStoreRecyclerView;
    private FirebaseFirestore dbFireStore;
    private FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trips);


        fireStoreRecyclerView = findViewById(R.id.fireStoreRecyclerView);
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Query


        Query query =
                dbFireStore.collection("users").document(mAuth.getUid())
                        .collection("upcoming")
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

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

                return new OldTripsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OldTripsViewHolder holder, int position, @NonNull OldTripsModel model) {


                holder.tvTripName.setText(model.getTripName());
                holder.tvStartPoint.setText(model.getStartPoint());
                holder.tvEndPoint.setText(model.getEndPoint());
                holder.tvDate.setText(model.getDate());
                holder.tvTime.setText(model.getTime());
            }
        };


        //view holder

        fireStoreRecyclerView.setHasFixedSize(false);
        fireStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fireStoreRecyclerView.setAdapter(adapter);



    }


    //viewHolder
    private class OldTripsViewHolder extends  RecyclerView.ViewHolder{

        TextView tvTripName, tvStartPoint, tvEndPoint,
        tvDate, tvTime;

        public OldTripsViewHolder(@NonNull View itemView) {
            super(itemView);

         tvTripName = itemView.findViewById(R.id.txtTripName);
         tvStartPoint = itemView.findViewById(R.id.txtStartPoint);
         tvEndPoint = itemView.findViewById(R.id.txtEndPoint);
         tvTime = itemView.findViewById(R.id.textViewTime);
         tvDate = itemView.findViewById(R.id.txtViewDate);


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }
    }





