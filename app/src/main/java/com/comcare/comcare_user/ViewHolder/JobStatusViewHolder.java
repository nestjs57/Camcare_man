package com.comcare.comcare_user.ViewHolder;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comcare.comcare_user.JobDetail;
import com.comcare.comcare_user.Models.JobStatusModel;
import com.comcare.comcare_user.R;
import com.comcare.comcare_user.JobDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobStatusViewHolder extends RecyclerView.ViewHolder{

    public TextView txtProblem;
    public TextView txtDate;
    public TextView txtStatus;
    public TextView txtName;
    public ImageView img;
    public TextView txtTime;
    public DatabaseReference databaseReference;
    public ValueEventListener valueEventListener;
    public Intent intent;

    public JobStatusViewHolder(View itemView) {
        super(itemView);

        txtName = (TextView) itemView.findViewById(R.id.tvName);
        txtDate = (TextView) itemView.findViewById(R.id.tvDate);
        txtStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        img = (ImageView) itemView.findViewById(R.id.img);
        txtTime = (TextView) itemView.findViewById(R.id.tvTime);




//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(view.getContext(), StatusDetail.class);
//                view.getContext().startActivity(intent);
//
//            }
//        });
    }
}


