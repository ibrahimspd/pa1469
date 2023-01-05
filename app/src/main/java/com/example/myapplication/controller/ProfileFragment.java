package com.example.myapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.myapplication.database.listeners.player.OnUpdatePlayerListener;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.model.ProfileModel;
import com.hbb20.CountryPickerView;

public class ProfileFragment  extends Fragment {

    private static FragmentProfileBinding binding;

    private FirestoreImpl firestore = new FirestoreImpl();

    private Player player;

    private MainActivity mainActivity;

    private ImageView profileImage;

    private TextView nationality;

    private TextView playerPosition;

    private TextView number;


    private Player updatedPlayer;

    private ProfileModel profileModel;

    private Button saveButton;

    private Spinner positionDropdown;


    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileModel = new ProfileModel(getContext(), this.mainActivity);

        profileImage = binding.profileImage;

        mainActivity = (MainActivity) getActivity();

        player = mainActivity.getPlayer();
        updatedPlayer = player;


        if (player != null) {
            nationality =  binding.playerCountryValue;

            binding.profileUsernameValue.setText(player.getName());
            playerPosition = binding.playerPoitionValue;
            number = binding.playerNumberValue;

            setFields(player);

            saveButton = binding.saveButton;
            positionDropdown = binding.profilePositionSpinner;

            ArrayAdapter<CharSequence> kitStyleAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.positions, android.R.layout.simple_spinner_item);
            kitStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            positionDropdown.setAdapter(kitStyleAdapter);

            positionDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String positionSelected = parent.getItemAtPosition(position).toString();
                    if (!positionSelected.equals(player.getPosition())) {
                        updatedPlayer.setPosition(positionSelected);
                        playerPosition.setText(positionSelected);
                        toggleSaveButtons(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            OnUpdatePlayerListener listener = new OnUpdatePlayerListener() {
                @Override
                public void onPlayerAdded(Boolean added) {
                    mainActivity.setPlayer(updatedPlayer);
                    player = updatedPlayer;
                    toggleSaveButtons(View.INVISIBLE);
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(mainActivity, "Could not save profile", Toast.LENGTH_SHORT).show();
                }
            };

            saveButton.setOnClickListener(v ->{
                firestore.updatePlayer(listener,updatedPlayer);
                toggleSaveButtons(View.INVISIBLE);
            });
        }

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            String imageName = player.getName() + "_profileImage";
            this.toggleSaveButtons(View.VISIBLE);

            updatedPlayer.setAvatar(imageName);
            profileModel.handleResult(result, imageName, profileImage);
        });

        profileImage.setOnClickListener(v -> {
            Intent intent = getPhotoPickerIntent("profileImage");
            galleryLauncher.launch(intent);
        });
        return root;
    }

    @NonNull
    private Intent getPhotoPickerIntent(String image) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("type", image);
        return photoPickerIntent;
    }

    private void setFields(Player player) {
        nationality.setText(player.getNationality());
        playerPosition.setText(player.getPosition());
        playerPosition.invalidate();
        playerPosition.requestLayout();
        number.setText(player.getNumber() + "");
    }

    private static void toggleSaveButtons(int visibility) {
        binding.saveButton.setVisibility(visibility);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
