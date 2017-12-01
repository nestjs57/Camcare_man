package com.comcare.comcare_user.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comcare.comcare_user.JobDetail;
import com.comcare.comcare_user.Models.JobModel;
import com.comcare.comcare_user.R;
import com.comcare.comcare_user.ViewHolder.JobViewHolder;

import java.util.ArrayList;


public class JobAdapter extends RecyclerView.Adapter<JobViewHolder>{

    private ArrayList<JobModel> jobSet;
    public  JobAdapter(ArrayList<JobModel> jobSet){this.jobSet = jobSet;}
    private ProgressDialog progressDialog;
    Intent intent;
    Runnable runable;


    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        JobViewHolder holder = new JobViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final JobViewHolder holder, int position) {

        final JobModel data = jobSet.get(position);


        holder.txtKm.setText(data.getKm());
        holder.txtName.setText(data.getName());
        holder.txtProblem.setText(data.getproblem1());
        holder.txtTime.setText(data.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                final Handler handle = new Handler();
                intent = new Intent(view.getContext(),JobDetail.class);
                intent.putExtra("key",data.getJobId());

                runable = new Runnable() {

                    @Override
                    public void run() {

                        view.getContext().startActivity(intent);

                        handle.removeCallbacks(runable);
                        progressDialog.dismiss();

                    }
                };
                handle.postDelayed(runable, 400); // delay 3 s.
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobSet.size();
    }
}
