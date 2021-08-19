package com.example.tripaway.ui.history;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.tripaway.R;
import com.example.tripaway.databinding.FragmentUpcomingBinding;
import com.example.tripaway.models.OldTripsModel;
import com.example.tripaway.utils.FireStoreHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.zxing.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    //ArrayList<OldTripsModel> historyList ;
    //
    RecyclerView recyclerView;
    private FragmentUpcomingBinding binding;
    View view;
    Drawable drawable;
    String result;
    private String lat = null,lon = null;

    ///
    //google map object
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation=null;
    Location destinationLocation=null;
    protected LatLng start=null;
    protected LatLng end=null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission=false;

    //polyline object
    private List<Polyline> polylines=null;


   // RecyclerView recyclerView;
    private FirebaseFirestore dbFireStore;
    private FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //request location permission.
        requestPermision();




        View view=inflater.inflate(R.layout.fragment_history, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        recyclerView=(RecyclerView) view.findViewById(R.id.idRVTrips);
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //init google map fragment to show map.



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
            protected void onBindViewHolder(@NonNull OldTripsViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull OldTripsModel model) {

                getSnapshots().getSnapshot(position).getId();


//                Log.i("WEAAM", "document id: " +  getSnapshots().getSnapshot(position).getId());
//                Log.i("WEAAM", "document : " +   getItem(position));
                getSnapshots().getSnapshot(position).getId();


                holder.btnShowNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //openNotesDialog();
                        FireStoreHelper.openNotesDialog(getSnapshots().getSnapshot(position).getId(),
                                getActivity(),
                                "oldTrips");
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //handle indexOutOfBoundsException .. Activity main thread
                        try {
                            showDeleteConfirmationDialog(getSnapshots().getSnapshot(position).getId());
                            // deleteTripFromFireStore(getSnapshots().getSnapshot(position).getId());

                        }
                        catch (Exception e)
                        {
                            Log.e("WEAAM", e.getMessage());
                        }



                    }
                });

                if(getSnapshots().getSnapshot(position).getBoolean("done") && getSnapshots().getSnapshot(position).getBoolean("isOneDirection") )
                {
                    holder.tvTripStatus.setText("Done");
                    drawable = ContextCompat.getDrawable(getActivity(), R.drawable.travel6);

                }
                else if(getSnapshots().getSnapshot(position).getBoolean("done") && !getSnapshots().getSnapshot(position).getBoolean("isOneDirection") )
                {
                    holder.tvTripStatus.setText("Done");
                    drawable = ContextCompat.getDrawable(getActivity(), R.drawable.round);

                }
                else
                {
                    holder.tvTripStatus.setText("Canceled");
                    drawable = ContextCompat.getDrawable(getActivity(), R.drawable.cancel);
                }

                holder.imageHistory.setImageDrawable(drawable);




                if(getSnapshots().getSnapshot(position).getBoolean("isOneDirection"))
                {


                    holder.tvTripName.setText(model.getTripName());
                    holder.tvStartPoint.setText("From " + model.getStartPoint());
                    holder.tvEndPoint.setText("To "+ model.getEndPoint());
                    holder.tvDate.setText(model.getDate());
                    holder.tvTime.setText(model.getTime());
                    holder.tvDistance.setText(model.getDistance());
                    holder.tvDuration.setText(model.getDuration());
                    String startAddress=holder.tvStartPoint.getText().toString();
                    String[] sparts = startAddress.split(",");
                    String spart1 = sparts[0];
                    start = getLatLngFromAddress(spart1);
                    String endtAddress=holder.tvEndPoint.getText().toString();
                    String[] eparts = endtAddress.split(",");
                    String epart1 = eparts[0];
                    end = getLatLngFromAddress(epart1);
                    Findroutes(start,end);

                    //  Drawable drawable  = getResources().getDrawable(R.drawable.cancel);
//                    if(model.isDone() && model.isOneDirection())
//                    {
//                        holder.tvTripStatus.setText("Done");
//                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.travel6);
//
//                    }
//                    else if(model.isDone() && !model.isOneDirection())
//                    {
//                        holder.tvTripStatus.setText("Done");
//                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.round);
//
//                    }
//                    else
//                    {
//                        holder.tvTripStatus.setText("Canceled");
//                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.cancel);
//                    }
//
//                    holder.imageHistory.setImageDrawable(drawable);



                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View hiddenView = holder.itemView.findViewById(R.id.lytHidden);
                            hiddenView.setVisibility( hiddenView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                        }
                    });

                }
                else
                {
                    //round trip



                    holder.tvTripName.setText(model.getTripNameList().get(0));
                    holder.tvStartPoint.setText("From " + model.getStartPointList().get(0));
                    holder.tvEndPoint.setText("To "+ model.getStartPointList().get(1));
                    holder.tvDate.setText(model.getDateList().get(0));
                    holder.tvTime.setText(model.getTimeList().get(0));
//                    start = getLatLngFromAddress(holder.tvStartPoint.getText().toString());
//                    end = getLatLngFromAddress(holder.tvEndPoint.getText().toString());
//                    Findroutes(start,end);
                    ////
                    //TODO:
                    holder.tvDistance.setText(model.getDistance());
                    holder.tvDuration.setText(model.getDuration());



                    holder.tvTripName.setText(model.getTripNameList().get(0));
                    holder.tvStartPoint2.setText("From " + model.getStartPointList().get(1));
                    holder.tvEndPoint2.setText("To "+ model.getStartPointList().get(0));
                    holder.tvDate2.setText(model.getDateList().get(1));
                    holder.tvTime2.setText(model.getTimeList().get(1));
                    //TODO:
                    holder.tvDistance2.setText(model.getDistance());
                    holder.tvDuration2.setText(model.getDuration());


                    //TODO: common
//                    holder.btnShowNotes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //openNotesDialog();
//                            FireStoreHelper.openNotesDialog(getSnapshots().getSnapshot(position).getId(),
//                                    getActivity(),
//                                    "oldTrips");
//                        }
//                    });
                    //  Drawable drawable  = getResources().getDrawable(R.drawable.cancel);
//                    if(model.isDone() && model.isOneDirection())
//                    {
//                        holder.tvTripStatus.setText("Done");
//                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.travel6);
//
//                    }
//                    else if(model.isDone() && !model.isOneDirection())
//                    {
//                        holder.tvTripStatus.setText("Done");
//                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.round);
//
//                    }
//                    else
//                    {
//                        holder.tvTripStatus.setText("Canceled");
//                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.cancel);
//                    }
//
//                    holder.imageHistory.setImageDrawable(drawable);


//                    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            //handle indexOutOfBoundsException .. Activity main thread
//                            try {
//                                showDeleteConfirmationDialog(getSnapshots().getSnapshot(position).getId());
//                                // deleteTripFromFireStore(getSnapshots().getSnapshot(position).getId());
//
//                            }
//                            catch (Exception e)
//                            {
//                                Log.e("WEAAM", e.getMessage());
//                            }
//
//
//
//                        }
//                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View hiddenView = holder.itemView.findViewById(R.id.lytHidden);
                            hiddenView.setVisibility( hiddenView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

                            View hiddenRound = holder.itemView.findViewById(R.id.roundHiddenLayout);
                            hiddenRound.setVisibility( hiddenRound.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                        }
                    });

                }
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
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        if(getActivity()!=null) {
//            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//            if (mapFragment != null) {
//                mapFragment.getMapAsync(this);
//            }
//        }
//    }
    private void requestPermision()
    {
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    OldTripsViewHolder.LOCATION_REQUEST_CODE);
        }
        else{
            locationPermission=true;
        }
    }




        private void showDeleteConfirmationDialog(String selectedDocumentId) throws NotFoundException {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete Trip")
                    .setMessage("Are you sure you want to delete Trip from history?")
                    .setIcon( ContextCompat.getDrawable(getActivity(), R.drawable.travel_image2))
                    .setPositiveButton(
                            "yes",
                            (dialog, which) -> {
                                //Do Something Here
                                FireStoreHelper.deleteOldTripFromFireStore(selectedDocumentId);

                            })
                    .setNegativeButton("No", (dialogInterface, i) -> {

                    }).show();
        }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = view.findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(getContext(),"Finding Route...",Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.design_default_color_primary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start,end);

    }

    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(getContext());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address,1);
            if(addressList!=null){
                Address singleaddress = addressList.get(0);
                LatLng latLng=new LatLng(singleaddress.getLatitude(),singleaddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if(locationPermission) {
            getMyLocation();

        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation(){
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation=location;
                LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 7);
                mMap.animateCamera(cameraUpdate);
            }
        });}

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(getContext(),"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDzTHI4DXKFwrM0xAzwnI-Brz1_3UkcL7g")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }



    //viewHolder
    private class OldTripsViewHolder extends  RecyclerView.ViewHolder{
        //google map object
        private GoogleMap mMap;

        //current and destination location objects
        Location myLocation=null;
        Location destinationLocation=null;
        protected LatLng start=null;
        protected LatLng end=null;

        //to get location permissions.
        private final static int LOCATION_REQUEST_CODE = 23;
        boolean locationPermission=false;

        //polyline object
        private List<Polyline> polylines=null;



        TextView tvTripName, tvDate, tvTime, tvStartPoint,
        tvEndPoint, tvDuration, tvDistance;
        TextView tvTripStatus, tvDate2, tvTime2, tvStartPoint2,
                tvEndPoint2, tvDuration2, tvDistance2;

        Button btnDelete, btnShowNotes;
        ImageView imageHistory;


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
            imageHistory = itemView.findViewById(R.id.imgHistory);


            tvStartPoint2 = itemView.findViewById(R.id.txtStartPoint2);
            tvEndPoint2 = itemView.findViewById(R.id.txtEndPoint2);
            tvTime2 = itemView.findViewById(R.id.textViewTime2);
            tvDate2 = itemView.findViewById(R.id.txtViewDate2);
            tvDuration2 = itemView.findViewById(R.id.txtDuration2);
            tvDistance2 = itemView.findViewById(R.id.txtDistance2);
            tvTripStatus = itemView.findViewById(R.id.txtTripStatus);

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
}