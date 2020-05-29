package com.example.volleyball;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class AdapterGameInfo extends RecyclerView.Adapter<AdapterGameInfo.ViewHolder> {

    private ArrayList<GameInfo> list;

    public AdapterGameInfo(ArrayList<GameInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gameinfo_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameInfo gameInfo = list.get(position);

        holder.date.setText(gameInfo.date);
        holder.team1.setText(gameInfo.firstTeam);
        holder.score.setText(gameInfo.score);
        holder.team2.setText(gameInfo.secondTeam);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date;
        TextView team1;
        TextView score;
        TextView team2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.gameinfo_date);
            team1 = itemView.findViewById(R.id.gameinfo_team1);
            score = itemView.findViewById(R.id.gameinfo_score);
            team2 = itemView.findViewById(R.id.gameinfo_team2);
            date.setOnClickListener(this);
            team1.setOnClickListener(this);
            score.setOnClickListener(this);
            team2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle b = new Bundle();
            Navigation.findNavController(v).navigate(R.id.gamesToGame);
        }
    }
}