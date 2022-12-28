package com.example.myapplication.controller;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.databinding.FragmentTeamInfoBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.example.myapplication.model.TeamInfoModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class TeamInfoFragment extends Fragment {

    private Context context;

    private TextView teamName;
    private TextView manager;
    private TextView language;
    private ImageView teamLogo;
    private Spinner fontPicker;
    private ImageView teamBackground;
    private ImageView teamKit;
    private ImageView teamGkKit;
    private ImageView mainColor;
    private ImageView secondaryColor;
    private ImageView fontColor;

    private Team team;
    private Player player;
    private Team updatedTeam;

    private final FirestoreImpl firestore = new FirestoreImpl();

    private ActivityResultLauncher<Intent> colorPickerLauncher;
    private ActivityResultLauncher<Intent> kitGalleryLauncher;
    private ActivityResultLauncher<Intent> gkKitGalleryLauncher;
    private ActivityResultLauncher<Intent> logoGalleryLauncher;
    private ActivityResultLauncher<Intent> backgroundGalleryLauncher;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private FragmentTeamInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TeamInfoModel homeViewModel =
                new ViewModelProvider(this).get(TeamInfoModel.class);

        MainActivity mainActivity = (MainActivity) getActivity();

        team = mainActivity.getTeam();
        player = mainActivity.getPlayer();

        binding = FragmentTeamInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        teamName = binding.teamName;
        manager = binding.manager;
        language = binding.language;
        teamLogo = binding.teamLogo;
        teamBackground = binding.teamBackground;
        teamGkKit = binding.teamgKkit;
        teamKit = binding.teamKit;
        mainColor = binding.mainColor;
        secondaryColor = binding.secondaryColor;
        fontColor = binding.fontColor;
        Button saveButton = binding.saveButton;
        Button createTeamButton = binding.createTeamButton;

        fontPicker = binding.fontDropDown;

        if(team != null)
            displayTeamInfo();
        else
            createTeamButton.setVisibility(View.VISIBLE);

        createTeamButton.setOnClickListener(view -> {
            String teamName;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Title");

// Set up the input
            final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();
                    System.out.println(m_Text);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            team = new Team.TeamBuilder()
                    .setTeamId(new Date().getTime() + "")
                    .setName("New Team")
                    .setLanguage("en")
                    .setKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                    .setGkKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                    .setMainColor("#648c2e")
                    .setSecondaryColor("#000000")
                    .setTeamLogo("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                    .setBackground("https://media.discordapp.net/attachments/996135352240717838/996156253472567336/output.png")
                    .setLineupStyle("4-4-2")
                    .setFontColor("#ffffff")
                    .setFont("rajdhani-bold")
                    .setManagerId(player.getId())
                    .build();
            OnAddTeamListener listener = new OnAddTeamListener() {
                @Override
                public void onTeamFilled(Boolean added) {
                    if (added) {
                        Log.d(TAG, "onTeamFilled: " + team.getName());
                        displayTeamInfo();
                    }
                    else
                        Log.d(TAG, "onTeamFilled: " + "Team not added");
                }

                @Override
                public void onError(Exception exception) {
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            };
            firestore.addTeam(listener, team);
        });

        saveButton.setOnClickListener(v -> firestore.updateTeam(updatedTeam));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.fonts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fontPicker.setAdapter(adapter);

        backgroundGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            getActivity();
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    StorageReference storageRef = storage.getReference();
                    StorageReference mountainsRef = storageRef.child(team.getName() + "_background");

                    updatedTeam.setBackground(team.getName() + "_background");

                    Glide.with(context).load(data.getData()).into(teamBackground);
                    teamBackground.setDrawingCacheEnabled(true);
                    teamBackground.buildDrawingCache();

                    Uri uri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = mountainsRef.putBytes(data2);
                    uploadTask
                            .addOnFailureListener(exception -> 
                                    Toast.makeText(mainActivity, "Image could not save", Toast.LENGTH_SHORT).show())
                            .addOnSuccessListener(taskSnapshot -> 
                                    Toast.makeText(mainActivity, "Image saved", Toast.LENGTH_SHORT).show());
                }
            }
        });

        kitGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    StorageReference storageRef = storage.getReference();
                    StorageReference mountainsRef = storageRef.child(team.getName() + "_kit");
                    updatedTeam.setKit(team.getName() + "_kit");

                    Glide.with(context).load(data.getData()).into(teamKit);
                    teamKit.setDrawingCacheEnabled(true);
                    teamKit.buildDrawingCache();

                    Uri uri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = mountainsRef.putBytes(data2);
                    uploadTask
                            .addOnFailureListener(exception ->
                                    Toast.makeText(mainActivity, "Image could not save", Toast.LENGTH_SHORT).show())
                            .addOnSuccessListener(taskSnapshot ->
                                    Toast.makeText(mainActivity, "Image saved", Toast.LENGTH_SHORT).show());
                }
            }
        });

        gkKitGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    StorageReference storageRef = storage.getReference();
                    StorageReference mountainsRef = storageRef.child(team.getName() + "_gkKit");
                    updatedTeam.setGkKit(team.getName() + "_gkKit");

                    Glide.with(context).load(data.getData()).into(teamGkKit);
                    teamGkKit.setDrawingCacheEnabled(true);
                    teamGkKit.buildDrawingCache();

                    Uri uri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = mountainsRef.putBytes(data2);
                    uploadTask
                            .addOnFailureListener(exception ->
                                    Toast.makeText(mainActivity, "Image could not save", Toast.LENGTH_SHORT).show())
                            .addOnSuccessListener(taskSnapshot ->
                                    Toast.makeText(mainActivity, "Image saved", Toast.LENGTH_SHORT).show());
                }
            }
        });

        logoGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    StorageReference storageRef = storage.getReference();
                    StorageReference mountainsRef = storageRef.child(team.getName() + "_logo");
                    updatedTeam.setTeamLogo(team.getName() + "_logo");

                    Glide.with(context).load(data.getData()).into(teamLogo);
                    teamLogo.setDrawingCacheEnabled(true);
                    teamLogo.buildDrawingCache();

                    Uri uri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = mountainsRef.putBytes(data2);
                    uploadTask
                            .addOnFailureListener(exception ->
                                    Toast.makeText(mainActivity, "Image could not save", Toast.LENGTH_SHORT).show())
                            .addOnSuccessListener(taskSnapshot ->
                                    Toast.makeText(mainActivity, "Image saved", Toast.LENGTH_SHORT).show());
                }
            }
        });

        teamBackground.setOnClickListener(v -> {
            Intent photoPickerIntent = getPhotoPickerIntent("background");
            backgroundGalleryLauncher.launch(photoPickerIntent);
        });

        teamKit.setOnClickListener(v -> {
            Intent photoPickerIntent = getPhotoPickerIntent("kit");
            kitGalleryLauncher.launch(photoPickerIntent);
        });

        teamGkKit.setOnClickListener(v -> {
            Intent photoPickerIntent = getPhotoPickerIntent("gkKit");
            gkKitGalleryLauncher.launch(photoPickerIntent);
        });
        teamLogo.setOnClickListener(v -> {
            Intent photoPickerIntent = getPhotoPickerIntent("logo");
            logoGalleryLauncher.launch(photoPickerIntent);
        });

        colorPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        String colorType = data.getStringExtra("colorType");
                        String hexColor = data.getStringExtra("hexColor");
                        int color = data.getIntExtra("color", 0);
                        if(colorType.equals("mainColor")){
                            mainColor.setBackgroundColor(color);
                            updatedTeam.setMainColor(hexColor);
                        } else if(colorType.equals("secondaryColor")){
                            secondaryColor.setBackgroundColor(color);
                            updatedTeam.setSecondaryColor(hexColor);
                        } else if(colorType.equals("fontColor")){
                            fontColor.setBackgroundColor(color);
                            updatedTeam.setFontColor(hexColor);
                        }
                        Log.d(TAG, "onActivityResult: " + colorType + " " + color);
                    }
                });

        fontPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(updatedTeam != null){
                    updatedTeam.setFont(fontPicker.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        return root;
    }

    @NonNull
    private Intent getPhotoPickerIntent(String kit) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("type", kit);
        return photoPickerIntent;
    }

    public void displayTeamInfo() {
        manager.setText("Manager: " + team.getManagerId());
        teamName.setText("Name: " + team.getName());
        language.setText("Language: " + team.getLanguage());
        fontPicker.setPrompt(team.getFont());
        String teamLogoUrl = team.getTeamLogo();
        if (!teamLogoUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference logoRef = storageRef.child(teamLogoUrl);
            logoRef.getDownloadUrl().addOnSuccessListener(
                    uri -> Glide.with(context).load(uri).into(teamLogo));
        }
        else
            Glide.with(context).load(teamLogoUrl).into(teamLogo);
        String teamKitUrl = team.getKit();
        if (!teamKitUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference kitRef = storageRef.child(teamKitUrl);
            kitRef.getDownloadUrl().addOnSuccessListener(
                    uri -> Glide.with(context).load(uri).into(teamKit));
        }
        else
            Glide.with(context).load(teamKitUrl).into(teamKit);
        String teamGkKitUrl = team.getGkKit() != null ?  team.getGkKit() : team.getKit();
        if (!teamGkKitUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference kitRef = storageRef.child(teamGkKitUrl);
            kitRef.getDownloadUrl().addOnSuccessListener(
                    uri -> Glide.with(context).load(uri).into(teamGkKit));
        }
        else
            Glide.with(context).load(teamGkKitUrl).into(teamKit);

        String teamBackgroundUrl = team.getBackground();
        if (!teamBackgroundUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference backgroundRef = storageRef.child(teamBackgroundUrl);
            backgroundRef.getDownloadUrl().addOnSuccessListener(
                    uri -> Glide.with(context).load(uri).into(teamBackground));
        }
        else
            Glide.with(context).load(teamBackgroundUrl).into(teamBackground);
        mainColor.setBackgroundColor(Color.parseColor(team.getMainColor()));
        mainColor.setOnClickListener(view -> {
            Intent intent = getColorPickerIntent("mainColor", team.getMainColor());
            colorPickerLauncher.launch(intent);
        });

        secondaryColor.setBackgroundColor(Color.parseColor(team.getSecondaryColor()));
        secondaryColor.setOnClickListener(view -> {
            Intent intent = getColorPickerIntent("secondaryColor", team.getSecondaryColor());
            colorPickerLauncher.launch(intent);
        });

        fontColor.setBackgroundColor(Color.parseColor(team.getFontColor()));
        fontColor.setOnClickListener(view -> {
            Intent intent = getColorPickerIntent("fontColor", team.getFontColor());
            colorPickerLauncher.launch(intent);
        });
        binding.coreInfoBox.setVisibility(View.VISIBLE);
        binding.colorBox.setVisibility(View.VISIBLE);
        binding.lineupConfigBox.setVisibility(View.VISIBLE);
        binding.logoBox.setVisibility(View.VISIBLE);
        binding.kitBox.setVisibility(View.VISIBLE);
        binding.gkKitBox.setVisibility(View.VISIBLE);
        binding.backgroundBox.setVisibility(View.VISIBLE);
        binding.saveButton.setVisibility(View.VISIBLE);
    }

    @NonNull
    private Intent getColorPickerIntent(String color, String type) {
        Intent intent = new Intent(context, ColorPicker.class);
        intent.putExtra("colorType", color);
        intent.putExtra("color", type);
        return intent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}