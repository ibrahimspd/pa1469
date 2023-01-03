package com.example.myapplication.model;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entites.Invitations;
import com.example.myapplication.entites.Player;

import java.util.List;

public class InvitationListAdapter extends RecyclerView.Adapter<InvitationListAdapter.ViewHolder>{

    private final Context context;
    private final List<Invitations> playerArrayList;

    public InvitationListAdapter(Context context, List<Invitations> playerArrayList) {
        this.context = context;
        this.playerArrayList = playerArrayList;
    }

    @NonNull
    @Override
    public InvitationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invatations_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationListAdapter.ViewHolder holder, int position) {
        holder.username.setText(playerArrayList.get(holder.getAdapterPosition()).getTeamId());
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;

        Button acceptButton;
        Button declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.invatationsText);
            acceptButton = itemView.findViewById(R.id.acceptInvatationButton);
            declineButton = itemView.findViewById(R.id.deleteAccountButton);
        }
    }
}
