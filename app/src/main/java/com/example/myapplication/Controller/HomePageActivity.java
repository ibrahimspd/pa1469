package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class HomePageActivity extends AppCompatActivity {

    private Button LineupButton;
    private Button GenerateLineupButton;
    private Button PayersButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        GenerateLineupButton = (Button) findViewById(R.id.GenerateLineupButton);
        GenerateLineupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGenerateLineupActivity();
            }
        });

        LineupButton = findViewById(R.id.LineupButton);
        LineupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyLineupActivity();
            }
        });
    }
    private void openMyLineupActivity(){
        Intent intent = new Intent(this, MyLineupActivity.class);
        startActivity(intent);
    }

    private void openGenerateLineupActivity(){
        Intent intent = new Intent(this, GenerateLineupActivity.class);
        startActivity(intent);
    }
}