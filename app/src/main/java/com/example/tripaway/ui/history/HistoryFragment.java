package com.example.tripaway.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.R;
import com.example.tripaway.OLDRecyclerViewAdapter;
import com.example.tripaway.databinding.FragmentUpcomingBinding;
import com.example.tripaway.models.OldTripsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {
    ArrayList<OldTripsModel> historyList ;
    RecyclerView recyclerView;
    private HistoryViewModel historyViewModel;
    private FragmentUpcomingBinding binding;
    View view;
    OLDRecyclerViewAdapter myAdapter;
    ArrayList<OldTripsModel> old;


    @Override
    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {









//        // Inflate the layout for this fragment
//        historyList = new ArrayList<>();
//        historyList.add(new OldTripsModel(true,"firstTripInHistory","Damietta","zag","10","10", Arrays.asList(new String[]{"alaa"})));
//        historyList.add(new OldTripsModel(true,"firstTripInHistory","Damietta","zag","10","10", Arrays.asList(new String[]{"alaa"})));
//        historyList.add(new OldTripsModel(true,"firstTripInHistory","Damietta","zag","10","10", Arrays.asList(new String[]{"alaa"})));
        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.idRVTrips);
        OLDRecyclerViewAdapter myAdapter = new OLDRecyclerViewAdapter((ArrayList<OldTripsModel>) historyList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}