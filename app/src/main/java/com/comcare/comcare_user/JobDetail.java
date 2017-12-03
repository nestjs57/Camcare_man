package com.comcare.comcare_user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.comcare.comcare_user.Fragments.secondFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class JobDetail extends AppCompatActivity {

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener,li;
    Intent intent = getIntent();
    private TextView txtCusname, txtProblem, txtOther, txtType, txtDate, txtAddress1, txtAddress2, txtProblem1, txtProblem2;
    private ImageView txtPath_img1, txtPath_img2, txtPath_img3, txtPath_img4, popupImg, pathProPic;
    private Button btndelete,btnaccept;
    private int chkImg = 0;
    private Boolean del = false;
    private String Uid, path;
    private ProgressDialog progressDialog;

    private ProgressBar spinner;
    StorageReference storageReference;
    private String statusChk;
    private Runnable runable;
    private DatabaseReference dbref;
    private DatabaseReference childref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = new ProgressDialog(this);

        txtCusname = (TextView) findViewById(R.id.tvCusname);
        txtProblem = (TextView) findViewById(R.id.tvProblem);
        txtOther = (TextView) findViewById(R.id.tvOther);
        txtPath_img1 = (ImageView) findViewById(R.id.imageView4);
        txtPath_img2 = (ImageView) findViewById(R.id.imageView5);
        txtPath_img3 = (ImageView) findViewById(R.id.imageView6);
        txtPath_img4 = (ImageView) findViewById(R.id.imageView7);
        pathProPic = (ImageView) findViewById(R.id.proPic);
        btnaccept = (Button) findViewById(R.id.button7);
//        txtType = (TextView) findViewById(R.id.txtType);
//        txtDate = (TextView) findViewById(R.id.txtTime);
//        txtAddress2 = (TextView) findViewById(R.id.txtLoca2);
//        txtAddress1 = (TextView) findViewById(R.id.txtLoca1);
//        txtProblem1 = (TextView) findViewById(R.id.txtProblem);
//        txtProblem2 = (TextView) findViewById(R.id.txtDetail);
//        spinner = (ProgressBar)findViewById(R.id.progressBar2);
//        spinner.setVisibility(View.INVISIBLE);

        connectToFirebase();
        acceptJob(intent.getStringExtra("key"));




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    private void connectToFirebase() {
        intent = getIntent();
        storageReference = FirebaseStorage.getInstance().getReference();


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {





//                txtDate.setText(dataSnapshot.child("day").getValue().toString() + " " +
//                        dataSnapshot.child("month").getValue().toString() + " " +
//                        dataSnapshot.child("year").getValue().toString() + "\n\n" +
//                        dataSnapshot.child("hour").getValue().toString() + " : " +
//                        dataSnapshot.child("minute").getValue().toString());
//                txtAddress2.setText(dataSnapshot.child("address1").getValue().toString());
//                txtAddress1.setText(dataSnapshot.child("address2").getValue().toString());
//                txtProblem1.setText(dataSnapshot.child("problem1").getValue().toString());
//                txtProblem2.setText(dataSnapshot.child("problem2").getValue().toString());

                Uid = dataSnapshot.child("user_id").getValue().toString();getProPicPath(Uid);
                txtCusname.setText(dataSnapshot.child("name").getValue().toString());
                txtProblem.setText(dataSnapshot.child("problem1").getValue().toString());
                txtOther.setText(dataSnapshot.child("problem2").getValue().toString());


                //Toast.makeText(getApplication(), dataSnapshot.child("Path_img1").getValue().toString(), Toast.LENGTH_LONG).show();

                Glide.with(getApplication()).load(dataSnapshot.child("Path_img1").getValue().toString()).into(txtPath_img1);
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img2").getValue().toString()).into(txtPath_img2);
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img3").getValue().toString()).into(txtPath_img3);
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img4").getValue().toString()).into(txtPath_img4);


                if (dataSnapshot.child("Path_img1").getValue().toString().equals("null")) {
                    chkImg = 1;
                } else if (dataSnapshot.child("Path_img2").getValue().toString().equals("null")) {
                    chkImg = 2;
                    txtPath_img2.setVisibility(View.GONE);
                } else if (dataSnapshot.child("Path_img3").getValue().toString().equals("null")) {
                    chkImg = 3;
                    txtPath_img3.setVisibility(View.GONE);
                } else if (dataSnapshot.child("Path_img4").getValue().toString().equals("null")) {
                    chkImg = 4;
                    txtPath_img4.setVisibility(View.GONE);
                }else {
                    chkImg = 5;
                }


                setEvent(dataSnapshot);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbref = FirebaseDatabase.getInstance().getReference();
        childref = dbref.child("order").child(intent.getStringExtra("key"));
        childref.addValueEventListener(valueEventListener);
    }



    public void setEvent(final DataSnapshot dataSnapshot) {

        if (chkImg == 2 || chkImg == 3||chkImg == 4||chkImg == 5) {
            txtPath_img1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(JobDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img1").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(JobDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();



                }
            });
        }
        if ( chkImg == 3||chkImg == 4||chkImg == 5) {
            txtPath_img2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(JobDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img2").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(JobDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }
        if (chkImg == 4||chkImg == 5) {
            txtPath_img3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(JobDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img3").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(JobDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }

        if (chkImg == 5) {
            txtPath_img4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(JobDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img4").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(JobDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }


    }


    public void getProPicPath(String Uid){

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                path = dataSnapshot.child("profile_image").getValue().toString();
                Glide.with(getApplication()).load(path).into(pathProPic);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbref = FirebaseDatabase.getInstance().getReference();
        childref = dbref.child("user").child(Uid).child("info");
        childref.addValueEventListener(valueEventListener);
    }

//    private void addMarker(double lat, double lng, String text) {
//
//        LatLng latLng = new LatLng(lat, lng);
//        MarkerOptions markerOption = new MarkerOptions();
//        markerOption.position(latLng);
//        markerOption.title(text);
//        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.wrench));
//        mMapView.addMarker(markerOption);
//    }

    public void acceptJob(final String key){
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                ref.child("order").child(key).child("status").setValue("2");
                finish();
            }
        });
    }

}
