package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;

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

        Spinner spinner = findViewById(R.id.positionSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.positionSpinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt("Pick Position");

    }
}