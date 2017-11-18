package com.comcare.comcare_user;

import android.app.Activity;
import android.os.Bundle;

public class LoginPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        /*Intent intent = new Intent(getApplication(), MainActivity.class);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out); //ใหม่ , เก่า*/
    }
}
