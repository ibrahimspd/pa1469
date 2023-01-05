package com.example.myapplication.model;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.RequestBuilder;
import com.example.myapplication.R;
import com.example.myapplication.entites.Invitations;
import com.example.myapplication.entites.Player;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PlayerInfoListAdapter extends RecyclerView.Adapter<PlayerInfoListAdapter.ViewHolder>{

    private final Context context;
    private final List<Player> playerArrayList;
    private final Player currentPlayer;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

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
    public void onBindViewHolder(@NonNull PlayerInfoListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Player player = playerArrayList.get(position);

        holder.username.setText(player.getName());
        holder.position.setText(player.getPosition());
        holder.gamerTag.setText(player.getName());
        holder.nationality.setText(player.getNationality());
        if(player.getAvatar() != null)
            loadImage(player.getAvatar(), holder.profileImage);
        try {
            holder.number.setText(player.getNumber()+ "");
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }

        Glide.with(context).load(player.getAvatar()).into(holder.profileImage);
        if (player.getTeamId().equals("0")){
            holder.button.setImageResource(R.drawable.ic_baseline_add_24);
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            holder.button.setOnClickListener(view -> {
                CollectionReference collectionReference = firestore.collection("invitations");

                Invitations invitations = new Invitations(currentPlayer.getTeamId(), currentPlayer.getUuid(), player.getUuid(), currentPlayer.getName(), player.getName());
                collectionReference.document(player.getUuid()).set(invitations).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        playerArrayList.remove(position);
                        notifyItemRangeChanged(position, playerArrayList.size());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Successfully invited player: " +  player.getName(), Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(context, "Could not invite player: " +  player.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
        else
        {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firestore.collection("players")
                        .document(player.getName()).update("teamId", "0")
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                playerArrayList.remove(position);
                                notifyItemRangeChanged(position, playerArrayList.size());
                                notifyDataSetChanged();
                                Toast.makeText(context, "Successfully removed player from team: " +  player.getName(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Failed to remove player from team: " +  player.getName(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            });
        }
    }

    public boolean loadImage(String imageString, ImageView imageView) {
        if (!imageString.contains(".")){
            loadImageFromFiresbase(imageString, imageView);
        }
        else
            return loadImageFromUrl(Glide.with(context).load(imageString), imageView);
        return true;
    }

    private boolean loadImageFromUrl(RequestBuilder<Drawable> image, ImageView imageView) {
        try {
            image.into(imageView);
        } catch (Exception e) {
            Toast.makeText(this.context, "Error loading image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadImageFromFiresbase(String imageString, ImageView imageView) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imageString);
        imageRef.getDownloadUrl().addOnSuccessListener(
                uri -> Glide.with(context).load(uri).into(imageView));
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profileImage;
        private final TextView username;
        private final TextView position;
        private final TextView nationality;
        private final TextView number;
        private final TextView gamerTag;

        ImageView button;
        CardView cardView;
        ImageView arrow;
        Group hiddenGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nationality = itemView.findViewById(R.id.nationality);
            number = itemView.findViewById(R.id.playerNumber);
            gamerTag = itemView.findViewById(R.id.gamertag);
            profileImage = itemView.findViewById(R.id.imageView2);
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
