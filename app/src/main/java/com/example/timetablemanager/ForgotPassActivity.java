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
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        EditText txtEmail = findViewById(R.id.email);
        EditText txtPass = findViewById(R.id.password);
        EditText txtPass2 = findViewById(R.id.password2);
        Button confirmBtn = findViewById(R.id.confirm);

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtEmail.setBackgroundResource(R.drawable.form);
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

        txtPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtPass2.setBackgroundResource(R.drawable.form);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmBtn.setOnClickListener(v -> {
            String email, password, password2;
            email = txtEmail.getText().toString();
            password = txtPass.getText().toString();
            password2 = txtPass2.getText().toString();

            if(!email.equals("") && !password.equals("") && !password2.equals("")) {
                if (!password.equals(password2)) {
                    txtPass2.setBackgroundResource(R.drawable.form_err);
                    Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                } else {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "email";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = email;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.43.33/Timetable/changepass.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Password Changed Successfully")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
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
                if(email.equals("")) txtEmail.setBackgroundResource(R.drawable.form_err);
                if(password.equals("")) txtPass.setBackgroundResource(R.drawable.form_err);
                if(password2.equals("")) txtPass2.setBackgroundResource(R.drawable.form_err);
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