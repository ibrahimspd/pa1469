package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.player.OnAddPlayerListener;
import com.example.myapplication.entites.Player;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryPickerView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText userName;
    EditText passText;
    EditText confPassText;
    CountryPickerView cpp;
    EditText numberText;
    Spinner positionDropdown;
    Button register;

    String regex = "^(.+)@(.+)$";
    Pattern pattern = Pattern.compile(regex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.textEmail);
        userName = findViewById(R.id.textUsername);
        passText = findViewById(R.id.textPassword);
        confPassText = findViewById(R.id.textConfirmPassword);
        cpp = findViewById(R.id.countryPicker);
        numberText = findViewById(R.id.number);
        positionDropdown = findViewById(R.id.positionSpinner);
        register = findViewById(R.id.registerButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.positions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        positionDropdown.setAdapter(adapter);
        positionDropdown.setPrompt("Pick Position");

        FirebaseAuth auth =  FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            OnAddPlayerListener listener = new OnAddPlayerListener() {
                @Override
                public void onPlayerAdded(Boolean added) {
                    if (Boolean.TRUE.equals(added)) {
                        Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(RegisterActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            String email = emailText.getText().toString();
            String password = passText.getText().toString();
            String confirmPassword = confPassText.getText().toString();
            String countryName = cpp.getTvCountryInfo().getText().toString();
            String prefPos = positionDropdown.getSelectedItem().toString();
            String username = userName.getText().toString();
            String number = numberText.getText().toString();
            int numberInt = 0;
            if (number.length() > 0)
            {
                numberInt = Integer.parseInt(number);
            }

            Matcher matcher = pattern.matcher(email);

            if(username.matches("")
                    || password.matches("")
                    || confirmPassword.matches("")
                    || email.matches("")
                    || number.matches("")){
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else if (1 > numberInt || numberInt > 99)
            {
                Toast.makeText(getApplicationContext(), "Number is out of range. Only numbers 1 - 99 are allowed", Toast.LENGTH_SHORT).show();
            }
            else if (username.length() > 12)
            {
                Toast.makeText(getApplicationContext(), "Username too long. Max length 12.", Toast.LENGTH_SHORT).show();
            }
            else{
                if(password.equals(confirmPassword) && matcher.matches()){
                    Player player = new Player.PlayerBuilder()
                            .setPosition(prefPos)
                            .setNationality(countryName)
                            .setName(username)
                            .setNumber(Integer.parseInt(number))
                            .setIsManager(false)
                            .setId(new Date().getTime()+"")
                            .setTeamId("0")
                            .build();
                    FirestoreImpl firestore = new FirestoreImpl();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            player.setUuid(task.getResult().getUser().getUid());
                            firestore.addPlayer(listener, player);
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("RegisterActivity", task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Passwords do not match or email is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
