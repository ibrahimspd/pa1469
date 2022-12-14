package com.example.myapplication.model;

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

import com.bumptech.glide.Glide;
import com.example.myapplication.controller.MainActivity;
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
import java.util.HashMap;
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

    Map<String, Player> players = new HashMap<>();

    private MainActivity activity;

    private final Context context;


    public GenerateLineupModel(Context context, MainActivity activity, List<Player> players) {
        this.context = context;
        this.activity = activity;
        if(players != null) {
            for (Player player : activity.getPlayers()) {
                this.players.put(player.getName(), player);
            }
        }
    }

    public void drawLoading(ImageView imageView){
        Glide.with(context).load("https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif").into(imageView);
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

    public Bitmap getBitmapFromResponse(Response response) {
        InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
        return BitmapFactory.decodeStream(inputStream);
    }

    private void storeBitMapInAppdata(Bitmap bitmap) throws IOException {
        if (bitmap != null) {
            File file = new File(context.getFilesDir(), "lineup.png");
            FileOutputStream fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }
    }

    @NonNull
    public Response getResponse(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        return client.newCall(request).execute();
    }

    @NonNull
    public Request createLineupRequest(Team team, String formation, List<Player> players) {
        if (players.size() < 11)
        {
            return null;
        }
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
    public List<Player> getPlayerFromInputFields() {
        List<Player> players = new ArrayList<>();
        for (AutoCompleteTextView positionInputField : inputFields) {
            players.add(addPlayerFromInputField(positionInputField));
        }
        return players;
    }

    public Player addPlayerFromInputField(AutoCompleteTextView editText){
        String playerName = editText.getText().toString();
        if (playerName.equals(""))
            playerName = "Bot";
        else if(players.containsKey(playerName))
            return players.get(playerName);
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

    public void setActivity(MainActivity activity){
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
        List<Player> players = activity.getPlayers();
        if (players != null) {
            for (Player player : players) {
                playerItems.add(playerToPlayerItem(player));
            }
        }
        addAutocomplete(playerItems, context);
    }

    private PlayerItem playerToPlayerItem(Player player){
        return new PlayerItem(player.getName(), player.getAvatar(), player.getPosition(), player.getNationality(), player.getNumber(), player.getName());
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
