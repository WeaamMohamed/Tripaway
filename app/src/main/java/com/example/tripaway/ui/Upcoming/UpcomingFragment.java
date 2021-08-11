package com.example.tripaway.ui.Upcoming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.R;
import com.example.tripaway.databinding.FragmentUpcomingBinding;
import com.example.tripaway.models.OldTripsModel;
import com.example.tripaway.models.UpcomingTripModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

public class UpcomingFragment extends Fragment {
    List<UpcomingTripModel> upcomingList ;
    RecyclerView recyclerView;
    private FirebaseFirestore dbFireStore;
    private FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;
    private UpcomingViewModel upcomingViewModel;
    private FragmentUpcomingBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//
//        //TODO: String date? String time?

        // Inflate the layout for this fragment
//        upcomingList = new ArrayList<>();
//        upcomingList.add(new UpcomingTripModel("Damietta","dam","zag","2/11/2022","9.30",true,1, Arrays.asList(new String[]{"alaa"}),
//                new Timestamp( System.currentTimeMillis()))
//               );
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

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
//        UOCOMINGRecyclerViewAdapter myAdapter = new UOCOMINGRecyclerViewAdapter((ArrayList<UpcomingTripModel>) upcomingList);
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


/*
        upcomingViewModel =
                new ViewModelProvider(this).get(UpcomingViewModel.class);

        binding = FragmentUpcomingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        upcomingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                RecyclerView myrv = (RecyclerView) root.findViewById(R.id.idRVTrips);
                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,tripsList);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                myrv.setLayoutManager(gridLayoutManager);
                myrv.setAdapter(myAdapter);
            }
        });*/

        return view;
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
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}