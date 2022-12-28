package com.example.myapplication.controller;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.databinding.FragmentTeamInfoBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.example.myapplication.model.TeamInfoModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private MainActivity activity;

    private TeamInfoModel teamInfoModel;

    private FragmentTeamInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();

        assert activity != null;
        team = activity.getTeam();
        updatedTeam = team;
        player = activity.getPlayer();

        binding = FragmentTeamInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        teamInfoModel = new TeamInfoModel(context, activity);

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

            EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);

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
            String imageName = team.getName() + "_background";
            updatedTeam.setKit(imageName);

            teamInfoModel.handleResult(result, imageName, teamKit);
        });

        kitGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_kit";
            updatedTeam.setKit(imageName);

            teamInfoModel.handleResult(result, imageName, teamKit);
        });

        gkKitGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_gkKit";
            updatedTeam.setGkKit(imageName);

            teamInfoModel.handleResult(result, imageName, teamGkKit);
        });

        logoGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_logo";
            updatedTeam.setTeamLogo(imageName);

            teamInfoModel.handleResult(result, imageName, teamLogo);
        });

        teamBackground.setOnClickListener(v -> launcColorPicker(getPhotoPickerIntent("background"), backgroundGalleryLauncher));

        teamKit.setOnClickListener(v -> launcColorPicker(getPhotoPickerIntent("kit"), kitGalleryLauncher));

        teamGkKit.setOnClickListener(v -> launcColorPicker(getPhotoPickerIntent("gkKit"), gkKitGalleryLauncher));

        teamLogo.setOnClickListener(v -> launcColorPicker(getPhotoPickerIntent("logo"), logoGalleryLauncher));

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

        teamInfoModel.loadImage(team.getTeamLogo(), teamLogo);
        teamInfoModel.loadImage(team.getKit(), teamKit);
        teamInfoModel.loadImage(team.getGkKit() != null ? team.getGkKit() : team.getKit(), teamGkKit);

        loadImage(team.getBackground(), teamBackground, teamBackground);
        mainColor.setBackgroundColor(Color.parseColor(team.getMainColor()));
        mainColor.setOnClickListener(view -> {
            launcColorPicker(getColorPickerIntent("mainColor", team.getMainColor()), colorPickerLauncher);
        });

        secondaryColor.setBackgroundColor(Color.parseColor(team.getSecondaryColor()));
        secondaryColor.setOnClickListener(view -> {
            launcColorPicker(getColorPickerIntent("secondaryColor", team.getSecondaryColor()), colorPickerLauncher);
        });

        fontColor.setBackgroundColor(Color.parseColor(team.getFontColor()));
        fontColor.setOnClickListener(view -> {
            launcColorPicker(getColorPickerIntent("fontColor", team.getFontColor()), colorPickerLauncher);
        });
        makeBoxesVisible();
    }

    private void loadImage(String team, ImageView teamGkKit, ImageView teamKit) {
        String teamGkKitUrl = team;
        if (!teamGkKitUrl.contains(".")){
            loadImageFromFiresbase(teamGkKitUrl, teamGkKit);
        }
        else
            loadImageFromUrl(Glide.with(context).load(teamGkKitUrl), teamKit);
    }

    private void loadImageFromUrl(RequestBuilder<Drawable> context, ImageView teamKit) {
        context.into(teamKit);
    }

    private void loadImageFromFiresbase(String teamKitUrl, ImageView teamKit) {
        StorageReference storageRef = storage.getReference();
        StorageReference kitRef = storageRef.child(teamKitUrl);
        kitRef.getDownloadUrl().addOnSuccessListener(
                uri -> Glide.with(context).load(uri).into(teamKit));
    }

    private void launcColorPicker(Intent team, ActivityResultLauncher<Intent> colorPickerLauncher) {
        Intent intent = team;
        colorPickerLauncher.launch(intent);
    }

    private void makeBoxesVisible() {
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
