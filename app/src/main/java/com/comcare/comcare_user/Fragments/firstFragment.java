package com.comcare.comcare_user.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comcare.comcare_user.Adapter.JobAdapter;
import com.comcare.comcare_user.Models.JobModel;
import com.comcare.comcare_user.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;



/**
 * A simple {@link Fragment} subclass.
 */
public class firstFragment extends Fragment {


    private ArrayList<JobModel> dataSet;
    private JobAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Runnable runable;
    SwipeRefreshLayout swipeRefreshLayout;
    ValueEventListener valueEventListener;




    public firstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_first, container, false);

        dataSet = new ArrayList<JobModel>();

        adapter = new JobAdapter(dataSet);

        recyclerView = (RecyclerView) inflate.findViewById(R.id.rcvyJob);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);



        //dataSet.add(new JobModel("10.9", "Saksit Jantaraplin", "bluescreen", "5 นาทีที่แล้ว", "123456", "in process"));


        connectToFirebase();
        pullDown(inflate);

        showToken(inflate);
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


                        handle.removeCallbacks(runable);
                    }
                };
                handle.postDelayed(runable, 2000); // delay 3 s.
            }
        });
    }

    private void connectToFirebase() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSet.clear();
                adapter.notifyDataSetChanged();

                String km, name, problem, time, jobId, status;

                for (DataSnapshot itemSnap : dataSnapshot.getChildren()) {

                    JobModel jobModel = itemSnap.getValue(JobModel.class);
                    jobId = itemSnap.getKey();

                    km = jobModel.getKm() + "";
                    name = jobModel.getName() + "";
                    problem = jobModel.getproblem1() + "";
                    time = jobModel.getTime() + "";
                    jobId = jobModel.getJobId() + "";
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







                    //if (firebaseDatabase.getUid().equals(uid)) {
                        dataSet.add(new JobModel(km, name, problem, T, jobId, status));
                    //}


                }
                Collections.reverse(dataSet);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference childref = dbref.child("order");
        childref.addValueEventListener(valueEventListener);

    }


}
