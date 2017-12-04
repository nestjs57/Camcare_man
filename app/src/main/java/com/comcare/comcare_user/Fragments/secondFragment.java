package com.comcare.comcare_user.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comcare.comcare_user.FragmentPage2.FragmentStatus1;
import com.comcare.comcare_user.FragmentPage2.FragmentStatus2;
import com.comcare.comcare_user.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class secondFragment extends Fragment {
    private Button btn1;
    private Button btn2;
    private View view1;
    private View view2;

    public secondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        final FragmentStatus1 oneFragment = new FragmentStatus1();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, oneFragment);
        transaction.commit();


        bindWidget(v);
        setEvent(v);
        return v;
    }
    private void setEvent(View v) {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setTextColor(Color.parseColor("#4052b5"));
                view2.setVisibility(View.INVISIBLE);
                btn2.setTextColor(Color.parseColor("#bdc1c9"));
                view1.setVisibility(View.VISIBLE);

                FragmentStatus1 oneFragment = new FragmentStatus1();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, oneFragment);
                transaction.commit();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                btn1.setTextColor(Color.parseColor("#bdc1c9"));
                view1.setVisibility(View.INVISIBLE);
                btn2.setTextColor(Color.parseColor("#4052b5"));
                view2.setVisibility(View.VISIBLE);

                FragmentStatus2 twoFragment = new FragmentStatus2();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,twoFragment );
                transaction.commit();

            }
        });
    }

    private void bindWidget(View v) {
        btn1 = (Button) v.findViewById(R.id.btn1);
        btn2 = (Button) v.findViewById(R.id.btn2);
        view1 = (View) v.findViewById(R.id.view1);
        view2 = (View) v.findViewById(R.id.view2);

    }

}
