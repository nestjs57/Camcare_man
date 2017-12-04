package com.comcare.comcare_user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class JobDetail extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener,li;
    Intent intent = getIntent();
    private TextView txtCusname, txtProblem, txtOther, txtType, txtDate, txtAddress1, txtAddress2, txtProblem1, txtProblem2;
    private ImageView txtPath_img1, txtPath_img2, txtPath_img3, txtPath_img4, popupImg, pathProPic;
    private Button btnCall,btnaccept;
    private int chkImg = 0;
    private Boolean del = false;
    private String Uid, path;


    private ProgressBar spinner;
    StorageReference storageReference;
    private String statusChk;
    private Runnable runable;
    private DatabaseReference dbref;
    private DatabaseReference childref;



    private GoogleMap mMapView;
    private LatLng defaultLocation;
    private GoogleApiClient mApiClient;
    private LocationRequest mRequest;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 1000;
    private double latCur;
    private double lngCur;

    private ProgressDialog progressDialog;
    private String token;

    private static final String AUTH_KEY = "key=AAAA2AyIZok:APA91bH6i3O8cGTzcjcNJtLC2kk8Zdn_eiRodNcGA1WJWxsWkA2AyEBCDGTEqlxXf88uMm7e9Hv67v5g_wAnGTQpi8m81SkjGSmewH4mQuk0EgcDePQy_j2xYjsg8k5-2KRNUNBo4UAI";
    private TextView mTextView;


    public JobDetail() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        progressDialog = new ProgressDialog(this);
        //showToken();
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
        btnCall  = (Button) findViewById(R.id.btnCall);
        btnaccept = (Button) findViewById(R.id.button4);
//        txtType = (TextView) findViewById(R.id.txtType);
//        txtDate = (TextView) findViewById(R.id.txtTime);
//        txtAddress2 = (TextView) findViewById(R.id.txtLoca2);
//        txtAddress1 = (TextView) findViewById(R.id.txtLoca1);
//        txtProblem1 = (TextView) findViewById(R.id.txtProblem);
//        txtProblem2 = (TextView) findViewById(R.id.txtDetail);
//        spinner = (ProgressBar)findViewById(R.id.progressBar2);
//        spinner.setVisibility(View.INVISIBLE);


        //connectToFirebase();


        bindWidgetMap();


    }
    private void intentCall(final String number){
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = number;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
    }
    private void bindWidgetMap() {

        //
        android.app.FragmentManager fragmentMgr = getFragmentManager();
        SupportMapFragment mMapViewFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap);
        mMapViewFragment.getMapAsync(this);


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

        acceptJob(intent.getStringExtra("key"));
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Uid = dataSnapshot.child("user_id").getValue().toString();getProPicPath(Uid);
                txtCusname.setText(dataSnapshot.child("name").getValue().toString());
                txtProblem.setText(dataSnapshot.child("problem1").getValue().toString());
                txtOther.setText(dataSnapshot.child("problem2").getValue().toString());

                String lat = (String)  dataSnapshot.child("latCur").getValue();
                String lng = (String)  dataSnapshot.child("lngCur").getValue();

                //token = (String) dataSnapshot.child("token").getValue();

               // addMarker(Double.parseDouble(lat),Double.parseDouble(lng),"");
                //Toast.makeText(getApplication(), dataSnapshot.child("Path_img1").getValue().toString(), Toast.LENGTH_LONG).show();
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img1").getValue().toString()).into(txtPath_img1);
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img2").getValue().toString()).into(txtPath_img2);
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img3").getValue().toString()).into(txtPath_img3);
                Glide.with(getApplication()).load(dataSnapshot.child("Path_img4").getValue().toString()).into(txtPath_img4);


                intentCall(dataSnapshot.child("tel").getValue().toString());
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
                addMarker (Double.parseDouble(lat),Double.parseDouble(lng),"");

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

                //try {
                path = (String) dataSnapshot.child("profile_image").getValue();
                //}catch (Exception e){
                //}

                token = (String) dataSnapshot.child("token").getValue();
                Glide.with(getApplication()).load(path).transform(new CircleTransform(getApplication())).into(pathProPic);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbref = FirebaseDatabase.getInstance().getReference();
        childref = dbref.child("user").child(Uid).child("info");
        childref.addValueEventListener(valueEventListener);
    }


    public void acceptJob(final String key){
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(JobDetail.this, token,
                        Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(JobDetail.this);
                builder.setMessage("ต้องการรับงานนี้หรือไม่ ?");

                builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog.setMessage("รอสักครู่ ...");
                        progressDialog.show();
                        final Handler handle = new Handler();
                        runable = new Runnable() {

                            @Override
                            public void run() {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                ref.child("order").child(key).child("status").setValue("2");
                                progressDialog.dismiss();
                                sendWithOtherThread("token");
                                finish();
                                handle.removeCallbacks(runable);
                            }
                        };
                        handle.postDelayed(runable, 500);

                    }
                });
                builder.setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();


            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapView = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        connectToFirebase();
           }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void addMarker(double lat, double lng, String text) {

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(text);
        mMapView.addMarker(markerOption);
        mMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }


    //noti
    // notification

    public void showToken() {
        Log.i("token", FirebaseInstanceId.getInstance().getToken());
    }
    public void sendToken(View view) {
        sendWithOtherThread("token");
    }
    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }
    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Comcare");
            jNotification.put("body", "ช่างตอบรับงานของคุณแล้ว");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "ThirdActivity");
            jNotification.put("icon", "ic_notification");

            //jData.put("picture", "http://opsbug.com/static/google-io.jpg");

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put(token);
                    //ja.put(FirebaseInstanceId.getInstance().getToken());
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/news");
                    break;
                case "condition":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    break;
                default:
                    jPayload.put("to", token);
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    //mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

}
