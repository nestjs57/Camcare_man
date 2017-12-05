package com.comcare.comcare_user.FragmentPage2;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comcare.comcare_user.Adapter.JobStatusAdapter;
import com.comcare.comcare_user.Models.JobStatusModel;
import com.comcare.comcare_user.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentStatus1 extends Fragment {

    private ArrayList<JobStatusModel> dataSet;
    private JobStatusAdapter adapter;
    ValueEventListener valueEventListener;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser firebaseDatabase = FirebaseAuth.getInstance().getCurrentUser();
    SwipeRefreshLayout swipeRefreshLayout;
    private Runnable runable;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog progressDialog;


    public FragmentStatus1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_fragment_status1, container, false);
        dataSet = new ArrayList<JobStatusModel>();

        adapter = new JobStatusAdapter(dataSet);


        recyclerView = (RecyclerView) inflate.findViewById(R.id.rcvyStatus);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);

//      dataSet.add(new JobStatusModel("0", "0", "0", "0", "0", "0", "0"));
        connectToFirebase();
//      Inflate the layout for this fragment
        pullDown(inflate);


        return inflate;
    }

    @Override
    public void onResume() {

        super.onResume();
        //dataSet.clear();
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

                String name, date, status, type, time, uid, oid, mid;

                for (DataSnapshot itemSnap : dataSnapshot.getChildren()) {

                    JobStatusModel statusModel = itemSnap.getValue(JobStatusModel.class);
                    oid = itemSnap.getKey();

                    name = statusModel.getName() + "";
                    status = statusModel.getStatus() + "";
                    type = statusModel.getType() + "";
                    time = statusModel.getTime() + "";
                    mid = (String) itemSnap.child("man_id").getValue();



                    String day = (String) itemSnap.child("day").getValue();
                    String month = (String) itemSnap.child("month").getValue();
                    String year = (String) itemSnap.child("year").getValue();

                    String hour = (String) itemSnap.child("hour").getValue();
                    String minute = (String) itemSnap.child("minute").getValue();

                    String TotalDateTime = day+"-"+month+"-"+year+" | "+hour+" : "+minute;

                    if (firebaseDatabase.getUid().toString().equals(mid)) {
                        if (status.equals("2") || status.equals("3")) {
                            dataSet.add(new JobStatusModel(name, TotalDateTime, status, type, time, mid, oid));
                        }
                    }
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
