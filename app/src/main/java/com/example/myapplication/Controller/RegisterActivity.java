package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.entites.Player;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryPickerView;

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

<<<<<<< Updated upstream
        Spinner spinner = findViewById(R.id.positionSpinner);
=======

>>>>>>> Stashed changes

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.positionSpinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt("Pick Position");

<<<<<<< Updated upstream
=======
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

                System.out.println("L" + countryName + "L");
                System.out.println("L" + countryName + "L");
                System.out.println(countryName);
                System.out.println(countryName);
                System.out.println(Boolean.toString(countryName != "Country") );
                System.out.println(Boolean.toString(countryName != "Country") );
                Player player = new Player.PlayerBuilder()
                        .setPostion(prefPos)
                        .setNationality(countryName)
                        .setName(username)
                        .build();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirestoreImpl firestore = new FirestoreImpl(db);
                firestore.addPlayer(player);
                Authentication auth = new Authentication();
                auth.createUser(emailText.getText().toString(), passText.getText().toString());
                finish();
            
            }else{
                Toast.makeText(getApplicationContext(), "Passwords do not match or email is invalid", Toast.LENGTH_SHORT).show();
            }
            }





        });
>>>>>>> Stashed changes
    }
}
