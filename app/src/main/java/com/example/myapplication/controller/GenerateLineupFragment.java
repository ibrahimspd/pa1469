package com.example.myapplication.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGenerateLineupBinding;
import com.example.myapplication.entites.Team;
import com.example.myapplication.model.GenerateLineupModel;

import java.io.File;
import java.io.IOException;

public class GenerateLineupFragment extends Fragment {

    private FragmentGenerateLineupBinding binding;

    private Team team;
    private Context context;

    private MainActivity mainActivity;

    private Spinner formationDropdown;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGenerateLineupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext();
        GenerateLineupModel generateLineupModel = new GenerateLineupModel(context, (MainActivity) getActivity());

        mainActivity = (MainActivity) getActivity();
        
        generateLineupModel.setActivity(mainActivity);

        formationDropdown = binding.formationSpinner;

        generateLineupModel.addInputFields(binding.positionInput);
        generateLineupModel.addInputFields(binding.positionInput2);
        generateLineupModel.addInputFields(binding.positionInput3);
        generateLineupModel.addInputFields(binding.positionInput4);
        generateLineupModel.addInputFields(binding.positionInput5);
        generateLineupModel.addInputFields(binding.positionInput6);
        generateLineupModel.addInputFields(binding.positionInput7);
        generateLineupModel.addInputFields(binding.positionInput8);
        generateLineupModel.addInputFields(binding.positionInput9);
        generateLineupModel.addInputFields(binding.positionInput10);
        generateLineupModel.addInputFields(binding.positionInput11);

        team = mainActivity.getTeam();

        File imgFile = new File(context.getFilesDir(),  "lineup.png");
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.lineupImageView.setImageBitmap(myBitmap);
        }else {
            try {
                generateLineupModel.createLineup(team, getFormation(), binding.lineupImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        generateLineupModel.loadPlayerDropdown(context);
        generateLineupModel.setHints(getFormation(), context);
        
        formationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                generateLineupModel.setHints(getFormation(), context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("GenerateLineupFragment", "onNothingSelected: Nothing Selected");
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(),
                R.array.formations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        formationDropdown.setAdapter(adapter);

        Button generateLineupButton = binding.generateLineup;
        generateLineupButton.setOnClickListener(view -> {
            Team team =  mainActivity.getTeam();
            String formation = getFormation();
            try {
                generateLineupModel.createLineup(team, formation, binding.lineupImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button downloadLineupButton = binding.goBackButton;
        downloadLineupButton.setOnClickListener(view -> {
            ImageView imageView = binding.lineupImageView;
            imageView.setDrawingCacheEnabled(true);
            try {
                generateLineupModel.saveImage(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button shareLineupButton = binding.shareLineup;
        shareLineupButton.setOnClickListener(view -> {
            generateLineupModel.shareImage(binding.lineupImageView);
        });
        team = mainActivity.getTeam();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
    private String getFormation(){
        if(formationDropdown.getSelectedItem() != null){
            return formationDropdown.getSelectedItem().toString();
        }
        return "352";
    }
}
