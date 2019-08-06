package com.vernos.sqorr.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.MyCardsPojo;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.Utilities;
import com.vernos.sqorr.views.Dashboard;
import com.vernos.sqorr.views.MatchupScreen;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.vernos.sqorr.utilities.Utilities.convertTParellelogram;

public class HomeCardsFragment extends Fragment {

    private PromosFragment.OnListFragmentInteractionListener mListener;
    private  List<MyCardsPojo> cardsList = new ArrayList<>();
    private  RecyclerView rvTabList;
    private List<Object> recyclerViewItems = new ArrayList<>();
    private RecyclerView.Adapter<RecyclerView.ViewHolder> recycleAdapter;
    private ImageView ivNoCards;
    private LinearLayout llNoCards;
    private int MY_CARDS_MAX = 3,myCardsCount = 0;
    private List<MyCardsPojo> myCardsData = new ArrayList<>();
    private List<MyCardsPojo> upcomingCardsData = new ArrayList<>();
    private String  leagueId;
    private TextView tvNoCards;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_cards, container, false);

        llNoCards = v.findViewById(R.id.llNoCards);
        rvTabList=v.findViewById(R.id.rvTabList);

        tvNoCards = v.findViewById(R.id.tvNoCards);
        ivNoCards = v.findViewById(R.id.ivNoCards);
        progressBar=v.findViewById(R.id.progressBar);
        tvNoCards.setText("There are no cards right now");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvTabList.setLayoutManager(llm);
        rvTabList.setItemAnimator(null);

        if (getArguments() != null) {
            leagueId = getArguments().getString("lid");
            progressBar.setVisibility(View.VISIBLE);
            getIndividualTabsData(leagueId);
        }

        return v;
    }


    private void getIndividualTabsData(String leagueId) {

        AndroidNetworking.get(APIs.GET_CARDS+"?leagueId="+leagueId)
                .setPriority(Priority.HIGH)
//                .addHeaders("sessionToken", "5d38b46f2165cf376851baf8")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.e("TabsData:: ", response.toString());
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jb = response.getJSONObject(i);

                                MyCardsPojo mp = new MyCardsPojo();

                                JSONArray ja = jb.getJSONArray("playerImages");

                                String a1 = String.valueOf(ja.get(0));
                                String a2 = String.valueOf(ja.get(1));

                                mp.setCardId(jb.getString("cardId"));
                                mp.setCardTitle(jb.getString("cardTitle"));
                                mp.setMatchupType(jb.getString("matchupType"));
                                mp.setStartTime(jb.getString("startTime"));
                                mp.setLeagueId(jb.getString("leagueId"));
                                mp.setLeagueAbbrevation(jb.getString("leagueAbbrevation"));
                                mp.setPlayerImageLeft(a1);
                                mp.setPlayerImageRight(a2);

                                mp.setIsPurchased(jb.getString("isPurchased"));
                                mp.setIsLive(jb.getString("isLive"));

                                cardsList.add(mp);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        handleDiffData();
                        setPageData();
                        progressBar.setVisibility(View.GONE);
                        if (recycleAdapter != null)
                            recycleAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(View.GONE);
                        Utilities.showToast(getActivity(),"Response error from server ");
                        // handle error
                    }
                });

    }

    private void handleDiffData(){
        //If No response
        if (cardsList.size() <= 0) {
            rvTabList.setVisibility(View.GONE);
            llNoCards.setVisibility(View.VISIBLE);
            handleNoData();
        } else { // If we had cards in response
            rvTabList.setVisibility(View.VISIBLE);
            llNoCards.setVisibility(View.GONE);
            recycleAdapter = new RecyclerViewAdapterNew(recyclerViewItems, getActivity());
            rvTabList.setAdapter(recycleAdapter);
            processResponseData();
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleNoData(){

        ivNoCards.setColorFilter(ivNoCards.getContext().getResources().getColor(R.color.no_cards_color), PorterDuff.Mode.SRC_ATOP);
        if(leagueId.equals(getResources().getString(R.string.nfl_lg_id))){
            tvNoCards.setText("There are no NFL cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_am_football);
        }else  if(leagueId.equals(getResources().getString(R.string.nba_lg_id))){
            tvNoCards.setText("There are no NBA cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_basketball);
        }else  if(leagueId.equals(getResources().getString(R.string.nhl_lg_id))){
            tvNoCards.setText("There are no NHL cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_hockey);
        }else  if(leagueId.equals(getResources().getString(R.string.nascar_lg_id))){
            tvNoCards.setText("There are no NASCAR cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_helmet);
        }else  if(leagueId.equals(getResources().getString(R.string.mlb_lg_id))){
            tvNoCards.setText("There are no MLB cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_baseball);
        }else  if(leagueId.equals(getResources().getString(R.string.epl_lg_id))){
            tvNoCards.setText("There are no EPL cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_tennis);
        }else  if(leagueId.equals(getResources().getString(R.string.LALIGA_lg_id))){
            tvNoCards.setText("There are no LA-LIGA cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_am_football);
        }else  if(leagueId.equals(getResources().getString(R.string.mls_lg_id))){
            tvNoCards.setText("There are no MLS cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_soccer);
        } else  if(leagueId.equals(getResources().getString(R.string.NCAAMB_lg_id))){
            tvNoCards.setText("There are no NCAAMB cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_basketball);
        } else  if(leagueId.equals(getResources().getString(R.string.pga_lg_id))){
            tvNoCards.setText("There are no PGA cards right now.");
            ivNoCards.setImageResource(R.drawable.ic_golf);
        }

    }

    //Process the cards fetched from server
    private void processResponseData(){
        for (int i = 0; i < cardsList.size(); i++) {
            if (cardsList.get(i).getIsPurchased().contains("true")) {
                if (myCardsCount < MY_CARDS_MAX) {
                    myCardsData.add(cardsList.get(i));
                }
                myCardsCount++;
            } else {
                upcomingCardsData.add(cardsList.get(i));
            }
        }
    }

    private void setPageData() {

        if (myCardsCount > 0) {
            recyclerViewItems.add("My cards");
            recyclerViewItems.addAll(myCardsData);
        }

        if(upcomingCardsData.size()>0) {

            recyclerViewItems.add("Upcoming cards");
            recyclerViewItems.addAll(upcomingCardsData);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PromosFragment.OnListFragmentInteractionListener) {
            mListener = (PromosFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public class RecyclerViewAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // A menu item view type.
        private static final int MENU_ITEM_VIEW_TYPE = 0;

        // The banner ad view type.
        private static final int BANNER_AD_VIEW_TYPE = 1;

        // An Activity's Context.
        private final Context context;

        // The list of banner ads and menu items.
        private final List<Object> recyclerViewItems;

        // FragmentActivity activity_t;

        RecyclerViewAdapterNew(List<Object> recyclerViewItems, Context context) {
            //    this.context = context;
            this.recyclerViewItems = recyclerViewItems;
            this.context = context;
        }


        class MenuItemViewHolder extends RecyclerView.ViewHolder {

            private TextView tvCardTitle, tvMatchUpType, tvStartTime;
            private View cardColor;

            ImageView player1Img, player2Img;
            LinearLayout llTotal,llLive;


            MenuItemViewHolder(View convertView) {
                super(convertView);

                tvCardTitle = convertView.findViewById(R.id.tvCardTitle);
                player1Img = convertView.findViewById(R.id.player1Img);
                player2Img = convertView.findViewById(R.id.player2Img);
                llTotal = convertView.findViewById(R.id.llTotal);
                tvMatchUpType = convertView.findViewById(R.id.tvMatchUpType);
                tvStartTime = convertView.findViewById(R.id.tvStartTime);
                cardColor = convertView.findViewById(R.id.cardColor);
                llLive=convertView.findViewById(R.id.llLive);
            }
        }

        class AdViewHolder extends RecyclerView.ViewHolder {
            private TextView tvHeader, tvViewALL;

            AdViewHolder(View adview) {
                super(adview);
                tvHeader = adview.findViewById(R.id.tvHeader);
                tvViewALL = adview.findViewById(R.id.tvViewALL);
            }
        }

        @Override
        public int getItemCount() {
            return recyclerViewItems.size();
        }

        /**
         * Determines the view type for the given position.
         */
        @Override
        public int getItemViewType(int position) {
            int viewType = -1;

            if (recyclerViewItems.get(position) instanceof MyCardsPojo) {
                viewType = 0;
            } else {
                viewType = 1;
            }
            return viewType;
        }

        /**
         * Creates a new view for a menu item view or a banner ad view
         * based on the viewType. This method is invoked by the layout manager.
         */
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            switch (viewType) {
                case MENU_ITEM_VIEW_TYPE:
                    View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                            R.layout.fragment_mlb, viewGroup, false);
                    return new MenuItemViewHolder(menuItemLayoutView);
                case BANNER_AD_VIEW_TYPE:
                    // fall through
                default:
                    View bannerLayoutView = LayoutInflater.from(
                            viewGroup.getContext()).inflate(R.layout.header_layout,
                            viewGroup, false);
                    return new AdViewHolder(bannerLayoutView);
            }
        }

        /**
         * Replaces the content in the views that make up the menu item view and the
         * banner ad view. This method is invoked by the layout manager.
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int ele_position) {

            int viewType = getItemViewType(ele_position);
            final int position = holder.getAdapterPosition();
            switch (viewType) {
                case MENU_ITEM_VIEW_TYPE:
                    final MenuItemViewHolder listingView = (MenuItemViewHolder) holder;
                    final MyCardsPojo all_home_cards = (MyCardsPojo) recyclerViewItems.get(position);

                    listingView.tvCardTitle.setText(all_home_cards.getCardTitle());
                    String cardType = all_home_cards.getMatchupType();
                    listingView.tvMatchUpType.setText(cardType);

                    if (cardType.equalsIgnoreCase("match-up")) {
                        listingView.tvMatchUpType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_matchup, 0, 0, 0);
                    } else if (cardType.equalsIgnoreCase("mixed")) {
                        listingView.tvMatchUpType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ptt, 0, 0, 0);
                    } else if (cardType.equalsIgnoreCase("OVER-UNDER")) {
                        listingView.tvMatchUpType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_overunder, 0, 0, 0);
                    }

                    if(all_home_cards.getIsLive().equals("true")){
                        listingView.llLive.setVisibility(View.VISIBLE);
                        listingView.tvStartTime.setVisibility(View.GONE);
                    }else{
                        listingView.tvStartTime.setVisibility(View.VISIBLE);
                        listingView.llLive.setVisibility(View.GONE);
                    }


                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                    f.setTimeZone(TimeZone.getTimeZone("EST"));
                    String currentDate = f.format(new Date()).toString();

                    String [] currentTime= currentDate.split(" ");
                    String [] hoursMIns = currentTime[1].split(":");
                    int  curHour=Integer.parseInt( hoursMIns[0]);
                    int curMins=Integer.parseInt(hoursMIns[1]);

                    String leagueTime =all_home_cards.getStartTime();

                    if(leagueTime!=null&&!leagueTime.equals("")) {
                        listingView.tvStartTime.setVisibility(View.VISIBLE);
                        String[] timeArray = leagueTime.split("T");
                        if(timeArray.length>0) {
                            String lg_time = timeArray[1];
                            String[] hourArray = lg_time.split("\\.");
                            if(hourArray.length>0){
                                String [] final_time = hourArray[0].split(":");

                                if(final_time.length>0) {
                                    int lg_hour = Integer.parseInt(final_time[0]);
                                    int lg_mins = Integer.parseInt(final_time[1]);

                                    int diffHours = lg_hour-curHour;
                                    int diffMins=lg_mins-curMins;
                                    if(diffHours>0){
                                        if(diffMins>0)
                                            listingView.tvStartTime.setText(diffHours+"h "+diffMins+"m");
                                        else
                                            listingView.tvStartTime.setText(diffHours+"h");
                                    }else{
                                        if(diffMins>0)
                                            listingView.tvStartTime.setText(diffMins+"m");
                                    }
                                }
                            }
                        }
                    }else{
                        listingView.tvStartTime.setVisibility(View.GONE);
                    }


                 /* final Bitmap first_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                            R.drawable.card_left_basketball);
                    final Bitmap second_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                            R.drawable.card_right_basketball);*/


                    // listingView.player1Img.setImageBitmap(convertTParellelogram(first_bitmap, "xxx"));
                    //  listingView.player2Img.setImageBitmap(convertTParellelogram(second_bitmap, "pare"));

                    Picasso.with(getActivity())
                            .load(all_home_cards.getPlayerImageLeft())
                            .into(new Target() {

                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    /* Save the bitmap or do something with it here */

                                    // Set it in the ImageView
                                    // listingView.player1Img.setImageBitmap(convertTParellelogram(bitmap, "xxx"));

                                    listingView.player1Img.setImageBitmap(convertTParellelogram(bitmap, "xxx", getActivity()));
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    listingView.player1Img.setImageDrawable(getResources().getDrawable(R.drawable.cl_basketball));
                                }
                            });

                    Picasso.with(getActivity())
                            .load(all_home_cards.getPlayerImageLeft())
                            .into(new Target() {

                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    /* Save the bitmap or do something with it here */

                                    // Set it in the ImageView
                                    listingView.player1Img.setImageBitmap(bitmap);
                                    //listingView.player2Img.setImageBitmap(convertTParellelogram(bitmap, "pare"));
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    listingView.player2Img.setImageDrawable(getResources().getDrawable(R.drawable.cr_basketball));
                                }
                            });

                    //   listingView.player1Img.setBackground(getResources().getDrawable(R.drawable.damian_lillard));
                    // listingView.player2Img.setBackground(getResources().getDrawable(R.drawable.dexter_fowler));
                    String leagueName = all_home_cards.getLeagueAbbrevation();

                    switch (leagueName) {
                        case "NFL":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.foot_ball_color));
                            break;

                        case "NBA":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.basket_ball_color));
                            break;

                        case "NHL":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.hockey_color));
                            break;

                        case "NASCAR":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.car_race_color));
                            break;

                        case "MLB":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.base_ball_color));
                            break;

                        case "EPL":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.tennis_color));
                            break;

                        case "LA-LIGA":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.foot_ball_color));
                            break;

                        case "MLS":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.soccer_color));
                            break;

                        case "NCAAMB":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.basket_ball_color));
                            break;

                        case "PGA":
                            listingView.cardColor.setBackgroundColor(getResources().getColor(R.color.golf_color));
                            break;
                        default:
                            break;
                    }


                    listingView.llTotal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mListener) {
                                Intent matchup = new Intent(getActivity(), MatchupScreen.class);
                                matchup.putExtra("cardid_color", "");
                                matchup.putExtra("cardid_color1", all_home_cards.getLeagueAbbrevation());
                                startActivity(matchup);
                            }
                        }
                    });

                    break;
                case BANNER_AD_VIEW_TYPE:
                    // fall through
                    final AdViewHolder adViewHolder = (AdViewHolder) holder;

                    //handle header data
                    String headerStr ="";
                    if (recyclerViewItems.get(ele_position) instanceof String) {
                        headerStr = (String) recyclerViewItems.get(ele_position);
                    }
                    adViewHolder.tvHeader.setText(headerStr);
                    adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


                    if (headerStr!=null&&headerStr.equals("My cards")) {
                        if(myCardsCount>MY_CARDS_MAX)
                            adViewHolder.tvViewALL.setVisibility(View.VISIBLE);
                        else
                            adViewHolder.tvViewALL.setVisibility(View.GONE);
                    }
                    else {
                        adViewHolder.tvViewALL.setVisibility(View.GONE);
                    }

                    adViewHolder.tvViewALL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                Activity act = getActivity();
                                if (act instanceof Dashboard)
                                    ((Dashboard) act).navToMyCards();
                            } catch (Exception e) {
                                e.getStackTrace();
                            }

                        }
                    });
                default:


                    break;
            }
        }

    }


}