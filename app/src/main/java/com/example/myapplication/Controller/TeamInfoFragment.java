package com.example.myapplication.Controller;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.databinding.FragmentTeamInfoBinding;
import com.example.myapplication.entites.Team;
import com.example.myapplication.Model.TeamInfoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TeamInfoFragment extends Fragment {

    private Context context;

    private TextView teamName;
    private TextView manager;
    private TextView language;
    private ImageView teamLogo;
    private Spinner fontPicker;
    private ImageView teamBackground;
    private ImageView teamKit;
    private ImageView mainColor;
    private ImageView secondaryColor;
    private ImageView fontColor;

    private Button saveButton;

    private Team team;

    private Team updatedTeam;

    private final FirestoreImpl firestore = new FirestoreImpl();

    private ActivityResultLauncher<Intent> colorPickerLauncher;
    private ActivityResultLauncher<Intent> kitGalleryLauncher;
    private ActivityResultLauncher<Intent> logoGalleryLauncher;
    private ActivityResultLauncher<Intent> backgroundGalleryLauncher;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private FragmentTeamInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TeamInfoModel homeViewModel =
                new ViewModelProvider(this).get(TeamInfoModel.class);

        binding = FragmentTeamInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        OnGetTeamListener teamListener = new OnGetTeamListener() {
            @Override
            public void onTeamFilled(Team fetchedTeam) {
                team = fetchedTeam;
                updatedTeam = fetchedTeam;
                if(team != null)
                    displayTeamInfo();
            }
            @Override
            public void onError(Exception exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
            }
        };

        teamName = binding.teamName;
        manager = binding.manager;
        language = binding.language;
        teamLogo = binding.teamLogo;
        teamBackground = binding.teamBackground;
        teamKit = binding.teamKit;
        mainColor = binding.mainColor;
        secondaryColor = binding.secondaryColor;
        fontColor = binding.fontColor;
        saveButton = binding.saveButton;

        saveButton.setOnClickListener(v -> {
            firestore.updateTeam(updatedTeam);
        });

        firestore.getTeam(teamListener, "test");

        fontPicker = binding.fontDropDown;

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
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
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
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
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
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
                }
            }
        });

        teamBackground.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("type", "background");
            backgroundGalleryLauncher.launch(photoPickerIntent);
        });

        teamKit.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("type", "kit");
            kitGalleryLauncher.launch(photoPickerIntent);
        });

        teamLogo.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("type", "logo");
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

    public void displayTeamInfo() {
        manager.setText("Manager: " + team.getManager());
        teamName.setText("Name: " + team.getName());
        language.setText("Language: " + team.getLanguage());
        fontPicker.setPrompt(team.getFont());
        String teamLogoUrl = team.getTeamLogo();
        if (!teamLogoUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference logoRef = storageRef.child(teamLogoUrl);
            logoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(teamLogo);
                }
            });
        }
        else
            Glide.with(context).load(teamLogoUrl).into(teamLogo);
        String teamKitUrl = team.getKit();
        if (!teamKitUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference kitRef = storageRef.child(teamKitUrl);
            kitRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(teamKit);
                }
            });
        }
        else
            Glide.with(context).load(teamKitUrl).into(teamKit);
        String teamBackgroundUrl = team.getBackground();
        if (!teamBackgroundUrl.contains(".")){
            StorageReference storageRef = storage.getReference();
            StorageReference backgroundRef = storageRef.child(teamBackgroundUrl);
            backgroundRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(teamBackground);
                }
            });
        }
        else
            Glide.with(context).load(teamBackgroundUrl).into(teamBackground);
        mainColor.setBackgroundColor(Color.parseColor(team.getMainColor()));
        mainColor.setOnClickListener(view -> {
            Intent intent = new Intent(context, ColorPicker.class);
            intent.putExtra("colorType", "mainColor");
            intent.putExtra("color", team.getMainColor());
            colorPickerLauncher.launch(intent);
        });

        secondaryColor.setBackgroundColor(Color.parseColor(team.getSecondaryColor()));
        secondaryColor.setOnClickListener(view -> {
            Intent intent = new Intent(context, ColorPicker.class);
            intent.putExtra("colorType", "secondaryColor");
            intent.putExtra("color", team.getSecondaryColor());
            colorPickerLauncher.launch(intent);
        });

        fontColor.setBackgroundColor(Color.parseColor(team.getFontColor()));

        fontColor.setOnClickListener(view -> {
            Intent intent = new Intent(context, ColorPicker.class);
            intent.putExtra("colorType", "fontColor");
            intent.putExtra("color", team.getFontColor());
            colorPickerLauncher.launch(intent);
        });
        binding.coreInfoBox.setVisibility(View.VISIBLE);
        binding.colorBox.setVisibility(View.VISIBLE);
        binding.lineupConfigBox.setVisibility(View.VISIBLE);
        binding.logoBox.setVisibility(View.VISIBLE);
        binding.kitBox.setVisibility(View.VISIBLE);
        binding.backgroundBox.setVisibility(View.VISIBLE);
        binding.saveButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
