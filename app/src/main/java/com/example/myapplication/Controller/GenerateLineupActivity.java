package com.example.myapplication.Controller;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.AutocompleteAdapter;
import com.example.myapplication.Model.PlayerItem;
import com.example.myapplication.Model.Utils;
import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.OnTeamListener;
import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateLineupActivity extends AppCompatActivity {

    private static final int TEAM_INFO = R.id.teamInfo;
    private static final int GENERATE_LINEUP = R.id.generateLineup;
    private static final int PLAYERS = R.id.players;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Team team;
    private final static Gson gson = new Gson();
    private Context context;

    List<Integer> positionInputFieldsIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_lineup);
        context = this;

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

        FirestoreImpl firestore = new FirestoreImpl(db);
        OnTeamListener teamListener = new OnTeamListener() {
            @Override
            public void onTeamFilled(Team loadedTeam) {
                team = loadedTeam;
            }
            @Override
            public void onError(Exception exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
            }
        };
        firestore.getTeam(teamListener);

        loadPlayerDropdown();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setSelectedItemId(GENERATE_LINEUP);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::goToActivity);

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
                createLineup();
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

    private void createLineup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/json");

                List<Player> players = new ArrayList<>();
                players.add(addPlayerFromInputFieldById(R.id.positionInput,1));
                players.add(addPlayerFromInputFieldById(R.id.positionInput2,2));
                players.add(addPlayerFromInputFieldById(R.id.positionInput3,3));
                players.add(addPlayerFromInputFieldById(R.id.positionInput4, 4));
                players.add(addPlayerFromInputFieldById(R.id.positionInput5,5));
                players.add(addPlayerFromInputFieldById(R.id.positionInput6,6));
                players.add(addPlayerFromInputFieldById(R.id.positionInput7,7));
                players.add(addPlayerFromInputFieldById(R.id.positionInput8,8));
                players.add(addPlayerFromInputFieldById(R.id.positionInput9,9));
                players.add(addPlayerFromInputFieldById(R.id.positionInput10,10));
                players.add(addPlayerFromInputFieldById(R.id.positionInput11,11));

                ImageView imageView = findViewById(R.id.lineupImageView);
                Lineup lineup = new Lineup(players, team, "352");
                Gson gson = new Gson();
                String json = gson.toJson(lineup);
                RequestBody body = RequestBody.create(mediaType,json.toString());
                Request request = new Request.Builder()
                        .url("http://78.141.233.225:4545/lineup")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {

                            imageView.setImageDrawable(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadPlayerDropdown() {
        List<PlayerItem> playerItems = new ArrayList<>();
        playerItems.add(new PlayerItem("Neuer","https://cdn.futbin.com/content/fifa23/img/players/167495.png?v=23", "GK"));
        playerItems.add(new PlayerItem("Walker", "https://cdn.futbin.com/content/fifa23/img/players/188377.png?v=23", "RB"));
        playerItems.add(new PlayerItem("Varane", "https://cdn.futbin.com/content/fifa23/img/players/201535.png?v=23", "CB"));
        playerItems.add(new PlayerItem("Van Dijk", "https://cdn.futbin.com/content/fifa23/img/players/203376.png?v=23", "CB"));
        playerItems.add(new PlayerItem("Hernandez", "https://cdn.futbin.com/content/fifa23/img/players/232656.png?v=23", "LB"));
        playerItems.add(new PlayerItem("Kimmich", "https://cdn.futbin.com/content/fifa23/img/players/212622.png?v=23", "CM"));
        playerItems.add(new PlayerItem("Pogba", "https://cdn.futbin.com/content/fifa23/img/players/195864.png?v=23", "CM"));
        playerItems.add(new PlayerItem("De Jong", "https://cdn.futbin.com/content/fifa23/img/players/228702.png?v=23", "CM"));
        playerItems.add(new PlayerItem("Messi", "https://cdn.futbin.com/content/fifa23/img/players/158023.png?v=23", "RW"));
        playerItems.add(new PlayerItem("Kane", "https://cdn.futbin.com/content/fifa23/img/players/202126.png?v=23", "ST"));
        playerItems.add(new PlayerItem("Neymar", "https://cdn.futbin.com/content/fifa23/img/players/190871.png?v=23", "LW"));
        addAutocomplete(playerItems, context);
        setHints("352");
    }

    private boolean goToActivity(MenuItem item) {
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

    public Player addPlayerFromInputFieldById(int id, Integer index){
        AutoCompleteTextView editText = findViewById(id);
        String playerName = editText.getText().toString();
        if (playerName.equals("")){
            playerName = "Bot";
        }
        return new Player.PlayerBuilder().setName(playerName).setPostion(editText.getHint().toString())
                .setAvatar("https://media.discordapp.net/attachments/987416205507833867/1001913008915742781/uknoiwkn_player.png")
                .setNumber(1)
                .setNationality("SE")
                .setIsManagger(false)
                .setTeam(1)
                .build();
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
