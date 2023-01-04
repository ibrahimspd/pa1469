package com.example.myapplication.controller;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentInboxBinding;
import com.example.myapplication.entites.Invitations;
import com.example.myapplication.model.InvitationListAdapter;
import com.example.myapplication.model.PlayerInfoListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InboxFragment extends Fragment {

    FragmentInboxBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        Context context = getContext();
        MainActivity mainActivity = (MainActivity) getActivity();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String currentUid = FirebaseAuth.getInstance().getUid();
        Query query = firestore.collection("invitations");
        Log.d(TAG, "onCreateView: " + currentUid);
        query.whereEqualTo("userId", currentUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    RecyclerView courseRV = binding.recyclerViewInbox;
                    List<Invitations> invitations = task.getResult().toObjects(Invitations.class);
                    InvitationListAdapter invitationListAdapter = new InvitationListAdapter(mainActivity, invitations);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);
                    courseRV.setLayoutManager(linearLayoutManager);
                    courseRV.setAdapter(invitationListAdapter);
                    Toast.makeText(context, "Loaded inbox", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Could not load inbox", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }
}
