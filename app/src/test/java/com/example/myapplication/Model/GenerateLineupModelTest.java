package com.example.myapplication.Model;
import com.example.myapplication.model.GenerateLineupModel;
import static org.junit.Assert.*;

import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.gson.JsonParser;

import org.junit.Test;

import okhttp3.Request;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class GenerateLineupModelTest {

    @Test
    public void createLineupRequestTest1() {
        GenerateLineupModel generateLineupModel = new GenerateLineupModel(null);
        Team testTeam = new Team.TeamBuilder()
                .setTeamId(new Date().getTime() + "")
                .setName("New Team")
                .setLanguage("en")
                .setKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setGkKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setMainColor("#648c2e")
                .setSecondaryColor("#000000")
                .setTeamLogo("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setBackground("https://media.discordapp.net/attachments/996135352240717838/996156253472567336/output.png")
                .setLineupStyle("4-4-2")
                .setFontColor("#ffffff")
                .setFont("rajdhani-bold")
                .setManagerId("13")
                .build();

        List <Player> playerList = new ArrayList<Player>();
        for (int i = 0; i < 11; i++)
        {
            Player testPlayer = new Player.PlayerBuilder()
                    .setPosition("test")
                    .setNationality("test")
                    .setName("test")
                    .setNumber(1)
                    .setIsManager(false)
                    .setId("test")
                    .setTeamId("0")
                    .build();
            playerList.add(testPlayer);
        }
        String formation = "352";
        Request result = generateLineupModel.createLineupRequest(testTeam, formation, playerList);
        assertNotNull(result);
    }
    @Test
    public void createLineupRequestTest2() {
        GenerateLineupModel generateLineupModel = new GenerateLineupModel(null);
        Team testTeam = new Team.TeamBuilder()
                .setTeamId(new Date().getTime() + "")
                .setName("New Team")
                .setLanguage("en")
                .setKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setGkKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setMainColor("#648c2e")
                .setSecondaryColor("#000000")
                .setTeamLogo("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setBackground("https://media.discordapp.net/attachments/996135352240717838/996156253472567336/output.png")
                .setLineupStyle("4-4-2")
                .setFontColor("#ffffff")
                .setFont("rajdhani-bold")
                .setManagerId("13")
                .build();

        List <Player> playerList = new ArrayList<Player>();
        for (int i = 0; i < 7; i++)
        {
            Player testPlayer = new Player.PlayerBuilder()
                    .setPosition("test")
                    .setNationality("test")
                    .setName("test")
                    .setNumber(1)
                    .setIsManager(false)
                    .setId("test")
                    .setTeamId("0")
                    .build();
            playerList.add(testPlayer);
        }
        String formation = "352";
        Request result = generateLineupModel.createLineupRequest(testTeam, formation, playerList);
        assertNull(result);
    }
    @Test
    public void addPlayerFromInputFieldTest1() {

    }
    @Test
    public void getPlayerFromInputFieldsTest1() {

    }
    @Test
    public void getResponseTest1() {

    }
    @Test
    public void getBitmapFromResponseTest1() {

    }

}