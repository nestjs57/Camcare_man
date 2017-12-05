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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StatusDetail extends AppCompatActivity {

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    private Intent intent;
    private TextView txtName, txtTel, txtMail, txtType, txtDate, txtAddress1, txtAddress2, txtProblem1, txtProblem2;
    private ImageView txtPath_img1, txtPath_img2, txtPath_img3, txtPath_img4, popupImg;
    private Button btndelete;
    private int chkImg = 0;
    private Boolean del = false;

    private ProgressBar spinner;
    private ProgressDialog progressDialog;
    StorageReference storageReference;
    private String statusChk;
    private Runnable runable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = new ProgressDialog(this);

        btndelete = (Button) findViewById(R.id.btndelete);
        txtName = (TextView) findViewById(R.id.txtName);
        txtTel = (TextView) findViewById(R.id.txtTelNo);
        txtMail = (TextView) findViewById(R.id.txtMail);
        txtType = (TextView) findViewById(R.id.txtType);
        txtDate = (TextView) findViewById(R.id.txtTime);
        txtAddress2 = (TextView) findViewById(R.id.txtLoca2);
        txtAddress1 = (TextView) findViewById(R.id.txtLoca1);
        txtProblem1 = (TextView) findViewById(R.id.txtProblem);
        txtProblem2 = (TextView) findViewById(R.id.txtDetail);
        txtPath_img1 = (ImageView) findViewById(R.id.imageView15);
        txtPath_img2 = (ImageView) findViewById(R.id.imageView16);
        txtPath_img3 = (ImageView) findViewById(R.id.imageView17);
        txtPath_img4 = (ImageView) findViewById(R.id.imageView18);

        connectToFirebase();
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

                if (del == false) {

                    txtName.setText(dataSnapshot.child("name").getValue().toString());
                    txtTel.setText(dataSnapshot.child("tel").getValue().toString());
                    txtMail.setText("-");

                    if (dataSnapshot.child("type").getValue().toString().equals("1")) {
                        txtType.setText("คอมพิวเตอร์");
                    } else {
                        txtType.setText("โน๊ตบุ๊ค");
                    }

                    txtDate.setText(dataSnapshot.child("day").getValue().toString() + " " +
                            dataSnapshot.child("month").getValue().toString() + " " +
                            dataSnapshot.child("year").getValue().toString() + "\n\n" +
                            dataSnapshot.child("hour").getValue().toString() + " : " +
                            dataSnapshot.child("minute").getValue().toString());
                    txtAddress2.setText(dataSnapshot.child("address1").getValue().toString());
                    txtAddress1.setText(dataSnapshot.child("address2").getValue().toString());
                    txtProblem1.setText(dataSnapshot.child("problem1").getValue().toString());
                    txtProblem2.setText(dataSnapshot.child("problem2").getValue().toString());

                    //Toast.makeText(getApplication(), dataSnapshot.child("Path_img1").getValue().toString(), Toast.LENGTH_LONG).show();

                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img1").getValue().toString()).into(txtPath_img1);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img2").getValue().toString()).into(txtPath_img2);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img3").getValue().toString()).into(txtPath_img3);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img4").getValue().toString()).into(txtPath_img4);

                    statusChk = (String) dataSnapshot.child("status").getValue().toString();

                    if (dataSnapshot.child("Path_img1").getValue().toString().equals("null")) {
                        chkImg = 1;
                    } else if (dataSnapshot.child("Path_img2").getValue().toString().equals("null")) {
                        chkImg = 2;
                    } else if (dataSnapshot.child("Path_img3").getValue().toString().equals("null")) {
                        chkImg = 3;
                    } else if (dataSnapshot.child("Path_img4").getValue().toString().equals("null")) {
                        chkImg = 4;
                    }else {
                        chkImg = 5;
                    }

                    if (!statusChk.equals("1")) {
                        btndelete.setVisibility(View.INVISIBLE);
                    }
                    setEvent(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference childref = dbref.child("order").child(intent.getStringExtra("key"));
        childref.addValueEventListener(valueEventListener);
    }


    public void setEvent(final DataSnapshot dataSnapshot) {


        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder =
                        new AlertDialog.Builder(StatusDetail.this);
                builder.setMessage("คุณต้องการลบงานนี้หรือไม่ ?");
                builder.setPositiveButton("ลบ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //spinner.setVisibility(View.VISIBLE);
                        progressDialog.setMessage("กำลังลบ...");
                        progressDialog.setTitle("Delete.");
                        progressDialog.show();
                        final Handler handle = new Handler();
                        runable = new Runnable() {

                            @Override
                            public void run() {
                                //spinner.setVisibility(View.INVISIBLE);
                                progressDialog.dismiss();
                                del = true;
                                btnDelete();
                                finish();
                                handle.removeCallbacks(runable);
                            }
                        };
                        handle.postDelayed(runable, 1500); // delay 3 s.

                    }
                });
                builder.setNegativeButton("ยกเลิก", null);
                builder.create();

                // สุดท้ายอย่าลืม show() ด้วย
                builder.show();


            }
        });

        if (chkImg == 2 || chkImg == 3||chkImg == 4||chkImg == 5) {
            txtPath_img1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(StatusDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img1").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(StatusDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }
        if ( chkImg == 3||chkImg == 4||chkImg == 5) {
            txtPath_img2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(StatusDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img2").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(StatusDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }
        if (chkImg == 4||chkImg == 5) {
            txtPath_img3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(StatusDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img3").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(StatusDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }

        if (chkImg == 5) {
            txtPath_img4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    LayoutInflater inflater = LayoutInflater.from(StatusDetail.this);
                    view = inflater.inflate(R.layout.image_popup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                    Glide.with(getApplication()).load(dataSnapshot.child("Path_img4").getValue().toString()).into(imageViewTarget);

                    AlertDialog.Builder share_dialog = new AlertDialog.Builder(StatusDetail.this);
                    share_dialog.setView(view);
                    share_dialog.show();

                }
            });
        }


    }

    private void btnDelete() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference childref = dbref.child("order").child(intent.getStringExtra("key"));
        childref.removeValue();

    }

}
