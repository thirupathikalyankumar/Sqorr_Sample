package com.vernos.sqorr.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.vernos.sqorr.R;
import com.vernos.sqorr.adapters.TabsAdapter;
import com.vernos.sqorr.pojos.MyCardsPojo;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.PresetValueButton;
import com.vernos.sqorr.views.Dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CardSFrag extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    LinearLayout without, withtc;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    List<MyCardsPojo> myCardsPojo_u = new ArrayList<>();
    List<MyCardsPojo> myCardsPojo_l = new ArrayList<>();
    List<MyCardsPojo> myCardsPojo_s = new ArrayList<>();

    PresetValueButton all_pvb, EPL_pvb, LA_LIGA_pvb, mlb_pvb, mls_pvb, NASCAR_pvb, NBA_pvb, NCAAMB_pvb, NFL_pvb, NHL_pvb, PGA_pvb;

    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragtwo, container, false);
    progressBar = v.findViewById(R.id.progressBar);
        viewPager = v.findViewById(R.id.pager_mycards);

        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);

//        getApp();
        return v;
    }

    private void getApp() {


        Log.e("70-----","Start");
        AndroidNetworking.get(APIs.MY_CARDS)
                .setPriority(Priority.HIGH)
                .addHeaders("sessionToken", Dashboard.SESSIONTOKEN)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        Log.e("80-----","Response Start");

                        // do anything with response
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jb = response.getJSONObject(i);


                                Log.e("90--", jb.toString());

                                String status_data = jb.getString("status");
                                if (status_data.equalsIgnoreCase("PENDING")) {
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
                                    mp.setStatus(jb.getString("status"));
                                    mp.setCurrencyTypeIsTokens(jb.getString("currencyTypeIsTokens"));
                                    mp.setMatchupsPlayed(jb.getString("matchupsPlayed"));
                                    mp.setMatchupsWon(jb.getString("matchupsWon"));
                                    mp.setSettlementDate(jb.getString("settlementDate"));
//                                    mp.setWinAmount(jb.getString("winAmount"));
                                    myCardsPojo_u.add(mp);

                                } else if (status_data.equalsIgnoreCase("LIVE")) {

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
                                    mp.setStatus(jb.getString("status"));
                                    mp.setCurrencyTypeIsTokens(jb.getString("currencyTypeIsTokens"));
                                    mp.setMatchupsPlayed(jb.getString("matchupsPlayed"));
                                    mp.setMatchupsWon(jb.getString("matchupsWon"));
                                    mp.setSettlementDate(jb.getString("settlementDate"));
//                                    mp.setWinAmount(jb.getString("winAmount"));

                                    myCardsPojo_l.add(mp);
                                } else {

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
                                    mp.setStatus(jb.getString("status"));
                                    mp.setCurrencyTypeIsTokens(jb.getString("currencyTypeIsTokens"));
                                    mp.setMatchupsPlayed(jb.getString("matchupsPlayed"));
                                    mp.setMatchupsWon(jb.getString("matchupsWon"));
                                    mp.setSettlementDate(jb.getString("settlementDate"));
//                                    mp.setWinAmount(jb.getString("winAmount"));


                                    myCardsPojo_s.add(mp);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        cardsData();
                        progressBar.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                    }


                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("184-----","error Start");
                    }
                });


        sethomeAll(myCardsPojo_u,myCardsPojo_l,myCardsPojo_s,"ALL");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        without = getActivity().findViewById(R.id.without);
        withtc = getActivity().findViewById(R.id.with_tc);

        all_pvb = getActivity().findViewById(R.id.all);
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


        all_pvb.setOnClickListener(this);
        EPL_pvb.setOnClickListener(this);
        LA_LIGA_pvb.setOnClickListener(this);
        mlb_pvb.setOnClickListener(this);
        mls_pvb.setOnClickListener(this);
        NASCAR_pvb.setOnClickListener(this);
        NBA_pvb.setOnClickListener(this);
        NCAAMB_pvb.setOnClickListener(this);
        NFL_pvb.setOnClickListener(this);
        NHL_pvb.setOnClickListener(this);
        PGA_pvb.setOnClickListener(this);

        updateUI();

//        Bundle bundle = new Bundle();
//        bundle.putString("username",editText.getText().toString());


    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        String rrole = Dashboard.ROLE;

        if (rrole.equalsIgnoreCase("0")) {

            without.setVisibility(View.VISIBLE);
            withtc.setVisibility(View.GONE);
        } else {
            without.setVisibility(View.GONE);
            withtc.setVisibility(View.VISIBLE);

            getApp();
            sethomeAll(myCardsPojo_u, myCardsPojo_l, myCardsPojo_s, "ALL");

        }

    }


    private List<MyCardsPojo> EPLData_u = new ArrayList<>();
    private List<MyCardsPojo> EPLData_l = new ArrayList<>();
    private List<MyCardsPojo> EPLData_s = new ArrayList<>();

    private List<MyCardsPojo> LALIGAData_u = new ArrayList<>();
    private List<MyCardsPojo> NFLData_u = new ArrayList<>();
    private List<MyCardsPojo> NBAData_u = new ArrayList<>();
    private List<MyCardsPojo> NHLData_u = new ArrayList<>();
    private List<MyCardsPojo> NASCARData_u = new ArrayList<>();
    private List<MyCardsPojo> MLBData_u = new ArrayList<>();
    private List<MyCardsPojo> MLSData_u = new ArrayList<>();
    private List<MyCardsPojo> NCAAMBData_u = new ArrayList<>();
    private List<MyCardsPojo> PGAData_u = new ArrayList<>();

    private List<MyCardsPojo> LALIGAData_l = new ArrayList<>();
    private List<MyCardsPojo> NFLData_l = new ArrayList<>();
    private List<MyCardsPojo> NBAData_l = new ArrayList<>();
    private List<MyCardsPojo> NHLData_l = new ArrayList<>();
    private List<MyCardsPojo> NASCARData_l = new ArrayList<>();
    private List<MyCardsPojo> MLBData_l = new ArrayList<>();
    private List<MyCardsPojo> MLSData_l = new ArrayList<>();
    private List<MyCardsPojo> NCAAMBData_l = new ArrayList<>();
    private List<MyCardsPojo> PGAData_l = new ArrayList<>();

    private List<MyCardsPojo> LALIGAData_s = new ArrayList<>();
    private List<MyCardsPojo> NFLData_s = new ArrayList<>();
    private List<MyCardsPojo> NBAData_s = new ArrayList<>();
    private List<MyCardsPojo> NHLData_s = new ArrayList<>();
    private List<MyCardsPojo> NASCARData_s = new ArrayList<>();
    private List<MyCardsPojo> MLBData_s = new ArrayList<>();
    private List<MyCardsPojo> MLSData_s = new ArrayList<>();
    private List<MyCardsPojo> NCAAMBData_s = new ArrayList<>();
    private List<MyCardsPojo> PGAData_s = new ArrayList<>();


    private void cardsData() {
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("EPL")) {
                EPLData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("EPL")) {
                EPLData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("EPL")) {
                EPLData_s.add(myCardsPojo_s.get(i));
            }
        }

        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("LA_LIGA")) {
                LALIGAData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("LA_LIGA")) {
                LALIGAData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("LA_LIGA")) {
                LALIGAData_s.add(myCardsPojo_s.get(i));
            }
        }

        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("MLB")) {
                MLBData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("MLB")) {
                MLBData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("MLB")) {
                MLBData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("MLS")) {
                MLSData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("MLS")) {
                MLSData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("MLS")) {
                MLSData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("NASCAR")) {
                NASCARData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("NASCAR")) {
                NASCARData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("NASCAR")) {
                NASCARData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("NBA")) {
                NBAData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("NBA")) {
                NBAData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("NBA")) {
                NBAData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("NCAAMB")) {
                NCAAMBData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("NCAAMB")) {
                NCAAMBData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("NCAAMB")) {
                NCAAMBData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("NFL")) {
                NFLData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("NFL")) {
                NFLData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("NFL")) {
                NFLData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("NHL")) {
                NHLData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("NHL")) {
                NHLData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("NHL")) {
                NHLData_s.add(myCardsPojo_s.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_u.size(); i++) {
            if (myCardsPojo_u.get(i).getLeagueAbbrevation().contains("PGA")) {
                PGAData_u.add(myCardsPojo_u.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_l.size(); i++) {
            if (myCardsPojo_l.get(i).getLeagueAbbrevation().contains("PGA")) {
                PGAData_l.add(myCardsPojo_l.get(i));
            }
        }
        for (int i = 0; i < myCardsPojo_s.size(); i++) {
            if (myCardsPojo_s.get(i).getLeagueAbbrevation().contains("PGA")) {
                PGAData_s.add(myCardsPojo_s.get(i));
            }
        }


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all /*2131230804*/:
                getActivity().findViewById(R.id.all).setSelected(true);
                sethomeAll(myCardsPojo_u, myCardsPojo_l, myCardsPojo_s, "ALL");
                return;
            case R.id.EPL /*2131230804*/:
                getActivity().findViewById(R.id.EPL);//.setSelected(true);
                sethomeAll(EPLData_u, EPLData_l, EPLData_s, "EPL");
                return;
            case R.id.LA_LIGA /*2131230804*/:
                getActivity().findViewById(R.id.LA_LIGA);//.setSelected(true);
                sethomeAll(LALIGAData_u, LALIGAData_l, LALIGAData_s, "LA_LIGA");
                return;
            case R.id.mlb /*2131230804*/:
                getActivity().findViewById(R.id.mlb);//.setSelected(true);
                sethomeAll(MLBData_u, MLBData_l, MLBData_s, "MLB");
                return;
            case R.id.mls /*2131230804*/:
                getActivity().findViewById(R.id.mls);
                sethomeAll(MLSData_u, MLSData_l, MLSData_s, "MLS");
                return;
            case R.id.NASCAR /*2131230804*/:
                getActivity().findViewById(R.id.NASCAR);//.setSelected(true);
                sethomeAll(NASCARData_u, NASCARData_l, NASCARData_s, "NASCAR");
                return;
            case R.id.NBA /*2131230804*/:
                getActivity().findViewById(R.id.NBA);//.setSelected(true);
                sethomeAll(NBAData_u, NBAData_l, NBAData_s, "NBA");
                return;
            case R.id.NCAAMB /*2131230804*/:
                getActivity().findViewById(R.id.NCAAMB);//.setSelected(true);
                sethomeAll(NCAAMBData_u, NCAAMBData_l, NCAAMBData_s, "NCAAMB");
                return;
            case R.id.NFL /*2131230804*/:
                getActivity().findViewById(R.id.NFL);//.setSelected(true);
                sethomeAll(NFLData_u, NFLData_l, NFLData_s, "NFL");
                return;
            case R.id.NHL /*2131230804*/:
                getActivity().findViewById(R.id.NHL);//.setSelected(true);
                sethomeAll(NHLData_u, NHLData_l, NHLData_s, "NHL");
                return;
            case R.id.PGA /*2131230804*/:
                getActivity().findViewById(R.id.PGA);
                sethomeAll(PGAData_u, PGAData_l, PGAData_s, "PGA");
                return;

            default:
                return;
        }
    }

    private void sethomeAll(List<MyCardsPojo> myCardsPojo_u, List<MyCardsPojo> myCardsPojo_l,
                            List<MyCardsPojo> myCardsPojo_s, String l) {

        tabLayout = getActivity().findViewById(R.id.tablayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new MyCardsUpComing().instantiate(l, "", myCardsPojo_u), "UPCOMING");
        adapter.addFragment(new MyCardsLive().instantiate(l, "", myCardsPojo_l), "LIVE");
        adapter.addFragment(new MyCardsSettled().instantiate(l, "", myCardsPojo_s), "SETTLED");
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("onPageSelected Called");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private static int count = 3;

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new MyCardsUpComing();
                case 1:
                    return new MyCardsLive();
                case 2:
                    return new MyCardsSettled();
            }
            return null;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "UPCOMING";
                case 1:
                    return "LIVE";
                case 2:
                    return "SETTLED";
            }
            return null;
        }
    }
}



