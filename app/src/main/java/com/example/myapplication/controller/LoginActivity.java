package com.example.myapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.LoginModel;
import com.example.myapplication.R;
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView emailTxt = findViewById(R.id.emailTxt);
        TextView passwordTxt = findViewById(R.id.passwordTxt);
        LoginModel loginModel = new LoginModel(this);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> {

            String email = emailTxt.getText().toString();
            String password = passwordTxt.getText().toString();

            if (!loginModel.isFieldsEmpty(email, password))
                loginModel.signIn(email, password);
        });
    }
}
