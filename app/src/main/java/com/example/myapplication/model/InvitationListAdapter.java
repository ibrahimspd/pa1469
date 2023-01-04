package com.example.myapplication.model;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.entites.Invitations;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class InvitationListAdapter extends RecyclerView.Adapter<InvitationListAdapter.ViewHolder>{

    private final MainActivity context;
    private final List<Invitations> playerArrayList;

    public InvitationListAdapter(MainActivity context, List<Invitations> playerArrayList) {
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
    public void onBindViewHolder(@NonNull InvitationListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Invitations invitations = playerArrayList.get(holder.getAdapterPosition());
            holder.username.setText("Invite from " + invitations.getFromUsername());

            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("players")
                            .document(invitations.getToUsername()).update("teamId", invitations.getTeamId())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                       Query query = FirebaseFirestore.getInstance().collection("teams").whereEqualTo("teamId", invitations.getTeamId());
                                       query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()){
                                                   Team team = task.getResult().toObjects(Team.class).get(0);
                                                   FirebaseFirestore.getInstance().collection("invitations").document(invitations.getUserId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           playerArrayList.remove(position);
                                                           notifyItemRangeChanged(position, playerArrayList.size());
                                                           notifyDataSetChanged();
                                                           context.setTeam(team);
                                                           Toast.makeText(context, "Accepted invite", Toast.LENGTH_SHORT).show();

                                                       }
                                                   });


                                               }
                                           }
                                       });

                                    } else {
                                        Toast.makeText(context, "Failed to accept invite ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });


            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(TAG, "onClick: Decline");
                    FirebaseFirestore.getInstance().collection("invitations").document(invitations.getUserId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Declined invite", Toast.LENGTH_SHORT).show();
                                playerArrayList.remove(position);
                                notifyItemRangeChanged(position, playerArrayList.size());
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "Could not remove invite, check connection", Toast.LENGTH_SHORT).show();
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
        private final TextView username;

        Button acceptButton;
        Button declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.invatationsText);
            acceptButton = itemView.findViewById(R.id.acceptInvatationButton);
            declineButton = itemView.findViewById(R.id.declineInvatationBtn);
        }
    }
}
