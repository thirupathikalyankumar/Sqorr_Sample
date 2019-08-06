package com.vernos.sqorr.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vernos.sqorr.R;
import com.vernos.sqorr.model.NewPlayerStatistics;
import com.vernos.sqorr.pojos.Matchup;
import com.vernos.sqorr.views.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerListAdapter extends RecyclerView.Adapter<NewPlayerListAdapter.PlayerHolder> {

    Context mContext;
    List<NewPlayerStatistics> mPlayersStatistics = new ArrayList<>();
    boolean IsPurchased;
    private List<Matchup> matchups = new ArrayList<>();
    private List<String> first_player_items = new ArrayList<>();
    private List<String> second_player_items = new ArrayList<>();
    private List<String> matchUp_Ids = new ArrayList<>();
    private List<String> pickIndex_Ids = new ArrayList<>();
    private List<String> over_pickIndex_Ids = new ArrayList<>();
    private List<String> under_pickIndex_Ids = new ArrayList<>();
    private OnItemClick mCallback;

    public NewPlayerListAdapter(List<Matchup> matchups, List<NewPlayerStatistics> mPlayersStatistics, boolean IsPurchased, Context mContext, OnItemClick mCallback) {
        this.mPlayersStatistics = mPlayersStatistics;
        this.mContext = mContext;
        this.IsPurchased = IsPurchased;
        this.matchups = matchups;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_player_card, viewGroup, false);
        PlayerHolder viewHolder = new PlayerHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayerHolder playerHolder, final int position) {

        //Filling the playA details
        if (matchups.get(position).getPlayerA().getFirstName() != null || matchups.get(position).getPlayerA().getLastName() != null) {
            playerHolder.first_player_Name.setText(matchups.get(position).getPlayerA().getFirstName() + " " + matchups.get(position).getPlayerA().getLastName());

        }
        if (matchups.get(position).getPlayerA().getPositonName() != null) {
            playerHolder.first_player_position.setText(matchups.get(position).getPlayerA().getPositonName());
        }
        if (matchups.get(position).getPlayerA().getPointSpread() != null) {
            playerHolder.first_player_price.setText(matchups.get(position).getPlayerA().getPointSpread() + "");
        }
        if (matchups.get(position).getPlayerA().getVenue() != null) {
            playerHolder.first_player_Match.setVisibility(View.VISIBLE);
            playerHolder.first_player_Match.setText(matchups.get(position).getPlayerA().getVenue());
        } else {
            playerHolder.first_player_Match.setVisibility(View.INVISIBLE);
        }
        if (matchups.get(position).getPlayerA().getPlayerImage() != null) {
            Picasso.with(mContext).load(matchups.get(position).getPlayerA().getPlayerImage())
                    .placeholder(R.drawable.game_inactive_placeholder)
                    .transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            return transformImg(source, R.color.hint);
                        }

                        @Override
                        public String key() {
                            return "circle";
                        }
                    })
                    .into(playerHolder.first_player_img);
        }

        //Filling the PlayerB details

        if (matchups.get(position).getPlayerB() == null) {
            playerHolder.second_playerCard.setVisibility(View.GONE);
            playerHolder.no_player.setVisibility(View.VISIBLE);
            playerHolder.first_playerCard.setClickable(false);
        } else {
            playerHolder.second_playerCard.setVisibility(View.VISIBLE);
            playerHolder.no_player.setVisibility(View.GONE);
            playerHolder.first_playerCard.setClickable(true);
            playerHolder.second_player_Name.setText(matchups.get(position).getPlayerB().getFirstName() + " " + matchups.get(position).getPlayerB().getLastName());
            playerHolder.second_player_position.setText(matchups.get(position).getPlayerB().getPositonName());
            playerHolder.second_player_price.setText(matchups.get(position).getPlayerB().getPointSpread() + "");
            if (matchups.get(position).getPlayerB().getVenue() != null) {
                playerHolder.second_player_Match.setVisibility(View.VISIBLE);
                playerHolder.second_player_Match.setText(matchups.get(position).getPlayerB().getVenue());
            } else {
                playerHolder.second_player_Match.setVisibility(View.INVISIBLE);
            }
            //   playerHolder.second_player_Time.setText(player_TWO.getGameDate());
            if (matchups.get(position).getPlayerB().getPlayerImage() != null) {
                Picasso.with(mContext).load(matchups.get(position).getPlayerB().getPlayerImage())
                        .placeholder(R.drawable.game_inactive_placeholder)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                return transformImg(source, R.color.hint);
                            }

                            @Override
                            public String key() {
                                return "circle";
                            }
                        })
                        .into(playerHolder.second_player_img);

            }
        }



/*
        Checking IsPurchased or not, if purchase player cound't clickble else can clickble
*/
        //if IsPurchased true
        /*
Checking picindex and win index of player A and PLayer B
*/
        if (IsPurchased) {
            /*  if winindex=0,pickindex=0 playerA checked Green*/
            if (matchups.get(position).getPickIndex() == 0 && matchups.get(position).getWinIndex() == 0) {
                playerHolder.first_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_win_bg));
                playerHolder.first_playerCheck.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_win));
                playerHolder.win_loss_statusView_ly.setVisibility(View.VISIBLE);
                playerHolder.win_loss_statusView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                playerHolder.statusTxt.setText("WIN");
            }
            /*  if winindex=1,pickindex=1 playerB checked Green*/
            if (matchups.get(position).getPickIndex() == 1 && matchups.get(position).getWinIndex() == 1) {
                playerHolder.second_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_win_bg));
                playerHolder.second_playerCheck.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_win));
                playerHolder.win_loss_statusView_ly.setVisibility(View.VISIBLE);
                playerHolder.win_loss_statusView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                playerHolder.statusTxt.setText("WIN");
            }
            /*  if winindex=0,pickindex=1 or winindex=0,pickindex=-1   playerA checked gray*/
            if ((matchups.get(position).getPickIndex() == 0 && matchups.get(position).getWinIndex() == 1) || (matchups.get(position).getPickIndex() == 0 && matchups.get(position).getWinIndex() == -1)) {
                playerHolder.first_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_lost_bg));
                playerHolder.first_playerCheck.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_loss));
                playerHolder.win_loss_statusView_ly.setVisibility(View.VISIBLE);
                playerHolder.win_loss_statusView.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_new));
                playerHolder.statusTxt.setText("LOST");
            }
            /*  if winindex=1,pickindex=0 or winindex=1,pickindex=-1   playerB checked gray*/
            if (matchups.get(position).getPickIndex() == 1 && matchups.get(position).getWinIndex() == 0) {
                playerHolder.second_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_lost_bg));
                playerHolder.second_playerCheck.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.game_badge_loss));
                playerHolder.win_loss_statusView_ly.setVisibility(View.VISIBLE);
                playerHolder.win_loss_statusView.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_new));
                playerHolder.statusTxt.setText("LOST");
            }

        } else {
            playerHolder.win_loss_statusView_ly.setVisibility(View.GONE);
            playerHolder.first_playerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerHolder.no_player.getVisibility() == View.GONE) {
                        mHilightViews(playerHolder, 1, position);
                    }
                }
            });

            playerHolder.second_playerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHilightViews(playerHolder, 2, position);
                }
            });

            playerHolder.over_playercard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHilightViews(playerHolder, 3, position);
                }
            });

            playerHolder.under_playercard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHilightViews(playerHolder, 4, position);
                }
            });

        }
        playerHolder.playerstatsViewSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHolder.playerStatisticsView.setVisibility(playerHolder.playerStatisticsView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                if (playerHolder.playerStatisticsView.getVisibility() == View.VISIBLE) {
                    playerHolder.arrow.setRotation(180);
                } else {
                    playerHolder.arrow.setRotation(360);
                }
            }
        });

        NewPLayerStatisticsAdapter pLayerStatisticsAdapter = new NewPLayerStatisticsAdapter(mPlayersStatistics.get(position), mContext);
        CustomLinearLayoutManager gridLayoutManager = new CustomLinearLayoutManager(mContext);
        playerHolder.playerStatsRcView_child.setLayoutManager(gridLayoutManager);
        playerHolder.playerStatsRcView_child.setAdapter(pLayerStatisticsAdapter);
    }


    private void mHilightViews(PlayerHolder playerHolder, int from, int position) {
        if (from == 1) {
            if (playerHolder.first_playerCheck.isChecked()) {
                if (matchUp_Ids.size() > 0) {
                    matchUp_Ids.remove(matchups.get(position).get_id());
                }
                if (pickIndex_Ids.size() > 0) {
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                }
                first_player_items.remove(matchups.get(position).getPlayerA().getPlayerId());
                playerHolder.first_playerCheck.setChecked(false);
                setFirstPlayerTextEnableDisable(playerHolder, false);
            } else {
                if (playerHolder.second_playerCheck.isChecked()) {
                    second_player_items.remove(matchups.get(position).getPlayerB().getPlayerId());
                    matchUp_Ids.remove(matchups.get(position).get_id());
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                    playerHolder.second_playerCheck.setChecked(false);
                    setSecondPlayerTextDisableEnableMode(playerHolder, false);
                }
                pickIndex_Ids.add(matchups.get(position).getPickIndex() + "");
                matchUp_Ids.add(matchups.get(position).get_id());
                playerHolder.first_playerCheck.setChecked(true);
                first_player_items.add(matchups.get(position).getPlayerA().getPlayerId());
                setFirstPlayerTextEnableDisable(playerHolder, true);
            }
        } else if (from == 2) {
            if (playerHolder.second_playerCheck.isChecked()) {
                if (matchUp_Ids.size() > 0) {
                    matchUp_Ids.remove(matchups.get(position).get_id());
                }
                if (pickIndex_Ids.size() > 0) {
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                }
                second_player_items.remove(matchups.get(position).getPlayerB().getPlayerId());
                playerHolder.second_playerCheck.setChecked(false);
                setSecondPlayerTextDisableEnableMode(playerHolder, false);
            } else {
                playerHolder.second_playerCheck.setChecked(true);
                if (playerHolder.first_playerCheck.isChecked()) {
                    first_player_items.remove(matchups.get(position).getPlayerA().getPlayerId());
                    playerHolder.first_playerCheck.setChecked(false);
                    matchUp_Ids.remove(matchups.get(position).get_id());
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                    setFirstPlayerTextEnableDisable(playerHolder, false);
                }
                matchUp_Ids.add(matchups.get(position).get_id());
                pickIndex_Ids.add(matchups.get(position).getPickIndex() + "");
                second_player_items.add(matchups.get(position).getPlayerB().getPlayerId());
                setSecondPlayerTextDisableEnableMode(playerHolder, true);
            }
        } else if (from == 3) {
            if (playerHolder.overCheckbox.isChecked()) {
                if (matchUp_Ids.size() > 0) {
                    matchUp_Ids.remove(matchups.get(position).get_id());
                }
                if (pickIndex_Ids.size() > 0) {
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                }
                over_pickIndex_Ids.remove("0");
                playerHolder.overCheckbox.setChecked(false);
                setAPlayerTextDisableEnableMode(playerHolder, false);
            } else {
                playerHolder.overCheckbox.setChecked(true);
                if (playerHolder.underCheckbox.isChecked()) {
                    under_pickIndex_Ids.remove("1");
                    matchUp_Ids.remove(matchups.get(position).get_id());
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                    playerHolder.underCheckbox.setChecked(false);
                    setBPlayerTextDisableEnableMode(playerHolder, false);
                }
                pickIndex_Ids.add(matchups.get(position).getPickIndex() + "");
                matchUp_Ids.add(matchups.get(position).get_id());
                over_pickIndex_Ids.add("0");
                setAPlayerTextDisableEnableMode(playerHolder, true);
            }
        } else if (from == 4) {
            if (playerHolder.underCheckbox.isChecked()) {
                if (matchUp_Ids.size() > 0) {
                    matchUp_Ids.remove(matchups.get(position).get_id());
                }
                if (pickIndex_Ids.size() > 0) {
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                }
                under_pickIndex_Ids.remove("1");
                playerHolder.underCheckbox.setChecked(false);
                setBPlayerTextDisableEnableMode(playerHolder, false);
            } else {
                playerHolder.underCheckbox.setChecked(true);
                if (playerHolder.overCheckbox.isChecked()) {
                    over_pickIndex_Ids.remove("0");
                    matchUp_Ids.remove(matchups.get(position).get_id());
                    pickIndex_Ids.remove(matchups.get(position).getPickIndex() + "");
                    playerHolder.overCheckbox.setChecked(false);
                    setAPlayerTextDisableEnableMode(playerHolder, false);
                }
                matchUp_Ids.add(matchups.get(position).get_id());
                pickIndex_Ids.add(matchups.get(position).getPickIndex() + "");
                under_pickIndex_Ids.add("1");
                setBPlayerTextDisableEnableMode(playerHolder, true);
            }
        }
    }


    private void setAPlayerTextDisableEnableMode(PlayerHolder playerHolder, boolean enable) {
        if (enable) {
            playerHolder.overview_txt.setTextColor(mContext.getResources().getColor(R.color.black));
            playerHolder.overView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_select_bg_circle));
            setTextViewDrawableColor(playerHolder.overview_txt, mContext.getResources().getColor(R.color.black));
        } else {
            playerHolder.overview_txt.setTextColor(mContext.getResources().getColor(R.color.hint));
            playerHolder.overView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_unchecked));
            setTextViewDrawableColor(playerHolder.overview_txt, mContext.getResources().getColor(R.color.hint));
        }
        mCallback.onClick(first_player_items, second_player_items, matchUp_Ids, pickIndex_Ids,over_pickIndex_Ids,under_pickIndex_Ids);
    }


    private void setBPlayerTextDisableEnableMode(PlayerHolder playerHolder, boolean enable) {
        if (enable) {
            playerHolder.underView_txt.setTextColor(mContext.getResources().getColor(R.color.black));
            playerHolder.underView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_select_bg_circle));
            setTextViewDrawableColor(playerHolder.underView_txt, mContext.getResources().getColor(R.color.black));
        } else {
            playerHolder.underView_txt.setTextColor(mContext.getResources().getColor(R.color.hint));
            setTextViewDrawableColor(playerHolder.underView_txt, mContext.getResources().getColor(R.color.hint));
            playerHolder.underView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_unchecked));

        }
        mCallback.onClick(first_player_items, second_player_items, matchUp_Ids, pickIndex_Ids,over_pickIndex_Ids,under_pickIndex_Ids);


    }

    private void setFirstPlayerTextEnableDisable(PlayerHolder playerHolder, boolean enable) {
        if (enable) {
            playerHolder.first_player_Name.setTextColor(mContext.getResources().getColor(R.color.black));
            playerHolder.first_player_position.setTextColor(mContext.getResources().getColor(R.color.black));
            playerHolder.first_player_Match.setTextColor(mContext.getResources().getColor(R.color.black));
            playerHolder.first_player_Time.setTextColor(mContext.getResources().getColor(R.color.black));
            playerHolder.first_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_select_bg_circle));
            setTextViewDrawableColor(playerHolder.underView_txt, mContext.getResources().getColor(R.color.black));

        } else {
            playerHolder.first_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_checked));
            playerHolder.first_player_Name.setTextColor(mContext.getResources().getColor(R.color.hint));
            playerHolder.first_player_position.setTextColor(mContext.getResources().getColor(R.color.hint));
            playerHolder.first_player_Match.setTextColor(mContext.getResources().getColor(R.color.hint));
            playerHolder.first_player_Time.setTextColor(mContext.getResources().getColor(R.color.hint));
        }
        mCallback.onClick(first_player_items, second_player_items, matchUp_Ids, pickIndex_Ids,over_pickIndex_Ids,under_pickIndex_Ids);

    }

    private void setSecondPlayerTextDisableEnableMode(PlayerHolder playerHolder, boolean enable) {
        if (enable) {
            playerHolder.second_player_Name.setTextColor(mContext.getResources().getColor((R.color.black)));
            playerHolder.second_player_position.setTextColor(mContext.getResources().getColor((R.color.black)));
            playerHolder.second_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_select_bg_circle));
            playerHolder.second_player_Match.setTextColor(mContext.getResources().getColor((R.color.black)));
            playerHolder.second_player_Time.setTextColor(mContext.getResources().getColor((R.color.black)));

        } else {
            playerHolder.second_player_Name.setTextColor(mContext.getResources().getColor((R.color.hint)));
            playerHolder.second_player_position.setTextColor(mContext.getResources().getColor((R.color.hint)));
            playerHolder.second_player_Match.setTextColor(mContext.getResources().getColor((R.color.hint)));
            playerHolder.second_player_Time.setTextColor(mContext.getResources().getColor((R.color.hint)));
            playerHolder.second_player_img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.player_checked));

        }
        mCallback.onClick(first_player_items, second_player_items, matchUp_Ids, pickIndex_Ids,over_pickIndex_Ids,under_pickIndex_Ids);


    }


    @Override
    public int getItemCount() {
        return matchups.size();
    }


    public Bitmap transformImg(Bitmap source, int color) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r1 = size / 2f;
        canvas.drawCircle(r1, r1, r1, paint);
        //border code
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mContext.getColor(color));
        paint.setStrokeWidth(1f);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        textView.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClick {
        void onClick(List<String> first_player_list, List<String> second_player_list, List<String> matchUp_Ids, List<String> pickIndex_Ids, List<String> over_pickIndex_Ids, List<String> under_pickIndex_Ids);

    }

    public class PlayerHolder extends RecyclerView.ViewHolder {

        //1St PLayer
        ImageView first_player_img;
        TextView first_player_Name;
        TextView first_player_Time;
        TextView first_player_position;
        TextView first_player_Match;
        Button first_player_price;
        CardView first_playerCard;
        CheckBox first_playerCheck;

        //2nd player
        ImageView second_player_img;
        TextView second_player_Name;
        TextView second_player_Time;
        TextView second_player_position;
        TextView second_player_Match;
        Button second_player_price;
        CardView second_playerCard;
        CheckBox second_playerCheck;

        //Player statistics
        CardView playerstatsViewSpinner;
        LinearLayout playerStatisticsView;
        ImageView arrow;
        RecyclerView playerStatsRcView_child;
        //B player off
        CheckBox overCheckbox;
        CheckBox underCheckbox;
        RelativeLayout under_playercard;
        RelativeLayout over_playercard;
        TextView overview_txt;
        TextView underView_txt;
        LinearLayout overView;
        LinearLayout underView;
        CardView no_player;

        /*win loss status View*/
        LinearLayout win_loss_statusView_ly;
        CardView win_loss_statusView;
        TextView statusTxt;


        public PlayerHolder(@NonNull View itemView) {
            super(itemView);
            // PLAYER A
            this.first_player_img = (ImageView) itemView.findViewById(R.id.player_img);
            this.first_player_Name = (TextView) itemView.findViewById(R.id.player_Name);
            this.first_player_position = (TextView) itemView.findViewById(R.id.player_position);
            this.first_player_Match = (TextView) itemView.findViewById(R.id.player_Match);
            this.first_player_Time = (TextView) itemView.findViewById(R.id.player_time);
            this.first_player_price = (Button) itemView.findViewById(R.id.player_price);
            this.first_playerCard = (CardView) itemView.findViewById(R.id.playerCard);
            this.first_playerCheck = (CheckBox) itemView.findViewById(R.id.playerCheck);

            // PLAYER B
            this.second_player_img = (ImageView) itemView.findViewById(R.id.second_player_img);
            this.second_player_Name = (TextView) itemView.findViewById(R.id.second_player_Name);
            this.second_player_position = (TextView) itemView.findViewById(R.id.second_player_position);
            this.second_player_Match = (TextView) itemView.findViewById(R.id.second_player_Match);
            this.second_player_Time = (TextView) itemView.findViewById(R.id.second_player_time);
            this.second_player_price = (Button) itemView.findViewById(R.id.second_player_price);
            this.second_playerCard = (CardView) itemView.findViewById(R.id.second_playerCard);
            this.second_playerCheck = (CheckBox) itemView.findViewById(R.id.second_playerCheck);

            //Players ststistics
            this.playerstatsViewSpinner = (CardView) itemView.findViewById(R.id.playerstatsViewSpinner);
            this.playerStatisticsView = (LinearLayout) itemView.findViewById(R.id.playerStatisticsView);
            this.arrow = (ImageView) itemView.findViewById(R.id.arrow);
            this.playerStatsRcView_child = (RecyclerView) itemView.findViewById(R.id.playerStatsRcView_child);

            //PlayerB OFF
            this.overCheckbox = (CheckBox) itemView.findViewById(R.id.overCheckbox);
            this.underCheckbox = (CheckBox) itemView.findViewById(R.id.underCheckbox);
            this.over_playercard = (RelativeLayout) itemView.findViewById(R.id.over_playercard);
            this.under_playercard = (RelativeLayout) itemView.findViewById(R.id.under_playercard);
            this.overview_txt = (TextView) itemView.findViewById(R.id.overview_txt);
            this.underView_txt = (TextView) itemView.findViewById(R.id.underView_txt);
            this.overView = (LinearLayout) itemView.findViewById(R.id.overView);
            this.underView = (LinearLayout) itemView.findViewById(R.id.underView);
            this.no_player = (CardView) itemView.findViewById(R.id.no_player);

            this.win_loss_statusView_ly = (LinearLayout) itemView.findViewById(R.id.win_loss_statusView_ly);
            this.win_loss_statusView = (CardView) itemView.findViewById(R.id.win_loss_statusView);
            this.statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);

        }

    }

}
