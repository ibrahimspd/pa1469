package com.example.myapplication.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.team.OnGetMultipleTeamsListener;
import com.example.myapplication.databinding.FragmentAdminBinding;
import com.example.myapplication.entites.Team;
import com.example.myapplication.model.AdminTeamListAdapter;

import java.util.List;

public class AdminFragment extends Fragment {

    private FragmentAdminBinding binding;

    FirestoreImpl firestore = new FirestoreImpl();

    private List<Team> teams;

    private Context context;

    private RecyclerView teamsRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        teamsRV = binding.recyclerView;

        context = getContext();

        OnGetMultipleTeamsListener listener = new OnGetMultipleTeamsListener() {
            @Override
            public void onTeamsFilled(List<Team> teams) {

                AdminFragment.this.teams = teams;
                Toast.makeText(getContext(), teams.size() + "", Toast.LENGTH_SHORT).show();
                showScreen();
            }

            @Override
            public void onError(Exception exception) {

            }
        };

        firestore.getAllTeams(listener);


        return root;
    }

    public void showScreen() {


        AdminTeamListAdapter courseAdapter = new AdminTeamListAdapter(context, teams);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        teamsRV.setLayoutManager(linearLayoutManager);
        teamsRV.setAdapter(courseAdapter);

        binding.recyclerView.setAdapter(courseAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
