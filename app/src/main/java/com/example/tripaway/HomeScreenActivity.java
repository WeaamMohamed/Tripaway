package com.example.tripaway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tripaway.databinding.ActivityHomeScreenBinding;
import com.example.tripaway.utils.FireStoreHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreenActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeScreenBinding binding;
    TextView txtEmail,txtUserName;
    private static FirebaseFirestore dbFireStore;
    private static FirebaseAuth mAuth;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.nav_header_home_screen);

        setSupportActionBar(binding.appBarHomeScreen.toolbar);
        binding.appBarHomeScreen.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(HomeScreenActivity.this, NewTripActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_history, R.id.nav_upcoming, R.id.nav_logout ,R.id.nav_sync)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.email);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            navEmail.setText(user.getEmail());
            String displayName = user.getDisplayName();
            for (UserInfo userInfo : user.getProviderData()) {
                if (displayName == null && userInfo.getDisplayName() != null) {
                    displayName = userInfo.getDisplayName();
                }
            }
//            navUsername.setText("Welcome "+displayName);
            navUsername.setText("Tripawy ");
            navUsername.setTextColor(R.color.dark_blue);
        }


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_screen, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_screen);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}