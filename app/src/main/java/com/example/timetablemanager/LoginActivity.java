package com.example.timetablemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity {
    public static String USERNAME_EXTRA = "com.example.timetablemanager.USERNAME_EXTRA";;

    EditText txtUsername, txtPass;
    Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signup = findViewById(R.id.signup);
        txtUsername = findViewById(R.id.username);
        txtPass = findViewById(R.id.password);
        LoginBtn = findViewById(R.id.login);

        txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtUsername.setBackgroundResource(R.drawable.form);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtPass.setBackgroundResource(R.drawable.form);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signup.setOnClickListener(v -> {
            Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(reg);
            finish();
        });

        LoginBtn.setOnClickListener(v -> {
            String username, password;
            username = txtUsername.getText().toString();
            password = txtPass.getText().toString();

            if(!username.equals("") && !password.equals("")) {
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[2];
                        field[0] = "username";
                        field[1] = "password";
                        //Creating array for data
                        String[] data = new String[2];
                        data[0] = username;
                        data[1] = password;
                        PutData putData = new PutData("http://localhost/Timetable/login.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Login Success")) {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra(USERNAME_EXTRA, username);
                                    startActivity(intent);
//                                    Intent intent_header = new Intent(getApplicationContext(), Header.class);
//                                    intent_header.putExtra(USERNAME_EXTRA, username);
//                                    startActivity(intent_header);
                                } else {
                                    txtUsername.setBackgroundResource(R.drawable.form_err);
                                    txtPass.setBackgroundResource(R.drawable.form_err);
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            } else {
                if(username.equals("")) {
                    txtUsername.setBackgroundResource(R.drawable.form_err);
                }
                if(password.equals("")) {
                    txtPass.setBackgroundResource(R.drawable.form_err);
                }
                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forgotPass(View v) {
        Intent intent = new Intent(getApplicationContext(), ForgotPassActivity.class);
        startActivity(intent);
        finish();
    }
}
