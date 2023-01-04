package com.example.myapplication.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    OnGetMultipleTeamsListener listener = new OnGetMultipleTeamsListener() {
      @Override
      public void onTeamsFilled(List<Team> teams) {
        AdminFragment.this.teams = teams;
      }

      @Override
      public void onError(Exception exception) {

      }
    };

    firestore.getAllTeams(listener);

    binding = FragmentAdminBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

      RecyclerView courseRV = binding.recyclerView;

      Context context = getContext();

      AdminTeamListAdapter courseAdapter = new AdminTeamListAdapter(context, teams);

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
