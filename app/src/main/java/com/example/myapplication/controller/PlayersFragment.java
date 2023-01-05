package com.example.myapplication.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.PlayerInfoListAdapter;
import com.example.myapplication.databinding.FragmentPlayersBinding;
import com.example.myapplication.entites.Player;
import com.example.myapplication.model.PlayersModel;

import java.util.List;

public class PlayersFragment extends Fragment {

    private FragmentPlayersBinding binding;
    private InvitePlayers invitePlayers = new InvitePlayers();

    private Button inviteButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPlayersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView playerRV = binding.recyclerView;

        inviteButton = binding.button;

        Context context = getContext();

        MainActivity mainActivity = (MainActivity) getActivity();

        assert mainActivity != null;
        if(!mainActivity.getPlayer().getIsManager()){
            inviteButton.setVisibility(View.INVISIBLE);
        }

        List<Player> players = mainActivity.getPlayers();
        if (players != null) {
            PlayerInfoListAdapter playerInfoListAdapter = new PlayerInfoListAdapter(context, players, mainActivity.getPlayer());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

            playerRV.setLayoutManager(linearLayoutManager);
            playerRV.setAdapter(playerInfoListAdapter);

            inviteButton.setOnClickListener(view ->
                    mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, invitePlayers).commit());
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
