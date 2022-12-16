package com.example.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.PlayerInfoListAdapter;
import com.example.myapplication.R;
import com.example.myapplication.entites.Player;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity {

    private static final int GENERATE_LINEUP = R.id.generateLineup;
    private static final int PLAYERS = R.id.players;
    private static final int TEAM_INFO = R.id.teamInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        RecyclerView courseRV = findViewById(R.id.recycler_view);

        ArrayList<Player> playerArrayList = new ArrayList<Player>();
        /*playerArrayList.add(new Player("Hazel", "GK", R.drawable.default_lineup));
        playerArrayList.add(new Player("Hytos", "ST", R.drawable.test_logo));
        playerArrayList.add(new Player("Sn1k3", "LB", R.drawable.test_logo));
        playerArrayList.add(new Player("Guss", "CB", R.drawable.test_logo));
        playerArrayList.add(new Player("Polo", "CM", R.drawable.test_logo));*/
        playerArrayList.add(new Player.PlayerBuilder().setName("Neuer").setPostion("GK").setAvatar("https://cdn.futbin.com/content/fifa23/img/players/167495.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Walker").setPostion("RB").setAvatar("https://cdn.futbin.com/content/fifa23/img/players/188377.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Varane").setPostion("CB").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/201535.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Van Dijk").setPostion( "CB").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/203376.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Hernandez").setPostion( "LB").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/232656.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Kimmich").setPostion("CM").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/212622.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Pogba").setPostion("CM").setPostion( "https://cdn.futbin.com/content/fifa23/img/players/195864.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("De Jong").setPostion("CM").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/228702.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Messi").setPostion( "RW").setPostion( "https://cdn.futbin.com/content/fifa23/img/players/158023.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Kane").setPostion("ST").setPostion("https://cdn.futbin.com/content/fifa23/img/players/202126.png?v=23").build());
        playerArrayList.add(new Player.PlayerBuilder().setName("Neymar").setPostion("LW").setAvatar( "https://cdn.futbin.com/content/fifa23/img/players/190871.png?v=23").build());

        PlayerInfoListAdapter courseAdapter = new PlayerInfoListAdapter(this, playerArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(PLAYERS);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case TEAM_INFO:
                    startActivity(new Intent(getApplicationContext(), TeamInfo.class));
                    overridePendingTransition(0, 0);
                    return true;
                case GENERATE_LINEUP:
                    startActivity(new Intent(getApplicationContext(), GenerateLineupActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case PLAYERS:
                    return true;
            }
            return false;
        });
    }
}
