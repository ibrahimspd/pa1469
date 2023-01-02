package com.example.myapplication.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button sandboxBtn = findViewById(R.id.SandboxBtn);
        showGenerateLineupFragment(sandboxBtn);

        Button databaseLoginBtn = findViewById(R.id.loginDatabaseBtn);
        showHompageOnClick(databaseLoginBtn);

        Button registerBtn = findViewById(R.id.registerBtn);
        showRegisterPageOnClick(registerBtn);
    }

    private void showGenerateLineupFragment(Button sandboxBtn) {
        sandboxBtn.setOnClickListener(view -> openGenerateLineupFragment());
    }


    private void showHompageOnClick(Button databaseLoginBtn) {
        databaseLoginBtn.setOnClickListener(view -> openLoginActivity());
    }

    private void showRegisterPageOnClick(Button registerBtn) {
        registerBtn.setOnClickListener(view -> openRegisterPageActivity());
    }

    private void openGenerateLineupFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isSandbox", true);
        startActivity(intent);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openRegisterPageActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            AuthorizationResponse authResponse = AuthorizationResponse.fromIntent(data);
            Toast.makeText(this, authResponse.idToken, Toast.LENGTH_SHORT).show();
        }
    }
}
