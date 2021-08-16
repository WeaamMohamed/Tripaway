package com.example.tripaway.ui.logout;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tripaway.LoginActivity;
import com.example.tripaway.databinding.FragmentLogoutBinding;
import com.example.tripaway.models.DatabaseAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentLogoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        Intent intent = new Intent(getContext(), NewTripActivity.class);
//        startActivity(intent);

        DatabaseAdapter databaseAdapter= new DatabaseAdapter(getApplicationContext());
        databaseAdapter.deleteAllTrips();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent ) ;
        getActivity().finish();




        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}