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

        ArrayList<Player> playerArrayList = new ArrayList<>();
        playerArrayList.add(new Player.PlayerBuilder().setName("Neuer").setPosition("GK").setAvatar("https://cdn.futbin.com/content/fifa23/img/players/167495.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Walker").setPosition("RB").setAvatar("https://cdn.futbin.com/content/fifa23/img/players/188377.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Varane").setPosition("CB").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/201535.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Van Dijk").setPosition( "CB").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/203376.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Hernandez").setPosition( "LB").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/232656.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Kimmich").setPosition("CM").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/212622.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Pogba").setPosition("CM").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/195864.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("De Jong").setPosition("CM").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/228702.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Messi").setPosition( "RW").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/158023.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Kane").setPosition("ST").setAvatar("https://cdn.futbin.com/content/fifa23/img/players/202126.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Neymar").setPosition("LW").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/190871.png?v=23").build());

        PlayerInfoListAdapter courseAdapter = new PlayerInfoListAdapter(context, playerArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
