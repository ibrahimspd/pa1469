package com.example.myapplication.model;

import android.app.Activity;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateLineupModel extends ViewModel {

    private final Gson gson = new Gson();

    List<AutoCompleteTextView> inputFields = new ArrayList<>();

    private Activity activity;

    private final Context context;

    public GenerateLineupModel(Context context) {
        this.context = context;
    }

    public void saveImage(ImageView imageView) throws IOException {
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
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
        Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();

        fos.flush();
        fos.close();
    }

    public void createLineup(Team team, String formation, ImageView imageView) throws IOException {
        if(team == null)
            return;
        new Thread(() -> {

            List<Player> players = getPlayerFromInputFields();

            Request request = createLineupRequest(team, formation, players);
            try {
                Response response = getResponse(request);

                Bitmap bitmap = getBitmapFromResponse(response);

                storeBitMapInAppdata(bitmap);

                if(activity != null) {
                    activity.runOnUiThread(() -> imageView.setImageBitmap(bitmap));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Bitmap getBitmapFromResponse(Response response) {
        InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
        return BitmapFactory.decodeStream(inputStream);
    }

    private void storeBitMapInAppdata(Bitmap bitmap) throws IOException {
        File file = new File(context.getFilesDir(), "lineup.png");
        FileOutputStream fos = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }

    @NonNull
    private Response getResponse(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        return client.newCall(request).execute();
    }

    @NonNull
    private Request createLineupRequest(Team team, String formation, List<Player> players) {
        Lineup lineup = new Lineup(players, team, formation);
        String json = gson.toJson(lineup);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,json);
        return new Request.Builder()
                .url("http://78.141.233.225:4545/lineup")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    @NonNull
    private List<Player> getPlayerFromInputFields() {
        List<Player> players = new ArrayList<>();
        for (AutoCompleteTextView positionInputField : inputFields) {
            players.add(addPlayerFromInputField(positionInputField));
        }
        return players;
    }

    public Player addPlayerFromInputField(AutoCompleteTextView editText){
        String playerName = editText.getText().toString();
        if (playerName.equals("")) playerName = "Bot";
        return new Player.PlayerBuilder().setName(playerName).setPosition(editText.getHint().toString())
                .setAvatar("https://media.discordapp.net/attachments/987416205507833867/1001913008915742781/uknoiwkn_player.png")
                .setNumber(1)
                .setNationality("SE")
                .setIsManager(false)
                .setTeam(1)
                .build();
    }

    public void setHints(String formationStr, Context context) {
        String json = Utils.getJsonFromAssets(context, "formations.json");
        Map<String, List<String>> formation = gson.fromJson(json,  new TypeToken<Map<String, List<String>>>(){}.getType());
        if(formation != null) {
            List<String> positions = formation.get(formationStr);
            for (int i = 0; i < inputFields.size(); i++) {
                AutoCompleteTextView autoCompleteTextView = inputFields.get(i);
                assert positions != null;
                String hint = positions.get(i);
                autoCompleteTextView.setHint(hint);
            }
        }
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void addInputFields(AutoCompleteTextView autoCompleteTextView){
        inputFields.add(autoCompleteTextView);
    }

    public void shareImage(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "Lineup", null);
        Uri uri = Uri.parse(path);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(intent, "Share Image"));
    }

    public void loadPlayerDropdown(Context context) {
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
    }

    private void addAutocomplete(List<PlayerItem> playerItems, Context context) {
        for (int i = 0; i < inputFields.size(); i++) {
            AutocompleteAdapter adapter = new AutocompleteAdapter(context, playerItems);
            AutoCompleteTextView autoCompleteTextView = inputFields.get(i);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());
        }
    }
}
