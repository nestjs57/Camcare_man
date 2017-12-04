package com.comcare.comcare_user.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.comcare.comcare_user.Models.JobStatusModel;
import com.comcare.comcare_user.R;
import com.comcare.comcare_user.JobDetail;
import com.comcare.comcare_user.ViewHolder.JobStatusViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JobStatusAdapter extends RecyclerView.Adapter<JobStatusViewHolder>{



    private ImageView imageView;
    private ArrayList <JobStatusModel> statusSet;
    private Runnable runable;
    private ProgressDialog progressDialog;
    private Intent intent;

    public JobStatusAdapter(ArrayList<JobStatusModel> statusSet) {
        this.statusSet = statusSet;
    }

    @Override
    public JobStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewh = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status,parent,false);
        final JobStatusViewHolder holder = new JobStatusViewHolder(viewh);

        return holder;
    }

    @Override
    public void onBindViewHolder(JobStatusViewHolder holder, final int position) {

        final JobStatusModel data = statusSet.get(position);

        if (data.getType().equals("2")){
            holder.img.setBackgroundResource(R.drawable.laptop);
        }

        if (data.getStatus().equals("2")){
            holder.txtStatus.setText("ระหว่างดำเนินงาน");
            holder.txtStatus.setTextColor(Color.parseColor("#FF8800"));
        }else if (data.getStatus().equals("3")){
            holder.txtStatus.setText("รอการยืนยัน");
            holder.txtStatus.setTextColor(Color.parseColor("#00FF00"));
        }else if (data.getStatus().equals("4")){
            holder.txtStatus.setText("เสร็จสิ้น");
            holder.txtStatus.setTextColor(Color.parseColor("#00FF00"));
        }



        holder.txtName.setText(data.getName());
        holder.txtDate.setText(data.getDate());
        holder.txtTime.setText(data.getTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                final Handler handle = new Handler();
                intent = new Intent(view.getContext(),JobDetail.class);
                intent.putExtra("key",data.getOrder_id());

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
        return statusSet.size();
    }







}

