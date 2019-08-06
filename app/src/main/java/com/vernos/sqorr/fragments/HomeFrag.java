package com.vernos.sqorr.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.vernos.sqorr.R;

import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.PresetValueButton;
import com.vernos.sqorr.utilities.Utilities;

import org.json.JSONArray;

import static com.vernos.sqorr.views.Dashboard.ROLE;


public class HomeFrag extends Fragment implements View.OnClickListener {

    PresetValueButton all_pvb, EPL_pvb, LA_LIGA_pvb, mlb_pvb, mls_pvb, NASCAR_pvb, NBA_pvb, NCAAMB_pvb, NFL_pvb, NHL_pvb, PGA_pvb;
    private HorizontalScrollView horizontal;
    View vv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragone, container, false);

        vv = v.findViewById(R.id.banner_c);

        if (ROLE.equalsIgnoreCase("cash") || ROLE.equalsIgnoreCase("tokens")) {
            vv.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        horizontal = getActivity().findViewById(R.id.horizontal);


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


        if (Utilities.isNetworkAvailable(getActivity())) {

            sethomeAll();


        } else {
            Utilities.showNoInternetAlert(getActivity());
        }


//        apiCall();
    }

    private void apiCall() {

        AndroidNetworking.get(APIs.GET_CARDS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.e("My Home::no ", response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all /*2131230804*/:
                getActivity().findViewById(R.id.all).setSelected(true);
                sethomeAll();
                return;
            case R.id.EPL /*2131230804*/:
                getActivity().findViewById(R.id.EPL);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.epl_lg_id));
                return;
            case R.id.LA_LIGA /*2131230804*/:
                getActivity().findViewById(R.id.LA_LIGA);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.LALIGA_lg_id));
                return;
            case R.id.mlb /*2131230804*/:
                getActivity().findViewById(R.id.mlb);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.mlb_lg_id));
                return;
            case R.id.mls /*2131230804*/:
                getActivity().findViewById(R.id.mls);
                sethomeOther(getResources().getString(R.string.mls_lg_id));
                return;
            case R.id.NASCAR /*2131230804*/:
                getActivity().findViewById(R.id.NASCAR);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.nascar_lg_id));
                return;
            case R.id.NBA /*2131230804*/:
                getActivity().findViewById(R.id.NBA);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.nba_lg_id));
                return;
            case R.id.NCAAMB /*2131230804*/:
                getActivity().findViewById(R.id.NCAAMB);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.NCAAMB_lg_id));
                return;
            case R.id.NFL /*2131230804*/:
                getActivity().findViewById(R.id.NFL);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.nfl_lg_id));
                return;
            case R.id.NHL /*2131230804*/:
                getActivity().findViewById(R.id.NHL);//.setSelected(true);
                sethomeOther(getResources().getString(R.string.nhl_lg_id));
                return;
            case R.id.PGA /*2131230804*/:
                getActivity().findViewById(R.id.PGA);
                sethomeOther(getResources().getString(R.string.pga_lg_id));
                return;

            default:
                return;
        }
    }

    private void sethomeAll() {
        Fragment someFragment2 = new AllHomeFragment();
        FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
        transaction2.replace(R.id.home_frame_layout, someFragment2); // give your fragment container id in first parameter
        transaction2.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction2.commit();
    }

    public void sethomeOther(String s_lid) {
        Bundle bundle = new Bundle();
        bundle.putString("lid", s_lid);
        Fragment someFragment2 = new HomeCardsFragment();
        someFragment2.setArguments(bundle);
        FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
        transaction2.replace(R.id.home_frame_layout, someFragment2); // give your fragment container id in first parameter
        transaction2.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction2.commit();
    }


}
