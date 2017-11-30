package com.comcare.comcare_user.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.comcare.comcare_user.R;


public class JobViewHolder extends RecyclerView.ViewHolder {

    public TextView txtKm, txtName, txtProblem, txtTime;


    public JobViewHolder(View itemView) {
        super(itemView);

        txtKm = (TextView) itemView.findViewById(R.id.tvKm);
        txtName = (TextView) itemView.findViewById(R.id.tvName);
        txtProblem = (TextView) itemView.findViewById(R.id.tvProblem);
        txtTime = (TextView) itemView.findViewById(R.id.tvTime);

    }
}
