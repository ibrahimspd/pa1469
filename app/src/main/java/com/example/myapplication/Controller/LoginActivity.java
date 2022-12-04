package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;


public class LoginActivity extends AppCompatActivity {
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.println("LoginActivity.onCreate");

        Button discordLoginBtn = (Button) findViewById(R.id.loginDiscord);
        loginWithDiscord(discordLoginBtn);

        Button databaseLoginBtn = (Button) findViewById(R.id.loginDatabase);
        showHompageOnClick(databaseLoginBtn);

    }



    private void loginWithDiscord(Button discordLoginBtn) {
        discordLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String url = "https://discord.com/api/oauth2/authorize?client_id=1047179104157446207&redirect_uri=http%3A%2F%2F78.141.233.225%3A4545%2Flogin&response_type=code&scope=identify";
                // create http request
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                System.out.println("request: " + request);*/
                AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                        Uri.parse("https://discord.com/api/oauth2/authorize"),  // authorization endpoint
                        Uri.parse("")  // token endpoint
                );

                String clientId = "1047179104157446207";

                System.out.println("serviceConfig");

                String[] discordScopes = {"identity", "email"};

                AuthorizationRequest authRequestBuilder = new AuthorizationRequest.Builder(
                        serviceConfig,  // the authorization service configuration
                        clientId,  // the client ID, typically pre-registered and static
                        "200",  // the response_type value: we want a code
                        Uri.parse("")// the redirect URI to which the auth response is sent

                ).setScopes(discordScopes)
                        .setCodeVerifier(null, null, null).build();


                AuthorizationService authService = new AuthorizationService(context);

                Intent authorizationRequestIntent = authService.getAuthorizationRequestIntent(authRequestBuilder);
                System.out.println("serviceConfig");
                startActivityForResult(authorizationRequestIntent, 200);
            }

            /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.ActivityResultContract(requestCode, resultCode, data);
                System.out.println("onActivityResult");
                System.out.println("requestCode: " + requestCode);
                System.out.println("resultCode: " + resultCode);
                System.out.println("data: " + data);
            }*/

           /* @Override
            public void onActivityResult(Integer requestCode, Integer resultCode , resultData: Intent?) {
                super.onActivityResult(requestCode, resultCode, resultData)

                when (requestCode) {
                    200 -> {
                        resultData?.let {
                            val authResponse = AuthorizationResponse.fromIntent(resultData)
                            authResponse?.let { response ->
                                    println("Access Token = ${response.accessToken}")
                            }?: run {
                                val authException = AuthorizationException.fromIntent(resultData)
                                authException?.let { exception ->
                                        println("Authentication Error = ${exception.errorDescription}")
                                }
                            }
                        }
                    }
                }*/

        });
    }

    private void showHompageOnClick(Button databaseLoginBtn) {
        databaseLoginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openHomePageActivity();
            }
        });
    }

    private void openHomePageActivity(){
        Intent intent = new Intent(this, GenerateLineupActivity.class);
        startActivity(intent);
    }
}
