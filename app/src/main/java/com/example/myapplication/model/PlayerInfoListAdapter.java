package com.example.myapplication.model;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.entites.Invitations;
import com.example.myapplication.entites.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PlayerInfoListAdapter extends RecyclerView.Adapter<PlayerInfoListAdapter.ViewHolder>{

    private final Context context;
    private final List<Player> playerArrayList;
    private final Player currentPlayer;

    public PlayerInfoListAdapter(Context context, List<Player> playerArrayList, Player currentPlayer) {
        this.context = context;
        this.playerArrayList = playerArrayList;
        this.currentPlayer = currentPlayer;
    }

    @NonNull
    @Override
    public PlayerInfoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_info_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerInfoListAdapter.ViewHolder holder, int position) {
        Player player = playerArrayList.get(position);
        holder.username.setText(player.getName());
        holder.position.setText(player.getPosition());
        Glide.with(context).load(player.getAvatar()).into(holder.courseIV);
        if (player.getTeamId().equals("0")){
            holder.button.setImageResource(R.drawable.ic_baseline_add_24);
        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference collectionReference = firestore.collection("invitations");

                Invitations invitations = new Invitations(currentPlayer.getTeamId(), currentPlayer.getUuid(), player.getUuid());
                collectionReference.document(player.getUuid()).set(invitations).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Successfully invited player: " +  player.getName(), Toast.LENGTH_SHORT).show();
                        }else
                        {
                             Toast.makeText(context, "Could not invite player: " +  player.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView courseIV;
        private final TextView username;
        private final TextView position;
        ImageView button;
        CardView cardView;
        ImageView arrow;
        Group hiddenGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.imageView2);
            username = itemView.findViewById(R.id.textView14);
            position = itemView.findViewById(R.id.textView3);
            button = itemView.findViewById(R.id.imageView3);
            cardView = itemView.findViewById(R.id.player_info_card);
            hiddenGroup = itemView.findViewById(R.id.card_group);
            arrow = itemView.findViewById(R.id.show);

            arrow.setOnClickListener(view -> {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());

                if (hiddenGroup.getVisibility() == View.VISIBLE) {
                    hiddenGroup.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                } else {
                    hiddenGroup.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
            });
        }
    }
}
