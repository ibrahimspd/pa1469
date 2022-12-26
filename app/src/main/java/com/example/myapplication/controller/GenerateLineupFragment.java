package com.example.myapplication.controller;

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Model.AutocompleteAdapter;
import com.example.myapplication.Model.PlayerItem;
import com.example.myapplication.Model.Utils;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGenerateLineupBinding;
import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.example.myapplication.Model.GenerateLineupModel;
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

public class GenerateLineupFragment extends Fragment {

    private FragmentGenerateLineupBinding binding;

    private Team team;
    private static final Gson gson = new Gson();
    private Context context;

    private MainActivity mainActivity;

    List<AutoCompleteTextView> positionInputFieldsIds = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GenerateLineupModel generateLineupModel =
                new ViewModelProvider(this).get(GenerateLineupModel.class);

        binding = FragmentGenerateLineupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext();

        mainActivity = (MainActivity) getActivity();

        positionInputFieldsIds.clear();
        positionInputFieldsIds.add(binding.positionInput);
        positionInputFieldsIds.add(binding.positionInput2);
        positionInputFieldsIds.add(binding.positionInput3);
        positionInputFieldsIds.add(binding.positionInput4);
        positionInputFieldsIds.add(binding.positionInput5);
        positionInputFieldsIds.add(binding.positionInput6);
        positionInputFieldsIds.add(binding.positionInput7);
        positionInputFieldsIds.add(binding.positionInput8);
        positionInputFieldsIds.add(binding.positionInput9);
        positionInputFieldsIds.add(binding.positionInput10);
        positionInputFieldsIds.add(binding.positionInput11);

        team = mainActivity.getTeam();

        File imgFile = new File(context.getFilesDir(),  "lineup.png");
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.lineupImageView.setImageBitmap(myBitmap);
        }else
            createLineup();

        loadPlayerDropdown();

        Spinner formationPicker = binding.formationSpinner;

        formationPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setHints(formationPicker.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // TODO document why this method is empty
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(),
                R.array.formations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        formationPicker.setAdapter(adapter);

        Button generateLineupButton = binding.generateLineup;
        generateLineupButton.setOnClickListener(view -> {
            team = mainActivity.getTeam();
            createLineup();
        });

        Button downloadLineupButton = binding.goBackButton;
        downloadLineupButton.setOnClickListener(view -> {
            Bitmap bitmap = null;
            ImageView imageView = binding.lineupImageView;
            imageView.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
            try {
                saveImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button shareLineupButton = binding.shareLineup;
        shareLineupButton.setOnClickListener(view -> {
            ImageView imageView = binding.lineupImageView;
            Drawable mDrawable = imageView.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, "Image Description", null);
            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share Image"));
        });
        team = mainActivity.getTeam();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createLineup() {
        if(team == null)
            return;
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");

            List<Player> players = new ArrayList<>();
            for (AutoCompleteTextView positionInputField : positionInputFieldsIds) {
                players.add(addPlayerFromInputFieldById(positionInputField));
            }

            ImageView imageView = binding.lineupImageView;
            Lineup lineup = new Lineup(players, team, "352");
            String json = gson.toJson(lineup);
            RequestBody body = RequestBody.create(mediaType,json);
            Request request = new Request.Builder()
                    .url("http://78.141.233.225:4545/lineup")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // save image in app data
                // getFilesDir() returns the path to the app data folder
                File file = new File(context.getFilesDir(), "lineup.png");
                FileOutputStream fos = new FileOutputStream(file);
                // store to getFilesDir() folder
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                Activity activity = getActivity();
                if(activity != null) {
                    activity.runOnUiThread(() -> imageView.setImageBitmap(bitmap));
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        addAutocomplete(playerItems, binding.getRoot().getContext());
        setHints("352");
    }

    private void addAutocomplete(List<PlayerItem> playerItems, Context context) {
        for (int i = 0; i < positionInputFieldsIds.size(); i++) {
            AutocompleteAdapter adapter = new AutocompleteAdapter(context, playerItems);
            AutoCompleteTextView autoCompleteTextView = positionInputFieldsIds.get(i);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());
        }
    }

    private void saveImage(Bitmap bitmap) throws IOException {
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getActivity().getContentResolver();
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

        Toast toast = Toast.makeText(binding.getRoot().getContext(), "Image saved", Toast.LENGTH_SHORT);
        toast.show();

        fos.flush();
        fos.close();
    }

    public Player addPlayerFromInputFieldById(AutoCompleteTextView editText){
        String playerName = editText.getText().toString();
        if (playerName.equals("")){
            playerName = "Bot";
        }
        return new Player.PlayerBuilder().setName(playerName).setPosition(editText.getHint().toString())
                .setAvatar("https://media.discordapp.net/attachments/987416205507833867/1001913008915742781/uknoiwkn_player.png")
                .setNumber(1)
                .setNationality("SE")
                .setIsManager(false)
                .setTeam(1)
                .build();
    }

    public void setHints(String formationStr) {
        String json = Utils.getJsonFromAssets(binding.getRoot().getContext(), "formations.json");
        Map<String, List<String>> formation = gson.fromJson(json,  new TypeToken<Map<String, List<String>>>(){}.getType());
        if(formation != null) {
            List<String> positions = formation.get(formationStr);
            for (int i = 0; i < positionInputFieldsIds.size(); i++) {
                AutoCompleteTextView autoCompleteTextView = positionInputFieldsIds.get(i);
                assert positions != null;
                String hint = positions.get(i);
                autoCompleteTextView.setHint(hint);
            }
        }
    }
}
