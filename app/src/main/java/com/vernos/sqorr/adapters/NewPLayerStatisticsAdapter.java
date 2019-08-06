package com.vernos.sqorr.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vernos.sqorr.R;
import com.vernos.sqorr.model.NewPlayerStatistics;


public class NewPLayerStatisticsAdapter extends RecyclerView.Adapter<NewPLayerStatisticsAdapter.PlayerHolder> {
    NewPlayerStatistics mPlayersStatisticsList;
    Context mContext;

    public NewPLayerStatisticsAdapter(NewPlayerStatistics mPlayersStatisticsList, Context mContext) {
        this.mPlayersStatisticsList = mPlayersStatisticsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.player_stat_single_row, viewGroup, false);
        PlayerHolder viewHolder = new PlayerHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder playerHolder, int i) {
        NewPlayerStatistics mPlayerStts = mPlayersStatisticsList;
        playerHolder.first_player_points.setText(String.format("%2f", Double.parseDouble(mPlayerStts.getFirst_player_points().get(i) + "")));
        if (!mPlayerStts.getSecond_player_points().get(i).equals("000")) {
            playerHolder.second_player_points.setText(String.format("%2f", (Double.parseDouble(mPlayerStts.getSecond_player_points().get(i) + ""))));
        } else {
            playerHolder.second_player_points.setText("-");
        }
        playerHolder.players_positions.setText(mPlayerStts.getPlayer_positions().get(i));
    }

    @Override
    public int getItemCount() {
        return mPlayersStatisticsList.getFirst_player_points().size();
    }


    public class PlayerHolder extends RecyclerView.ViewHolder {
        TextView first_player_points;
        TextView second_player_points;
        TextView players_positions;

        public PlayerHolder(@NonNull View itemView) {
            super(itemView);
            this.first_player_points = (TextView) itemView.findViewById(R.id.first_player_points);
            this.second_player_points = (TextView) itemView.findViewById(R.id.second_player_points);
            this.players_positions = (TextView) itemView.findViewById(R.id.players_positions);

        }
    }
}
