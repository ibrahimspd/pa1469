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
import com.hbb20.countrypicker.flagprovider.CPFlagProvider;

import java.util.Date;

public class TeamInfoFragment extends Fragment {

    private Context context;

    private TextView teamName;
    private TextView manager;
    private TextView language;
    private ImageView teamLogo;

    private ImageView teamBackground;
    private ImageView teamKit;
    private ImageView teamGkKit;
    private ImageView mainColorImgView;
    private ImageView secondaryColorImgView;
    private ImageView fontColorImgView;

    private CPFlagProvider languagePicker;

    private Spinner fontPicker;
    private Spinner lineupStylePicker;
    private Spinner kitStylePicker;

    private Team team;
    private Player player;
    private Team updatedTeam;

    private Boolean changed = false;

    private Button saveButton;
    private Button discardButton;

    private final FirestoreImpl firestore = new FirestoreImpl();

    private ActivityResultLauncher<Intent> colorPickerLauncher;
    private ActivityResultLauncher<Intent> kitGalleryLauncher;
    private ActivityResultLauncher<Intent> gkKitGalleryLauncher;
    private ActivityResultLauncher<Intent> logoGalleryLauncher;
    private ActivityResultLauncher<Intent> backgroundGalleryLauncher;

    private MainActivity activity;

    private TeamInfoModel teamInfoModel;

    private static FragmentTeamInfoBinding binding;

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

        saveButton = binding.saveButton;
        saveButton.setOnClickListener(v -> firestore.updateTeam(updatedTeam));

        discardButton = binding.disardButton;
        discardButton.setOnClickListener(v -> {
            updatedTeam = team;
            displayTeamInfo(updatedTeam);
            toggleSaveDiscardButtons(View.INVISIBLE);
        });

        Button createTeamButton = binding.createTeamButton;

        fontPicker = binding.fontDropDown;
        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(context,
                R.array.fonts, android.R.layout.simple_spinner_item);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontPicker.setAdapter(fontAdapter);

        lineupStylePicker = binding.layoutDropDown;
        ArrayAdapter<CharSequence> layoutAdapter = ArrayAdapter.createFromResource(context,
                R.array.lineupStyle, android.R.layout.simple_spinner_item);
        layoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineupStylePicker.setAdapter(layoutAdapter);

        kitStylePicker = binding.kitStyleDropDown;
        ArrayAdapter<CharSequence> kitStyleAdapter = ArrayAdapter.createFromResource(context,
                R.array.kitStyle, android.R.layout.simple_spinner_item);
        kitStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kitStylePicker.setAdapter(kitStyleAdapter);

        if (team != null) {
            displayTeamInfo(team);
            makeBoxesVisible();
        } else
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
                    .setLayout("4-4-2")
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
                        activity.setTeam(team);
                        displayTeamInfo(team);
                        makeBoxesVisible();
                    } else
                        Log.d(TAG, "onTeamFilled: " + "Team not added");
                }

                @Override
                public void onError(Exception exception) {
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            };
            firestore.addTeam(listener, team);

        });

        backgroundGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_background";
            if(!changed){
                this.toggleSaveDiscardButtons(View.VISIBLE);
            }

            updatedTeam.setKit(imageName);

            teamInfoModel.handleResult(result, imageName, teamKit);
        });

        kitGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_kit";
            updatedTeam.setKit(imageName);
            if(!changed){
                this.toggleSaveDiscardButtons(View.VISIBLE);
            }

            teamInfoModel.handleResult(result, imageName, teamKit);
        });

        gkKitGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_gkKit";
            updatedTeam.setGkKit(imageName);
            if(!changed){
                this.toggleSaveDiscardButtons(View.VISIBLE);
            }

            teamInfoModel.handleResult(result, imageName, teamGkKit);
        });

        logoGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = team.getName() + "_logo";
            updatedTeam.setTeamLogo(imageName);
            if(!changed){
                this.toggleSaveDiscardButtons(View.VISIBLE);
            }

            teamInfoModel.handleResult(result, imageName, teamLogo);
        });

        teamBackground.setOnClickListener(v -> {
            Intent intent = getPhotoPickerIntent("background");
            backgroundGalleryLauncher.launch(intent);
        });

        teamKit.setOnClickListener(v -> {
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
                                if(!changed){
                                    this.toggleSaveDiscardButtons(View.VISIBLE);
                                }
                                updatedTeam.setMainColor(hexColor);
                                break;
                            case "secondaryColor":
                                secondaryColorImgView.setBackgroundColor(color);
                                if(!changed){
                                    this.toggleSaveDiscardButtons(View.VISIBLE);
                                }
                                updatedTeam.setSecondaryColor(hexColor);
                                break;
                            case "fontColor":
                                fontColorImgView.setBackgroundColor(color);
                                if(!changed){
                                    this.toggleSaveDiscardButtons(View.VISIBLE);
                                }
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
                if (updatedTeam != null) {
                    if (!changed) {
                        //TeamInfoFragment.toggleSaveDiscardButtons(View.VISIBLE);
                    }
                    updatedTeam.setFont(fontPicker.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // TODO document why this method is empty
            }
        });


        lineupStylePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (updatedTeam != null) {
                    if (!changed) {
                        //TeamInfoFragment.toggleSaveDiscardButtons(View.VISIBLE);
                    }
                    updatedTeam.setLayout(lineupStylePicker.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // TODO document why this method is empty
            }
        });

        kitStylePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (updatedTeam != null) {
                    if (!changed) {
                        //TeamInfoFragment.toggleSaveDiscardButtons(View.VISIBLE);
                    }
                    updatedTeam.setKitStyle(kitStylePicker.getSelectedItem().toString());
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

    public void displayTeamInfo(Team team) {
        manager.setText("Manager: " + team.getManagerId());
        teamName.setText("Name: " + team.getName());
        language.setText("Language: ");
        fontPicker.setPrompt(team.getFont());

        String fontColor = team.getFontColor();
        String mainColor = team.getMainColor();
        String secondaryColor = team.getSecondaryColor();

        if (!teamInfoModel.loadImage(team.getTeamLogo(), teamLogo)
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

    private static void toggleSaveDiscardButtons(int visibility) {
        System.out.println("toggleSaveDiscardButtons");
        binding.saveButton.setVisibility(visibility);
        binding.disardButton.setVisibility(visibility);
    }
}
