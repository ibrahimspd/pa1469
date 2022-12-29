package com.example.myapplication.controller;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.databinding.FragmentTeamInfoBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.example.myapplication.model.TeamInfoModel;

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
    private ImageView mainColorImgView;
    private ImageView secondaryColorImgView;
    private ImageView fontColorImgView;

    private Team team;
    private Player player;
    private Team updatedTeam;

    private final FirestoreImpl firestore = new FirestoreImpl();

    private ActivityResultLauncher<Intent> colorPickerLauncher;
    private ActivityResultLauncher<Intent> kitGalleryLauncher;
    private ActivityResultLauncher<Intent> gkKitGalleryLauncher;
    private ActivityResultLauncher<Intent> logoGalleryLauncher;
    private ActivityResultLauncher<Intent> backgroundGalleryLauncher;

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
        mainColorImgView = binding.mainColor;
        secondaryColorImgView = binding.secondaryColor;
        fontColorImgView = binding.fontColor;

        Button saveButton = binding.saveButton;
        Button createTeamButton = binding.createTeamButton;

        fontPicker = binding.fontDropDown;

        if(team != null) {
            displayTeamInfo();
            makeBoxesVisible();
        }
        else
            binding.createTeamBox.setVisibility(View.VISIBLE);

        createTeamButton.setOnClickListener(view -> {
            String teamName = binding.createTeamInput.getText().toString();

            EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            team = new Team.TeamBuilder()
                    .setTeamId(new Date().getTime() + "")
                    .setName(teamName)
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
                    if (Boolean.TRUE.equals(added)) {
                        binding.createTeamBox.setVisibility(View.GONE);
                        Toast.makeText(context, "Team created successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onTeamFilled: " + team.getName());
                        displayTeamInfo();
                        makeBoxesVisible();
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

        teamBackground.setOnClickListener(v ->{
            Intent intent = getPhotoPickerIntent("background");
            backgroundGalleryLauncher.launch(intent);
        });

        teamKit.setOnClickListener(v ->{
            Intent intent = getPhotoPickerIntent("kit");
            kitGalleryLauncher.launch(intent);
        });

        teamGkKit.setOnClickListener(v -> {
            Intent intent = getPhotoPickerIntent("gkKit");
            gkKitGalleryLauncher.launch(intent);
        });

        teamLogo.setOnClickListener(v -> {
            Intent intent = getPhotoPickerIntent("logo");
            logoGalleryLauncher.launch(intent);
        });

        colorPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String colorType = data.getStringExtra("colorType");
                        String hexColor = data.getStringExtra("hexColor");
                        int color = data.getIntExtra("color", 0);
                        switch (colorType) {
                            case "mainColor":
                                mainColorImgView.setBackgroundColor(color);
                                updatedTeam.setMainColor(hexColor);
                                break;
                            case "secondaryColor":
                                secondaryColorImgView.setBackgroundColor(color);
                                updatedTeam.setSecondaryColor(hexColor);
                                break;
                            case "fontColor":
                                fontColorImgView.setBackgroundColor(color);
                                updatedTeam.setFontColor(hexColor);
                                break;
                            default:
                                Log.d(TAG, "onActivityResult: " + "Color type not found");
                                break;
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
                // TODO document why this method is empty
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

        String fontColor = team.getFontColor();
        String mainColor = team.getMainColor();
        String secondaryColor = team.getSecondaryColor();


        if(!teamInfoModel.loadImage(team.getTeamLogo(), teamLogo)
                || !teamInfoModel.loadImage(team.getKit(), teamKit)
                || !teamInfoModel.loadImage(team.getGkKit() != null ? team.getGkKit() : team.getKit(), teamGkKit)
                || !teamInfoModel.loadImage(team.getBackground(), teamBackground)) {
            displayErrorToast(getString(R.string.loadingImageToImageViewError));
        }

        mainColorImgView.setBackgroundColor(Color.parseColor(mainColor));
        mainColorImgView.setOnClickListener(view -> {
            Intent intent = teamInfoModel.getColorPickerIntent("mainColor", mainColor);
            colorPickerLauncher.launch(intent);
        });

        secondaryColorImgView.setBackgroundColor(Color.parseColor(secondaryColor));
        secondaryColorImgView.setOnClickListener(view -> {
            Intent intent = teamInfoModel.getColorPickerIntent("secondaryColor", secondaryColor);
            colorPickerLauncher.launch(intent);
        });

        fontColorImgView.setBackgroundColor(Color.parseColor(fontColor));
        fontColorImgView.setOnClickListener(view -> {
            Intent intent = teamInfoModel.getColorPickerIntent("fontColor", fontColor);
            colorPickerLauncher.launch(intent);
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void displayErrorToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "displayErrorToast: " + message);
    }
}
