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
    private static final String REDIRECT_URI = "com.example.myapplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button discordLoginBtn = findViewById(R.id.loginDiscordBtn);
        discordLoginBtn.setOnClickListener(view -> {
            loginWithDiscord();
        });

        Button databaseLoginBtn = findViewById(R.id.loginDatabaseBtn);
        showHompageOnClick(databaseLoginBtn);

        Button registerBtn = findViewById(R.id.registerBtn);
        showRegisterPageOnClick(registerBtn);
    }

    private void loginWithDiscord() {
        Runnable runnable = () -> {
            AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                    Uri.parse("https://discord.com/api/oauth2/authorize"),  // authorization endpoint
                    Uri.parse("https://discord.com/api/oauth2/token")  // token endpoint
            );

            String clientId = "1047179104157446207";

            List<String> discordScopes = new ArrayList<String>();
            discordScopes.add("identify");
            discordScopes.add("email");

            AuthorizationRequest authRequest = new AuthorizationRequest.Builder(
                    serviceConfig,  // the authorization service configuration
                    clientId,  // the client ID, typically pre-registered and static
                    ResponseTypeValues.CODE,  // the response_type value: we want a code
                    Uri.parse(REDIRECT_URI)
            )
                    .setScopes(discordScopes)
                    .setCodeVerifier(null, null, null) // THIS IS A HACK, NOT RECOMMENDED, BUT NEEDED
                    .build();

            AuthorizationService authService = new AuthorizationService(this);

           // System.out.println("Performing authorization request to " + authRequest.configuration.authorizationEndpoint);
            Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);

          //startActivityForResult(authIntent, 200);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void showHompageOnClick(Button databaseLoginBtn) {
        databaseLoginBtn.setOnClickListener(view -> openLoginActivity());
    }

    private void showRegisterPageOnClick(Button registerBtn) {
        registerBtn.setOnClickListener(view -> openRegisterPageActivity());
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
