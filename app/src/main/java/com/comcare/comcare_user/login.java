package com.comcare.comcare_user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends Activity {

    private TextView txtLogin;
    EditText txtUsername, txtPassword;
    FirebaseAuth firebaseAuth;
    Intent intent;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindWidget();
        OnClick();
    }

    public void OnClick(){

                firebaseAuth = FirebaseAuth.getInstance();

                button = (Button) findViewById(R.id.loginButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (txtUsername.getText().toString().trim().equals("")&&txtPassword.getText().toString().trim().equals("")){
                            Dialog("\n- อีเมล\n- รหัสผ่าน");
                        }else if (txtUsername.getText().toString().trim().equals("")){
                            Dialog("\n- อีเมล");
                        }else if (txtPassword.getText().toString().trim().equals("")){
                            Dialog("\n- รหัสผ่าน");
                        }else {

                            firebaseAuth.signInWithEmailAndPassword(txtUsername.getText().toString(), txtPassword.getText().toString())
                                    .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getApplication(), "Login Failed Please Try Agian", Toast.LENGTH_LONG).show();

                                            } else {
//                        Toast.makeText(EmailLogin.this, "Login Successful", Toast.LENGTH_LONG).show();

                                                intent = new Intent(getApplication(), MainActivity.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }

                    }
                });
    }




    private void bindWidget() {

        txtUsername = (EditText) findViewById(R.id.edtUsername);
        txtPassword = (EditText) findViewById(R.id.edtPassword);

    }
    private void Dialog(String txt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("คุณยังไม่ได้กรอก: " + txt);
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    protected void onResume()
    {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
