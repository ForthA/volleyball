package com.example.volleyball;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class AdapterRound extends RecyclerView.Adapter<AdapterRound.ViewHolder> {

    private List<Round> list;

    public AdapterRound(List<Round> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Round round = list.get(position);

        holder.n.setText(round.n + "");
        holder.time.setText(round.time + "");
        holder.team1.setText(round.team1 ? "X" : "");
        holder.team2.setText(round.team2 ? "X" : "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView n;
        TextView time;
        TextView team1;
        TextView team2;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            n = itemView.findViewById(R.id.round_n);
            time = itemView.findViewById(R.id.round_time);
            team1 = itemView.findViewById(R.id.round_team1);
            team2 = itemView.findViewById(R.id.round_team2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Round round = list.get(position);

                    Bundle b = new Bundle();
                    b.putLong(RoundFragment.KEY_ROUND_ID, round.id);
                    Navigation.findNavController(itemView).navigate(R.id.gameToRound, b);
                }
            });
        }
    }
}
