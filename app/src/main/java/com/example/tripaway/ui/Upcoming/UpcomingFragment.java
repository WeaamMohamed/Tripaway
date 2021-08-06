package com.example.tripaway.ui.Upcoming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.R;
import com.example.tripaway.RecyclerViewAdapter;
import com.example.tripaway.Trips;
import com.example.tripaway.databinding.FragmentUpcomingBinding;

import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment {
    List<Trips> tripsList ;
    RecyclerView recyclerView;
    private UpcomingViewModel upcomingViewModel;
    private FragmentUpcomingBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        tripsList = new ArrayList<>();
        tripsList.add(new Trips("9.30","2/11/2022","newDamietta","Damietta","newDamietta", new String[]{"alaa"}));
        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.idRVTrips);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter((ArrayList<Trips>) tripsList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}