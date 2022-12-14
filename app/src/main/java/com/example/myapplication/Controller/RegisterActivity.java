package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Model.Authentication;
import com.example.myapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText passText;
    EditText confPassText;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.textEmail);
        passText = findViewById(R.id.textPassword);
        confPassText = findViewById(R.id.textConfirmPassword);
        register = findViewById(R.id.registerButton);

        Spinner positionDropdown = findViewById(R.id.positionSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.positions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        positionDropdown.setAdapter(adapter);
        positionDropdown.setPrompt("Pick Position");

        register.setOnClickListener(view -> {
            String regex = "^(.+)@(.+)$";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(emailText.getText().toString());
            if(passText.getText().toString().equals(confPassText.getText().toString()) && matcher.matches()){
                Authentication auth = new Authentication();
                auth.createUser(emailText.getText().toString(), passText.getText().toString());
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Passwords do not match or email is invalid", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
