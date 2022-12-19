package com.example.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.user.OnGetUserListener;
import com.example.myapplication.entites.Credentials;

public class LoginActivity extends AppCompatActivity {

    private TextView emailTxt;
    private TextView passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> {
            Credentials InputtedCredentials = new Credentials.CredentialsBuilder()
                    .email(emailTxt.getText().toString())
                    .password(passwordTxt.getText().toString())
                    .build();
            verifyUser(InputtedCredentials);
            }
        );
    }

    private void verifyUser(Credentials enteredCredentials){
        FirestoreImpl firestore = new FirestoreImpl();
        OnGetUserListener userListener = new OnGetUserListener() {
            @Override
            public void onUserFilled(Credentials credentials) {
                if(credentials.equals(enteredCredentials)){
                    openHomePageActivity();
                }else {
                    emailTxt.setText("");
                    passwordTxt.setText("");
                    Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Exception exception) {
                Log.d("Error", exception.getMessage());
            }
        };
        firestore.checkCredentials(enteredCredentials, userListener);
    }

    private void openHomePageActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
