package com.vernos.sqorr.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.MyCardsPojo;
import com.vernos.sqorr.utilities.BitmapUtils;
import com.vernos.sqorr.utilities.PathParser;
import com.vernos.sqorr.views.MatchupScreen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.vernos.sqorr.utilities.Utilities.convertTParellelogram;
import static com.vernos.sqorr.utilities.Utilities.resizePath;


public class MyCards_Adapter extends RecyclerView.Adapter<MyCards_Adapter.ViewHolder> {


    private final List<MyCardsPojo> mValues;
    //    private final PromosFragment.OnListFragmentInteractionListener mListener;
    static FragmentActivity activity_t;

    public MyCards_Adapter(List<MyCardsPojo> items, FragmentActivity activity) {
        mValues = items;
        this.activity_t = activity;
    }

    @NonNull
    @Override
    public MyCards_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mycards_s, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCards_Adapter.ViewHolder holder, int position) {

        final MyCardsPojo myCardsList = mValues.get(position);


        final Bitmap first_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                R.drawable.adam_scott);
        final Bitmap second_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                R.drawable.branden_grace);

        final Bitmap frame_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                R.drawable.frame12);
        final Bitmap frame_bitmap2 = BitmapFactory.decodeResource(activity_t.getResources(),
                R.drawable.frame12);


        holder.player1Img.setImageBitmap(convertTParellelogram(first_bitmap, "xxx"));
        holder.player2Img.setImageBitmap(convertTParellelogram(second_bitmap, "pare"));

        holder.playerFrame1.setImageBitmap(convertTParellelogram(frame_bitmap, "xxx"));
        holder.playerFrame2.setImageBitmap(convertTParellelogram(frame_bitmap2, "pare"));


        holder.mIdView.setText(myCardsList.getCardTitle());
        holder.tvMatchUpType.setText(myCardsList.getMatchupType());


        holder.count_txt.setText(myCardsList.getMatchupsWon()+" / "+ myCardsList.getMatchupsPlayed());


        if (myCardsList.getStatus().equalsIgnoreCase("LOSS")) {
            holder.s_ll.setVisibility(View.VISIBLE);
            holder.s_won.setVisibility(View.GONE);

            holder.tvCardwin.setText(myCardsList.getStatus());
        } else if (myCardsList.getStatus().equalsIgnoreCase("WIN")) {
            holder.s_ll.setVisibility(View.GONE);
            holder.s_won.setVisibility(View.VISIBLE);
            holder.mywin_amount.setText(myCardsList.getStatus());

        } else if (myCardsList.getStatus().equalsIgnoreCase("CANCELLED")) {
            holder.s_ll.setVisibility(View.VISIBLE);
            holder.s_won.setVisibility(View.GONE);

            holder.tvCardwin.setText(myCardsList.getStatus());
        } else {
//            holder.s_ll.setVisibility(View.GONE);
        }


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("EST"));
        String currentDate = f.format(new Date()).toString();

        String[] currentTime = currentDate.split(" ");
        String[] hoursMIns = currentTime[1].split(":");
        int curHour = Integer.parseInt(hoursMIns[0]);
        int curMins = Integer.parseInt(hoursMIns[1]);

        String leagueTime = myCardsList.getStartTime();

        if (leagueTime != null && !leagueTime.equals("")) {
            holder.tvStartTime.setVisibility(View.VISIBLE);
            String[] timeArray = leagueTime.split("T");
            if (timeArray.length > 0) {
                String lg_time = timeArray[1];
                String[] hourArray = lg_time.split("\\.");
                if (hourArray.length > 0) {
                    String[] final_time = hourArray[0].split(":");

                    if (final_time.length > 0) {
                        int lg_hour = Integer.parseInt(final_time[0]);
                        int lg_mins = Integer.parseInt(final_time[1]);

                        int diffHours = lg_hour - curHour;
                        int diffMins = lg_mins - curMins;
                        if (diffHours > 0) {
                            if (diffMins > 0)
                                holder.tvStartTime.setText(diffHours + "h " + diffMins + "m");
                            else
                                holder.tvStartTime.setText(diffHours + "h");
                        } else {
                            if (diffMins > 0)
                                holder.tvStartTime.setText(diffMins + "m");
                        }
                    }
                }
            }
        } else {
            holder.tvStartTime.setVisibility(View.GONE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent matchup = new Intent(activity_t, MatchupScreen.class);
                matchup.putExtra("cardid", myCardsList.getCardId());
                matchup.putExtra("cardid_color", "");
                matchup.putExtra("cardid_color1", myCardsList.getLeagueAbbrevation());
                activity_t.startActivity(matchup);

            }
        });


        String cardType = myCardsList.getMatchupType();
        holder.tvMatchUpType.setText(cardType);

        if (cardType.equalsIgnoreCase("match-up")) {
            holder.tvMatchUpType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_matchup, 0, 0, 0);
        } else if (cardType.equalsIgnoreCase("mixed")) {
            holder.tvMatchUpType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ptt, 0, 0, 0);
        } else if (cardType.equalsIgnoreCase("OVER-UNDER")) {
            holder.tvMatchUpType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_overunder, 0, 0, 0);
        }

        String leagueName = myCardsList.getLeagueAbbrevation();

        switch (leagueName) {
            case "NFL":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.foot_ball_color));
                break;

            case "NBA":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.basket_ball_color));
                break;

            case "NHL":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.hockey_color));
                break;

            case "NASCAR":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.car_race_color));
                break;

            case "MLB":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.base_ball_color));
                break;

            case "EPL":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.tennis_color));
                break;

            case "LA-LIGA":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.foot_ball_color));
                break;

            case "MLS":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.soccer_color));
                break;

            case "NCAAMB":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.basket_ball_color));
                break;

            case "PGA":
                holder.cardColor.setBackgroundColor(activity_t.getResources().getColor(R.color.golf_color));
                break;
            default:
                break;
        }

    }


    public static Path resizePath(Path path, float width, float height) {
        RectF bounds = new RectF(0, 0, width, height);
        Path resizedPath = new Path(path);
        RectF src = new RectF();
        resizedPath.computeBounds(src, true);

        Matrix resizeMatrix = new Matrix();
        resizeMatrix.setRectToRect(src, bounds, Matrix.ScaleToFit.CENTER);
        resizedPath.transform(resizeMatrix);

        return resizedPath;
    }

    public static Bitmap convertTParellelogram(Bitmap src, String type) {
        Bitmap typex;
        if (type.equals("pare")) {
            typex = BitmapUtils.getCroppedBitmap(src, getParellelogramPath(src, type));
        } else if (type.equals("xxx")) {
            typex = BitmapUtils.getCroppedBitmap(src, getParellelogramPath(src, type));
        } else {
            typex = BitmapUtils.getCroppedBitmap(src, getParellelogramPath(src, type));
        }
        return typex;
    }

    public static Path getParellelogramPath(Bitmap src, String type) {
        Path path = null;
        if (type.equals("pare")) {
            path = resizePath(PathParser.createPathFromPathData(activity_t.getString(R.string.pare)),
                    src.getWidth(), src.getHeight());
        } else if (type.equals("xxx")) {
            path = resizePath(PathParser.createPathFromPathData(activity_t.getString(R.string.square)),
                    src.getWidth(), src.getHeight());
        } else {
            path = resizePath(PathParser.createPathFromPathData(activity_t.getString(R.string.square)),
                    src.getWidth(), src.getHeight());
        }

        return path;
    }

    @Override
    public int getItemCount() {
        return this.mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView, tvMatchUpType, tvCardwin, tvStartTime, mywin_amount,count_txt;
        // public final TextView mContentView;
        final ImageView player1Img, player2Img, playerFrame1, playerFrame2;

        public final LinearLayout s_ll, s_won;
        public MyCardsPojo mItem;
        private View cardColor;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.tvCardTitle);
            tvMatchUpType = (TextView) view.findViewById(R.id.tvMatchUpType);
            //mContentView = (TextView) view.findViewById(R.id.content);
            player1Img = (ImageView) view.findViewById(R.id.player1Img);
            player2Img = (ImageView) view.findViewById(R.id.player2Img);
            playerFrame1 = (ImageView) view.findViewById(R.id.fimg1);
            playerFrame2 = (ImageView) view.findViewById(R.id.fimg2);
            cardColor = view.findViewById(R.id.cardColor);
            tvCardwin = view.findViewById(R.id.tvCardwin);
            s_ll = view.findViewById(R.id.stas);
            s_won = view.findViewById(R.id.wonstas);
            mywin_amount = view.findViewById(R.id.mywins);
            count_txt = view.findViewById(R.id.count_txt);
            tvStartTime = view.findViewById(R.id.tvStartTime);


        }
    }
}
