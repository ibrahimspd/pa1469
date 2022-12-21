package com.example.myapplication.Model;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entites.Player;

import java.util.ArrayList;

public class PlayerInfoListAdapter extends RecyclerView.Adapter<PlayerInfoListAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<Player> playerArrayList;

    public PlayerInfoListAdapter(Context context, ArrayList<Player> playerArrayList) {
        this.context = context;
        this.playerArrayList = playerArrayList;
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
        holder.position.setText(player.getPostion());
        Glide.with(context).load(player.getAvatar()).into(holder.courseIV);
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView courseIV;
        private final TextView username;
        private final TextView position;

        CardView cardView;
        ImageView arrow;
        Group hiddenGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.imageView2);
            username = itemView.findViewById(R.id.textView14);
            position = itemView.findViewById(R.id.textView3);

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
