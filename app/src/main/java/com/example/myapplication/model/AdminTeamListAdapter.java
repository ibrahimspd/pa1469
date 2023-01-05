package com.example.myapplication.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.player.OnGetPlayerListener;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;

import java.util.List;

public class AdminTeamListAdapter extends RecyclerView.Adapter<AdminTeamListAdapter.ViewHolder>{

    private final Context context;
    private final List<Team> playerArrayList;

    private final FirestoreImpl firestore = new FirestoreImpl();

    public AdminTeamListAdapter(Context context, List<Team> playerArrayList) {
        this.context = context;
        this.playerArrayList = playerArrayList;
    }

    @NonNull
    @Override
    public AdminTeamListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_team_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTeamListAdapter.ViewHolder holder, int position) {
        Team team = playerArrayList.get(position);
        holder.teamName.setText(team.getName());
        OnGetPlayerListener listener = new OnGetPlayerListener() {
            @Override
            public void onPlayerFilled(Player player) {
                if(player != null){
                    holder.manager.setText(player.getName());
                }
                else {
                    holder.manager.setText("No manager");
                }
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
            }
        };
        firestore.getPlayerById(listener, team.getManagerId());
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView teamName;
        private final TextView manager;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.teamNameValue);
            manager = itemView.findViewById(R.id.managerValue);

            cardView = itemView.findViewById(R.id.player_info_card);
        }
    }
}
