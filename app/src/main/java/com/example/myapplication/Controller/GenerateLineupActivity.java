package com.example.myapplication.Controller;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.AutocompleteAdapter;
import com.example.myapplication.Model.PlayerItem;
import com.example.myapplication.Model.Utils;
import com.example.myapplication.R;
import com.example.myapplication.databinding.DrawerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GenerateLineupActivity extends AppCompatActivity {

    private static final int TEAM_INFO = R.id.teamInfo;
    private static final int GENERATE_LINEUP = R.id.generateLineup;
    private static final int PLAYERS = R.id.players;

    private final static Gson gson = new Gson();

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerBinding binding;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    AutoCompleteTextView player1;

    List<Integer> positionInputFieldsIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_lineup);
        Context context = this;

        List<PlayerItem> playerItems = new ArrayList<>();
        playerItems.add(new PlayerItem("Hazel", "https://cdn.discordapp.com/avatars/228939978539925505/57cf0b329687050d057b346ec29ff5e1.webp", "GK"));
        playerItems.add(new PlayerItem("Polo", "https://cdn.discordapp.com/avatars/123908079883386885/54c30e622c4633070977c41986b91aba.webp", "CM"));
        playerItems.add(new PlayerItem("Hytos", "https://cdn.discordapp.com/avatars/236188492621807616/8d298266b9543614609afcf608c7bf17.webp", "ST"));
        playerItems.add(new PlayerItem("Guss", "https://cdn.discordapp.com/avatars/381365555283755009/a_8c25d9d99338bb3c0c8735210ddafda4.webp", "CB"));
        playerItems.add(new PlayerItem("Sn1k3", "https://cdn.discordapp.com/avatars/169091903520899072/f69af5ee53480d5cc42c7d16348efcb1.webp", "LB"));

        positionInputFieldsIds.add(R.id.positionInput);
        positionInputFieldsIds.add(R.id.positionInput2);
        positionInputFieldsIds.add(R.id.positionInput3);
        positionInputFieldsIds.add(R.id.positionInput4);
        positionInputFieldsIds.add(R.id.positionInput5);
        positionInputFieldsIds.add(R.id.positionInput6);
        positionInputFieldsIds.add(R.id.positionInput7);
        positionInputFieldsIds.add(R.id.positionInput8);
        positionInputFieldsIds.add(R.id.positionInput9);
        positionInputFieldsIds.add(R.id.positionInput10);
        positionInputFieldsIds.add(R.id.positionInput11);

        addAutocomplete(playerItems, context);
        setHints("352");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(GENERATE_LINEUP);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case TEAM_INFO:
                    startActivity(new Intent(getApplicationContext(), TeamInfo.class));
                    overridePendingTransition(0, 0);
                    return true;
                case GENERATE_LINEUP:
                    return true;
                case PLAYERS:
                    startActivity(new Intent(getApplicationContext(), PlayersActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                default:
                    return false;
            }
        });

        Spinner formationPicker = findViewById(R.id.formationSpinner);

        formationPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setHints(formationPicker.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.formations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        formationPicker.setAdapter(adapter);

        Button generateLineupButton = findViewById(GENERATE_LINEUP);
        generateLineupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseUrl = "http://78.141.233.225:4545";

                Spinner spinner = findViewById(R.id.formationSpinner);
                String formation = spinner.getSelectedItem().toString();

                String url = baseUrl + "/lineup.png?formation=" + formation;

                for (int i = 0; i < positionInputFieldsIds.size(); i++) {
                    url += addPlayerUrl(positionInputFieldsIds.get(i),i +1);
                }
                
                Glide.with(context).load(url).into((ImageView) findViewById(R.id.lineupImageView));
            }
        });

        Button downloadLineupButton = findViewById(R.id.goBackButton);
        downloadLineupButton.setOnClickListener(view -> {
            Bitmap bitmap = null;
            ImageView imageView = findViewById(R.id.lineupImageView);
            imageView.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
            try {
                saveImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button shareLineupButton = findViewById(R.id.shareLineup);
        shareLineupButton.setOnClickListener(view -> {
            ImageView imageView = findViewById(R.id.lineupImageView);
            Drawable mDrawable = imageView.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share Image"));
        });

    }

    private void addAutocomplete(List<PlayerItem> playerItems, Context context) {
        for (int i = 0; i < positionInputFieldsIds.size(); i++) {
            AutocompleteAdapter adapter = new AutocompleteAdapter(context, playerItems);

            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(positionInputFieldsIds.get(i));
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());
        }

    }

    private void saveImage(Bitmap bitmap) throws IOException {
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Lineup");
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

            File image = new File(imagesDir, "Lineup" + ".png");
            fos = new FileOutputStream(image);

        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        Toast toast = Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT);
        toast.show();

        fos.flush();
        fos.close();
    }

    public String addPlayerUrl(int id, Integer index){
        AutoCompleteTextView editText = findViewById(id);
        String player = editText.getText().toString();
        if (player.equals("")){
            player = "Bot";
        }
        return "&player" + index.toString() + "=" + player;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setHints(String formationStr) {
        String json = Utils.getJsonFromAssets(this, "formations.json");
        Map<String, List<String>> formation = gson.fromJson(json,  new TypeToken<Map<String, List<String>>>(){}.getType());;
        for (int i = 0; i < positionInputFieldsIds.size(); i++) {
            AutoCompleteTextView autoCompleteTextView = findViewById(positionInputFieldsIds.get(i));
            autoCompleteTextView.setHint(formation.get(formationStr).get(i));
        }
    }
}
