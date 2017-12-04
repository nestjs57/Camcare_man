package com.comcare.comcare_user.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comcare.comcare_user.Adapter.JobAdapter;
import com.comcare.comcare_user.Models.JobModel;
import com.comcare.comcare_user.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class firstFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {


    private ArrayList<JobModel> dataSet;
    private JobAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Runnable runable;
    SwipeRefreshLayout swipeRefreshLayout;
    ValueEventListener valueEventListener;

    private GoogleMap mMapView;
    private LatLng defaultLocation;
    private GoogleApiClient mApiClient;
    private LocationRequest mRequest;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 1000;
    private String latCur;
    private String lngCur;
    private LatLng end = null;
    private LatLng start = null;
    private ProgressDialog progressDialog;
    private String jobId;

    public firstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_first, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        dataSet = new ArrayList<JobModel>();

        adapter = new JobAdapter(dataSet);

        recyclerView = (RecyclerView) inflate.findViewById(R.id.rcvyJob);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);

        //dataSet.add(new JobModel("10.9", "Saksit Jantaraplin", "bluescreen", "5 นาทีที่แล้ว", "123456", "in process"));


        //connectToFirebase();
        pullDown(inflate);

        //showToken(inflate);
        requestRealTimePermission();

        return inflate;
    }


    public void showToken(View view) {
        Log.i("token", FirebaseInstanceId.getInstance().getToken());
    }


    private void pullDown(View inflate) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do something.
                final Handler handle = new Handler();
                runable = new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        dataSet.clear();
                        connectToFirebase();
                        handle.removeCallbacks(runable);
                    }
                };
                handle.postDelayed(runable, 1000); // delay 3 s.
            }
        });
    }

    private void connectToFirebase() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSet.clear();
                adapter.notifyDataSetChanged();

                String km, name, problem, time,  status;

                for (DataSnapshot itemSnap : dataSnapshot.getChildren()) {
                    JobModel jobModel = itemSnap.getValue(JobModel.class);

                    jobId = itemSnap.getKey();

                    km = jobModel.getKm() + "";
                    name = jobModel.getName() + "";
                    problem = jobModel.getproblem1() + "";
                    time = jobModel.getTime() + "";
                    status = jobModel.getStatus() + "";

                    //date of post
                    String minute_inPost = (String) itemSnap.child("minute").getValue();
                    String hour_inPost = (String) itemSnap.child("hour").getValue();
                    String day_inPost = (String) itemSnap.child("day").getValue();
                    String month_inPost = (String) itemSnap.child("month").getValue();
                    String year_inPost = (String) itemSnap.child("year").getValue();
                    //END date of post

//                        //chang to integer for calculate 'time ago'
//                        ////////////////////////////////////////////////////////////////// in firebase
                    int All_int = 0;
                    try {
                        int minute_int = Integer.parseInt(minute_inPost) * 60;
                        int hour_int = Integer.parseInt(hour_inPost) * 3600;
                        int day_int = Integer.parseInt(day_inPost) * 86400;
                        int month_int = Integer.parseInt(month_inPost) * 2592000;
                        int year_int = Integer.parseInt(year_inPost) * 31104000;
                        All_int = minute_int + hour_int + day_int + month_int + year_int;
                    } catch (Exception e) {

                    }

//
//                        ////////////////////////////////////////////////////////////////// in Current time
                    DateFormat minute = new SimpleDateFormat("mm");
                    DateFormat day = new SimpleDateFormat("dd");
                    DateFormat month = new SimpleDateFormat("MM");
                    DateFormat year = new SimpleDateFormat("yyyy");
                    DateFormat hour = new SimpleDateFormat("HH");
                    Calendar calobj = Calendar.getInstance();
//
//
                    int minute_recent = Integer.parseInt(minute.format(calobj.getTime()))* 60;
                    int hour_recent = Integer.parseInt(hour.format(calobj.getTime()))* 3600;
                    int day_recent = Integer.parseInt(day.format(calobj.getTime()))* 86400;
                    int month_recent = Integer.parseInt(month.format(calobj.getTime()))* 2592000;
                    int year_recent = Integer.parseInt(year.format(calobj.getTime()))* 31104000;
                    int All_recent = minute_recent + hour_recent + day_recent + month_recent + year_recent+1;
//
//                        ////////////////////////////////////////////////////////////////// (Time in direbase - Recent Time)
                    int Time_ago = All_recent - All_int;
                    String T = "";

                    if (Time_ago>=0&&Time_ago<=60){
                        //Time_ago/=0;
                        T = "just ago";
                    }else if (Time_ago>60&&Time_ago<=3600){
                        Time_ago/=60;  //minute
                        T = String.valueOf(Time_ago)+" minute ago";

                    }else if (Time_ago>3600&&Time_ago<=86400){
                        Time_ago/=3600; //hour
                        T = String.valueOf(Time_ago)+" hour ago";

                    }else if (Time_ago>86400&&Time_ago<=2419200){
                        Time_ago/=86400; //day
                        T = String.valueOf(Time_ago)+" day ago";

                    }else if (Time_ago>2419200&&Time_ago<=29030400){
                        Time_ago/=2419200; //year
                        T = String.valueOf(Time_ago)+" month ago";

                    }else if (Time_ago>29030400){
                        Time_ago/=29030400; //year
                        T = String.valueOf(Time_ago)+" year ago";

                    }

                    latCur = (String) itemSnap.child("latCur").getValue();
                    lngCur = (String) itemSnap.child("lngCur").getValue();

                    try {
                        end = new LatLng(Double.parseDouble(latCur), Double.parseDouble(lngCur));
                        km = ""+(int) (Double.parseDouble(String.valueOf(SphericalUtil.computeDistanceBetween(start, end) / 1000)));
                    }catch (Exception e){
                       // getLastLocation();
                        //end = new LatLng(Double.parseDouble(String.valueOf(getLastLocation().getLatitude())), Double.parseDouble(String.valueOf(getLastLocation().getLongitude())));

                    }
                    //end = new LatLng(Double.parseDouble(latCur), Double.parseDouble(lngCur));
                   // km = ""+(int) (Double.parseDouble(String.valueOf(SphericalUtil.computeDistanceBetween(start, end) / 1000)));


                    if (status.equals("1")) {

                        dataSet.add(new JobModel(km, name, problem, T, jobId, status));
                    }


                }
                //Collections.reverse(dataSet);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference childref = dbref.child("order");
        childref.addValueEventListener(valueEventListener);

    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapView = googleMap;
    }
    public void requestRealTimePermission() {
        Nammu.askForPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                startLocationTracking();
            }

            @Override
            public void permissionRefused() {

            }
        });
    }
    @SuppressWarnings({"MissingPermission"})
    public void startLocationTracking() {

        mApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        //mMapView.setMyLocationEnabled(true);
                        // get last location
                        try {
                            getLastLocation();
                        } catch (Exception e) {
                        }

                        // set request
                        mRequest = LocationRequest.create();
                        mRequest.setInterval(UPDATE_INTERVAL);
                        mRequest.setFastestInterval(FASTEST_INTERVAL);
                        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mRequest, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                //Toast.makeText(getActivity(), location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_LONG).show();

                                start = new LatLng(location.getLatitude(), location.getLongitude());
                                connectToFirebase();

                                //animateToDefaultLocation();
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Toast.makeText(getActivity(), "Connection is susppended!", Toast.LENGTH_LONG).show();
                    }
                }).build();

        mApiClient.connect();


    }
    @SuppressWarnings({"MissingPermission"})
    public Location getLastLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        //radiusCenter(location);

        if (location != null) {
        }
        return location;
    }
    @Override
    public void onResume() {
        super.onResume();
        recyclerView.smoothScrollToPosition(dataSet.size());

    }
    @Override
    public void onStart(){
        super.onStart();
        recyclerView.smoothScrollToPosition(dataSet.size());


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mApiClient != null) {
            mApiClient.disconnect();
        }
    }

}
