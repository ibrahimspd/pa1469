package com.example.myapplication.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.databinding.FragmentTeamInfoBinding;
import com.example.myapplication.entites.Player;

public class ProfileFragment  extends Fragment {

    private FragmentProfileBinding binding;

    private Player player;

    private MainActivity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainActivity = (MainActivity) getActivity();

        player = mainActivity.getPlayer();

       binding.usernameTextView.setText(player.getName());


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
