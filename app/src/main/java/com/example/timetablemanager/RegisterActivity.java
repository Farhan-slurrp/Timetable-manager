package com.example.timetablemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RegisterActivity extends AppCompatActivity {

    EditText nameTxt, emailTxt, usernameTxt, passwordTxt, password2Txt;
    Button SignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameTxt = findViewById(R.id.name);
        emailTxt = findViewById(R.id.email);
        usernameTxt = findViewById(R.id.username);
        passwordTxt = findViewById(R.id.password);
        password2Txt = findViewById(R.id.password2);
        SignUpBtn = findViewById(R.id.register);

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameTxt.setBackgroundResource(R.drawable.form);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        emailTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTxt.setBackgroundResource(R.drawable.form);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        usernameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameTxt.setBackgroundResource(R.drawable.form);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passwordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTxt.setBackgroundResource(R.drawable.form);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password2Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password2Txt.setBackgroundResource(R.drawable.form);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        SignUpBtn.setOnClickListener(v -> {
            String name, email, username, password, password2;
            name = nameTxt.getText().toString();
            email = emailTxt.getText().toString();
            username = usernameTxt.getText().toString();
            password = passwordTxt.getText().toString();
            password2 = password2Txt.getText().toString();

            if(!name.equals("") && !email.equals("") && !username.equals("") && !password.equals("") && !password2.equals("")) {
                if(!password.equals(password2)) {
                    password2Txt.setBackgroundResource(R.drawable.form_err);
                    Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                } else {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "name";
                            field[1] = "email";
                            field[2] = "username";
                            field[3] = "password";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = name;
                            data[1] = email;
                            data[2] = username;
                            data[3] = password;
                            PutData putData = new PutData("http://192.168.43.33/Timetable/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(login);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
            } else {
                if(name.equals("")) {
                    nameTxt.setBackgroundResource(R.drawable.form_err);
                } if(email.equals("")) {
                    emailTxt.setBackgroundResource(R.drawable.form_err);
                } if(username.equals("")) {
                    usernameTxt.setBackgroundResource(R.drawable.form_err);
                } if(password.equals("")) {
                    passwordTxt.setBackgroundResource(R.drawable.form_err);
                } if(password2.equals("")) {
                    password2Txt.setBackgroundResource(R.drawable.form_err);
                }
                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void back(View v) {
        Intent back = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(back);
        finish();
    }
}