package com.example.myapplication.model;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.myapplication.controller.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginModel {

    private FirebaseAuth auth =  FirebaseAuth.getInstance();

    private Context context;

    public LoginModel(Context context) {
        this.context = context;
    }

    public boolean isFieldsEmpty(String email, String password) {
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("id",  task.getResult().getUser().getUid());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
