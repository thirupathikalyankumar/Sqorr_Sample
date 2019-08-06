package com.vernos.sqorr.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
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
import com.vernos.sqorr.utilities.PresetValueButton;
import com.vernos.sqorr.utilities.Utilities;
import com.vernos.sqorr.views.Dashboard;
import com.vernos.sqorr.views.MatchupScreen;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import static com.vernos.sqorr.utilities.Utilities.convertTParellelogram;

public class AllHomeFragment extends Fragment {


    private PromosFragment.OnListFragmentInteractionListener mListener;

    private List<Object> recyclerViewItems = new ArrayList<>();
    private RecyclerView.Adapter<RecyclerView.ViewHolder> recycleAdapter;
    private RecyclerView rvAllTab;

    private static FragmentActivity activity_t;
    private  PresetValueButton EPL_pvb, LA_LIGA_pvb, mlb_pvb, mls_pvb, NASCAR_pvb, NBA_pvb, NCAAMB_pvb, NFL_pvb, NHL_pvb, PGA_pvb;
    private HorizontalScrollView horizontal;

    private List<MyCardsPojo> cardsResponse = new ArrayList<>();
    private int MY_CARDS_MAX = 3, CARDS_MAX = 2;


    private int myCardsCount = 0, NFLCount = 0, NBACount = 0, NHLCount = 0, NASCARCount = 0, MLBCount = 0,
            EPLCount = 0, LALIGACount = 0, MLSCount = 0, NCAAMBCount = 0, PGACount = 0;

    private List<String> cardTypes = new ArrayList<>();
    private Map<String, String> leagueData = new HashMap<>();

    private List<MyCardsPojo> myCardsData = new ArrayList<>();
    private List<MyCardsPojo> EPLData = new ArrayList<>();
    private List<MyCardsPojo> LALIGAData = new ArrayList<>();
    private List<MyCardsPojo> NFLData = new ArrayList<>();
    private List<MyCardsPojo> NBAData = new ArrayList<>();
    private List<MyCardsPojo> NHLData = new ArrayList<>();
    private List<MyCardsPojo> NASCARData = new ArrayList<>();
    private List<MyCardsPojo> MLBData = new ArrayList<>();
    private List<MyCardsPojo> MLSData = new ArrayList<>();
    private List<MyCardsPojo> NCAAMBData = new ArrayList<>();
    private List<MyCardsPojo> PGAData = new ArrayList<>();


    private ImageView ivNoCards;
    private LinearLayout llNoCards;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_home, container, false);

        llNoCards = v.findViewById(R.id.no_ll);

        TextView tvNoCards = v.findViewById(R.id.no_txt);
        ivNoCards = v.findViewById(R.id.no_logo);
        tvNoCards.setText("There are no cards right now");
        rvAllTab = v.findViewById(R.id.rvAllTab);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvAllTab.setLayoutManager(llm);
        rvAllTab.setItemAnimator(null);

        progressBar=v.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        getHomeAllTabData();


        return v;
    }

    //To get data of ALL tab data of home
    private void getHomeAllTabData() {

        AndroidNetworking.get(APIs.GET_CARDS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        recyclerViewItems.clear();
                        cardsResponse.clear();
                        Log.e("ALL +HOME :: ", response.toString());

                        // do anything with response
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
                                cardsResponse.add(mp);

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
                        // handle error
                        progressBar.setVisibility(View.GONE);
                        Utilities.showToast(getActivity(), error.getErrorDetail());
                    }
                });

    }


    private void handleDiffData() {
        //If No response
        if (cardsResponse.size() <= 0) {
            rvAllTab.setVisibility(View.GONE);
            llNoCards.setVisibility(View.VISIBLE);
            ivNoCards.setImageResource(R.drawable.ic_star);
        } else { // If we had cards in response
            rvAllTab.setVisibility(View.VISIBLE);
            llNoCards.setVisibility(View.GONE);
            recycleAdapter = new RecyclerViewAdapterNew(recyclerViewItems, getActivity());
            rvAllTab.setAdapter(recycleAdapter);
            processResponseData();
        }
    }

    //Process the cards fetched from server
    private void processResponseData(){
        for (int i = 0; i < cardsResponse.size(); i++) {
            if (cardsResponse.get(i).getIsPurchased().contains("true")) {
                if (myCardsCount < MY_CARDS_MAX) {
                    myCardsData.add(cardsResponse.get(i));
                }
                myCardsCount++;
            } else {
                String leagueName = cardsResponse.get(i).getLeagueAbbrevation();
                if (!cardTypes.contains(leagueName)) {
                    cardTypes.add(leagueName);
                }
                String leagueId = cardsResponse.get(i).getLeagueId();
                if (!leagueData.containsKey(leagueName))
                    leagueData.put(leagueName, leagueId);

                switch (leagueName) {
                    case "NFL":
                        if (NFLCount < CARDS_MAX) {
                            NFLData.add(cardsResponse.get(i));
                        }
                        NFLCount++;
                        break;

                    case "NBA":
                        if (NBACount < CARDS_MAX) {
                            NBAData.add(cardsResponse.get(i));
                        }
                        NBACount++;
                        break;

                    case "NHL":
                        if (NHLCount < CARDS_MAX) {
                            NHLData.add(cardsResponse.get(i));
                        }
                        NHLCount++;
                        break;

                    case "NASCAR":
                        if (NASCARCount < CARDS_MAX) {
                            NASCARData.add(cardsResponse.get(i));
                        }
                        NASCARCount++;
                        break;

                    case "MLB":
                        if (MLBCount < CARDS_MAX) {
                            MLBData.add(cardsResponse.get(i));
                        }
                        MLBCount++;
                        break;

                    case "EPL":
                        if (EPLCount < CARDS_MAX) {
                            EPLData.add(cardsResponse.get(i));
                        }
                        EPLCount++;
                        break;

                    case "LA-LIGA":
                        if (LALIGACount < CARDS_MAX) {
                            LALIGAData.add(cardsResponse.get(i));
                        }
                        LALIGACount++;
                        break;

                    case "MLS":
                        if (MLSCount < CARDS_MAX) {
                            MLSData.add(cardsResponse.get(i));
                        }
                        MLSCount++;
                        break;

                    case "NCAAMB":
                        if (NCAAMBCount < CARDS_MAX) {
                            NCAAMBData.add(cardsResponse.get(i));
                        }
                        NCAAMBCount++;
                        break;

                    case "PGA":
                        if (PGACount < CARDS_MAX) {
                            PGAData.add(cardsResponse.get(i));
                        }
                        PGACount++;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {

            horizontal = getActivity().findViewById(R.id.horizontal);
            EPL_pvb = getActivity().findViewById(R.id.EPL);
            LA_LIGA_pvb = getActivity().findViewById(R.id.LA_LIGA);
            mlb_pvb = getActivity().findViewById(R.id.mlb);
            mls_pvb = getActivity().findViewById(R.id.mls);
            NASCAR_pvb = getActivity().findViewById(R.id.NASCAR);
            NBA_pvb = getActivity().findViewById(R.id.NBA);
            NCAAMB_pvb = getActivity().findViewById(R.id.NCAAMB);
            NFL_pvb = getActivity().findViewById(R.id.NFL);
            NHL_pvb = getActivity().findViewById(R.id.NHL);
            PGA_pvb = getActivity().findViewById(R.id.PGA);

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

    private void setPageData() {

        if (myCardsCount > 0) {
            recyclerViewItems.add("My cards");
            recyclerViewItems.addAll(myCardsData);
        }

        for (int i = 0; i < cardTypes.size(); i++) {
            switch (cardTypes.get(i)) {
                case "NFL":
                    recyclerViewItems.add("NFL");
                    recyclerViewItems.addAll(NFLData);
                    break;

                case "NBA":
                    recyclerViewItems.add("NBA");
                    recyclerViewItems.addAll(NBAData);
                    break;

                case "NHL":
                    recyclerViewItems.add("NHL");
                    recyclerViewItems.addAll(NHLData);
                    break;

                case "NASCAR":
                    recyclerViewItems.add("NASCAR");
                    recyclerViewItems.addAll(NASCARData);
                    break;

                case "MLB":
                    recyclerViewItems.add("MLB");
                    recyclerViewItems.addAll(MLBData);
                    break;

                case "EPL":
                    recyclerViewItems.add("EPL");
                    recyclerViewItems.addAll(EPLData);
                    break;

                case "LA-LIGA":
                    recyclerViewItems.add("LA-LIGA");
                    recyclerViewItems.addAll(LALIGAData);
                    break;

                case "MLS":
                    recyclerViewItems.add("MLS");
                    recyclerViewItems.addAll(MLSData);
                    break;

                case "NCAAMB":
                    recyclerViewItems.add("NCAAMB");
                    recyclerViewItems.addAll(NCAAMBData);
                    break;

                case "PGA":
                    recyclerViewItems.add("PGA");
                    recyclerViewItems.addAll(PGAData);
                    break;
                default:
                    break;
            }
        }

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

                    if(all_home_cards.getIsLive().equalsIgnoreCase("true")){
                        listingView.tvStartTime.setVisibility(View.GONE);
                        listingView.llLive.setVisibility(View.VISIBLE);
                    }else{
                        listingView.tvStartTime.setVisibility(View.VISIBLE);
                        listingView.llLive.setVisibility(View.GONE);
                    }

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                    f.setTimeZone(TimeZone.getTimeZone("EDT5EST"));
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
                    final String leagueName = all_home_cards.getLeagueAbbrevation();

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
                                // Notify the active callbacks interface (the activity, if the
                                // fragment is attached to one) that an item has been selected.

                                Intent matchup = new Intent(context, MatchupScreen.class);
                                matchup.putExtra("cardid", all_home_cards.getCardId());
                                matchup.putExtra("cardid_color", "");
                                matchup.putExtra("cardid_color1", all_home_cards.getLeagueAbbrevation());

                                context.startActivity(matchup);

                                // mListener.onListFragmentInteraction(holder.mItem);
                            }
                        }
                    });

                    break;
                case BANNER_AD_VIEW_TYPE:
                    // fall through
                    final AdViewHolder adViewHolder = (AdViewHolder) holder;

                    //handle header data
                    String headerStr = "Header";
                    if (recyclerViewItems.get(ele_position) instanceof String) {
                        headerStr = (String) recyclerViewItems.get(ele_position);
                    }
                    adViewHolder.tvHeader.setText(headerStr);

                    //handle view all

                    boolean showViewAll = false;
                    switch (headerStr) {
                        case "My cards":
                            if (myCardsCount > MY_CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            break;
                        case "NFL":
                            if (NFLCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_am_football, 0, 0, 0);
                            break;

                        case "NBA":
                            if (NBACount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_basketball, 0, 0, 0);
                            break;

                        case "NHL":
                            if (NHLCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hockey, 0, 0, 0);
                            break;
                        case "NASCAR":
                            if (NASCARCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_helmet, 0, 0, 0);
                            break;

                        case "MLB":
                            if (MLBCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseball, 0, 0, 0);
                            break;

                        case "EPL":
                            if (EPLCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tennis, 0, 0, 0);
                            break;

                        case "LA-LIGA":
                            if (LALIGACount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_am_football, 0, 0, 0);
                            break;

                        case "MLS":
                            if (MLSCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_soccer, 0, 0, 0);
                            break;

                        case "NCAAMB":
                            if (NCAAMBCount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_basketball, 0, 0, 0);
                            break;

                        case "PGA":
                            if (PGACount > CARDS_MAX)
                                showViewAll = true;
                            adViewHolder.tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_golf, 0, 0, 0);
                            break;
                        default:
                            break;
                    }

                    if (showViewAll) {
                        adViewHolder.tvViewALL.setVisibility(View.VISIBLE);
                    } else {
                        adViewHolder.tvViewALL.setVisibility(View.GONE);
                    }

                    final String finalHeaderStr = headerStr;
                    String leagueId = "";

                    for (Map.Entry<String, String> entry : leagueData.entrySet()) {
                        if (entry.getValue().equals(finalHeaderStr)) {
                            leagueId = entry.getKey();
                        }
                    }


                    final String finalLeagueId = leagueId;
                    adViewHolder.tvViewALL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (finalHeaderStr.equalsIgnoreCase("My cards")) {
                                try {
                                    Activity act = getActivity();
                                    if (act instanceof Dashboard)
                                        ((Dashboard) act).navToMyCards();
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("lid", finalLeagueId);
                                Fragment someFragment2 = new HomeCardsFragment();
                                someFragment2.setArguments(bundle);
                                FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
                                transaction2.replace(R.id.home_frame_layout, someFragment2); // give your fragment container id in first parameter
                                transaction2.addToBackStack(null);  // if written, this transaction will be added to back stack
                                transaction2.commit();

                            }
                            //To scroll the view programmatically
                            if (horizontal != null) {
                                // Obtain MotionEvent object
                                long downTime = SystemClock.uptimeMillis();
                                long eventTime = SystemClock.uptimeMillis() + 100;
                                MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 0.0f, 0.0f, 0);
                                switch (finalHeaderStr) {

                                    case "NFL":
                                        horizontal.smoothScrollBy((NFL_pvb.getLeft() - 500), 0);
                                        NFL_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "NBA":
                                        horizontal.smoothScrollBy((NBA_pvb.getLeft() - 500), 0);
                                        PGA_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "NHL":
                                        horizontal.smoothScrollBy((NHL_pvb.getLeft() - 500), 0);
                                        NHL_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "NASCAR":
                                        horizontal.smoothScrollBy((NASCAR_pvb.getLeft() - 500), 0);
                                        NASCAR_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "MLB":
                                        horizontal.smoothScrollBy((mlb_pvb.getLeft() - 500), 0);
                                        mlb_pvb.dispatchTouchEvent(motionEvent);

                                        break;

                                    case "EPL":
                                        horizontal.smoothScrollBy((EPL_pvb.getLeft() - 500), 0);
                                        EPL_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "LA-LIGA":
                                        horizontal.smoothScrollBy((LA_LIGA_pvb.getLeft() - 500), 0);
                                        LA_LIGA_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "MLS":
                                        horizontal.smoothScrollBy((mls_pvb.getLeft() - 500), 0);
                                        mls_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "NCAAMB":
                                        horizontal.smoothScrollBy((NCAAMB_pvb.getLeft() - 500), 0);
                                        NCAAMB_pvb.dispatchTouchEvent(motionEvent);
                                        break;

                                    case "PGA":
                                        horizontal.smoothScrollBy((PGA_pvb.getLeft() - 500), 0);
                                        PGA_pvb.dispatchTouchEvent(motionEvent);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }
                    });
                default:


                    break;
            }
        }

    }


}