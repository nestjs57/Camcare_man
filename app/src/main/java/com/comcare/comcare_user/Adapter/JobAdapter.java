package com.comcare.comcare_user.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comcare.comcare_user.Models.JobModel;
import com.comcare.comcare_user.R;
import com.comcare.comcare_user.ViewHolder.JobViewHolder;

import java.util.ArrayList;


public class JobAdapter extends RecyclerView.Adapter<JobViewHolder>{

    private ArrayList<JobModel> jobSet;
    public  JobAdapter(ArrayList<JobModel> jobSet){this.jobSet = jobSet;}

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
            public void onClick(View view) {
                Toast.makeText(view.getContext(), data.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobSet.size();
    }
}
