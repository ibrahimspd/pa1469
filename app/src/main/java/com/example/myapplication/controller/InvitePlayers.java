package com.example.myapplication.controller;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentInvitePlayersBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.model.PlayerInfoListAdapter;

import java.util.List;


public class InvitePlayers extends Fragment {

    private FragmentInvitePlayersBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInvitePlayersBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        RecyclerView courseRV = binding.recyclerViewInvite;
        Context context = getContext();

        MainActivity mainActivity = (MainActivity) getActivity();
        List<Player> players = mainActivity.getPlayersToInvite();

        PlayerInfoListAdapter courseAdapter = new PlayerInfoListAdapter(context, players, mainActivity.getPlayer());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);

        return root;
    }
}