package com.example.myapplication.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GenerateLineupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_lineup);
        Context context = this;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(R.id.generateLineup);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.teamInfo:
                        startActivity(new Intent(getApplicationContext(), TeamInfo.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.generateLineup:
                        return true;
                    case R.id.players:
                        startActivity(new Intent(getApplicationContext(), PlayersActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.formationSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.formations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        Button generateLineupButton = (Button) findViewById(R.id.generateLineup);
        generateLineupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseUrl = "http://78.141.233.225:4545";

                Spinner spinner = (Spinner) findViewById(R.id.formationSpinner);
                String formation = spinner.getSelectedItem().toString();

                String url = baseUrl + "/lineup.png?formation=" + formation;

                url += addPlayerUrl(R.id.positionInput,1);
                url += addPlayerUrl(R.id.positionInput2,2);
                url += addPlayerUrl(R.id.positionInput3,3);
                url += addPlayerUrl(R.id.positionInput4,4);
                url += addPlayerUrl(R.id.positionInput5,5);
                url += addPlayerUrl(R.id.positionInput6,6);
                url += addPlayerUrl(R.id.positionInput7,7);
                url += addPlayerUrl(R.id.positionInput8,8);
                url += addPlayerUrl(R.id.positionInput9,9);
                url += addPlayerUrl(R.id.positionInput10,10);
                url += addPlayerUrl(R.id.positionInput11, 11);
                
                Glide.with(context).load(url).into((ImageView) findViewById(R.id.lineupImageView));
            }
        });

        Button downloadLineupButton = findViewById(R.id.goBackButton);
        downloadLineupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = null;
                ImageView imageView = (ImageView) findViewById(R.id.lineupImageView);
                imageView.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                try {
                    saveImage(bitmap, "Lineup");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "StartingXI");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "StartingXI";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        // create toast
        Toast toast = Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT);
        toast.show();

        fos.flush();
        fos.close();
    }

    public String addPlayerUrl(int id, Integer index){
        EditText editText = (EditText) findViewById(id);
        String player = editText.getText().toString();
        return "&player" + index.toString() + "=" + player;
    }
}
