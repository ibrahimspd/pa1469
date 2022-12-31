package com.example.myapplication.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.database.listeners.team.OnGetMultipleTeamsListener;
import com.example.myapplication.databinding.FragmentAdminBinding;
import com.example.myapplication.entites.Team;

import java.util.List;

public class AdminFragment extends Fragment {

  private FragmentAdminBinding binding;

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

    binding = FragmentAdminBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
