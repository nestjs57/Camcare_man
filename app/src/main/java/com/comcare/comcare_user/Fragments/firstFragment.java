package com.comcare.comcare_user.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
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


        return inflate;
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


                    //if (firebaseDatabase.getUid().equals(uid)) {
                        dataSet.add(new JobModel("10.5", name, problem, "comming soon ...", jobId, status));
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
