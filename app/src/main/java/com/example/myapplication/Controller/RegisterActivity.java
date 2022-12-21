package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.entites.Player;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Model.Authentication;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryPickerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText;
    EditText userName;
    EditText passText;
    EditText confPassText;
    CountryPickerView cpp;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.textEmail);
        userName = findViewById(R.id.textUsername);
        passText = findViewById(R.id.textPassword);
        confPassText = findViewById(R.id.textConfirmPassword);
        cpp = findViewById(R.id.countryPicker);
        Spinner positionDropdown = findViewById(R.id.positionSpinner);
        register = findViewById(R.id.registerButton);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.positions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        positionDropdown.setAdapter(adapter);
        positionDropdown.setPrompt("Pick Position");

        register.setOnClickListener(view -> {
            String regex = "^(.+)@(.+)$";

            Pattern pattern = Pattern.compile(regex);


            Matcher matcher = pattern.matcher(emailText.getText().toString());
            String countryName = cpp.getTvCountryInfo().getText().toString();
            String prefPos = positionDropdown.getSelectedItem().toString();
            String username = userName.getText().toString();

            if(username.matches("")
                    || passText.getText().toString().matches("")
                    || confPassText.getText().toString().matches("")
                    || emailText.getText().toString().matches("")){

                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }else{
            if(passText.getText().toString().equals(confPassText.getText().toString())
                    && matcher.matches()){

                Player player = new Player.PlayerBuilder()
                        .setPostion(prefPos)
                        .setNationality(countryName)
                        .setName(username)
                        .build();

           
                FirestoreImpl firestore = new FirestoreImpl();
                firestore.addPlayer(player);
                Authentication auth = new Authentication();
                auth.createUser(emailText.getText().toString(), passText.getText().toString());
                finish();
            
            }else{
                Toast.makeText(getApplicationContext(), "Passwords do not match or email is invalid", Toast.LENGTH_SHORT).show();
            }
            }
        });

    }
}
