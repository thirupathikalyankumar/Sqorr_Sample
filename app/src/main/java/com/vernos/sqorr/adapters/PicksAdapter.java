package com.vernos.sqorr.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vernos.sqorr.R;
import com.vernos.sqorr.model.Picks;

import java.util.List;


public class PicksAdapter extends RecyclerView.Adapter<PicksAdapter.PlayerHolder> {
    List<Picks> mPlayersStatisticsList;
    Context mContext;

    public PicksAdapter(List<Picks> mPlayersStatisticsList, Context mContext) {
        this.mPlayersStatisticsList = mPlayersStatisticsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PicksAdapter.PlayerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.picks_layout_single, viewGroup, false);
        PicksAdapter.PlayerHolder viewHolder = new PicksAdapter.PlayerHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PicksAdapter.PlayerHolder playerHolder, int i) {
        Picks player_stats = mPlayersStatisticsList.get(i);
        playerHolder.picks_tv.setText(player_stats.getPicks());
        playerHolder.multiplier_tv.setText(player_stats.getMultiplier());
        playerHolder.winpayout_tv.setText(player_stats.getWinpayout());
    }

    @Override
    public int getItemCount() {
        return mPlayersStatisticsList.size();
    }

    public class PlayerHolder extends RecyclerView.ViewHolder {
        TextView picks_tv;
        TextView multiplier_tv;
        TextView winpayout_tv;

        public PlayerHolder(@NonNull View itemView) {
            super(itemView);
            this.picks_tv = (TextView) itemView.findViewById(R.id.picks_tv);
            this.multiplier_tv = (TextView) itemView.findViewById(R.id.multiplier_tv);
            this.winpayout_tv = (TextView) itemView.findViewById(R.id.winpayout_tv);

        }
    }
}
