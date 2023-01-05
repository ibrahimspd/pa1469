package com.example.myapplication.controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.aboutTxt.setText("This app is developed during a mobile development course. \n\n The data collected and stored is only what the user inputs. The data is stored in firebase database and only used for generating graphics, and is not given out for third-parties. \n\n The app is developed by: \n\n - Robin Hellgen \n - Josef Jakobsson \n - Ibrahim Hazem \n - Emil Wallin \n - Pontus Gustafsson");

        return root;
    }
}
