package com.example.myapplication.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.PlayerInfoListAdapter;
import com.example.myapplication.databinding.FragmentPlayersBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.model.PlayersModel;

import java.util.ArrayList;
import java.util.List;

public class PlayersFragment extends Fragment {

    private FragmentPlayersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PlayersModel slideshowViewModel =
                new ViewModelProvider(this).get(PlayersModel.class);

        binding = FragmentPlayersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView courseRV = binding.recyclerView;

        Context context = getContext();

        MainActivity mainActivity = (MainActivity) getActivity();
        List<Player> players = mainActivity.getPlayers();
        if (players != null) {
            PlayerInfoListAdapter courseAdapter = new PlayerInfoListAdapter(context, players);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

            courseRV.setLayoutManager(linearLayoutManager);
            courseRV.setAdapter(courseAdapter);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
