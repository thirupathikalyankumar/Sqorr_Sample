package com.vernos.sqorr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vernos.sqorr.R;
import com.vernos.sqorr.adapters.NewPlayerListAdapter;
import com.vernos.sqorr.adapters.PicksAdapter;
import com.vernos.sqorr.model.NewPlayerStatistics;
import com.vernos.sqorr.model.Picks;
import com.vernos.sqorr.pojos.MatchupModel;
import com.vernos.sqorr.ui.AppConstants;
import com.vernos.sqorr.utilities.APIs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MatchupScreen extends AppCompatActivity implements View.OnClickListener {
    BottomSheetBehavior sheetBehavior;
    RecyclerView playerGridView;
    List<NewPlayerStatistics> mNewPlayerStatisticsList = new ArrayList<>();
    Button twox, fivex, tenx, fivteenx, twentyx;
    TextView no_of_picks_count, multiplier_count, winpayout, dollarchangetxt;
    Button playBtn;
    LinearLayout layoutBottomSheet;
    List<Map<String, Object>> playerA_Stats = new ArrayList<>();
    List<Map<String, Object>> playerB_Stats = new ArrayList<>();

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    String getcardID, cardColor,Legue_id;
    ProgressDialog loading = null;
    Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_screen);


        Bundle bundle =getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("cardid"))
                getcardID = bundle.getString("cardid");

            if(bundle.containsKey("cardid_color"))
                cardColor=bundle.getString("cardid_color");
            if(bundle.containsKey("cardid_color1"))
                Legue_id=bundle.getString("cardid_color1");

            Log.e("C id----", getcardID + "--" + cardColor);
        }



/*
        if (getIntent().getExtras().getString("cardid") != null) {
            getcardID = getIntent().getExtras().getString("cardid");
            cardColor = getIntent().getExtras().getString("cardid_color");
            Legue_id = getIntent().getExtras().getString("cardid_color1");
        }
*/


        initViews();
        initApiCall();
    }
    ProgressBar progressBar;
    RelativeLayout match_header;
    private void initViews() {
        progressBar = findViewById(R.id.indeterminateBar);

        progressBar.setVisibility(View.VISIBLE);
        ImageView arrow_up;
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        match_header = findViewById(R.id.match_header);

        //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.init_status_bar_color));
        //   }

        final String leagueName = Legue_id;

        if (leagueName != null){

            switch (leagueName) {
                case "NFL":
                case "LA-LIGA":
                    match_header.setBackgroundColor(getResources().getColor(R.color.foot_ball_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.foot_ball_color));
                    break;

                case "NBA":
                case "NCAAMB":
                    match_header.setBackgroundColor(getResources().getColor(R.color.basket_ball_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.basket_ball_color));
                    break;

                case "NHL":
                    match_header.setBackgroundColor(getResources().getColor(R.color.hockey_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.hockey_color));
                    break;

                case "NASCAR":
                    match_header.setBackgroundColor(getResources().getColor(R.color.car_race_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.car_race_color));
                    break;

                case "MLB":
                    match_header.setBackgroundColor(getResources().getColor(R.color.base_ball_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.base_ball_color));
                    break;

                case "EPL":
                    match_header.setBackgroundColor(getResources().getColor(R.color.tennis_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.tennis_color));
                    break;

               /* case "LA-LIGA":
                    match_header.setBackgroundColor(getResources().getColor(R.color.foot_ball_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.foot_ball_color));
                    break;*/

                case "MLS":
                    match_header.setBackgroundColor(getResources().getColor(R.color.soccer_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.soccer_color));
                    break;

              /*  case "NCAAMB":
                    match_header.setBackgroundColor(getResources().getColor(R.color.basket_ball_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.basket_ball_color));
                    break;*/

                case "PGA":
                    match_header.setBackgroundColor(getResources().getColor(R.color.golf_color_org));
                    window.setStatusBarColor(getResources().getColor(R.color.golf_color));
                    break;
                default:
                    break;
            }
        }else{
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }

        layoutBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        twox = (Button) layoutBottomSheet.findViewById(R.id.twox);
        fivex = (Button) layoutBottomSheet.findViewById(R.id.fivex);
        tenx = (Button) layoutBottomSheet.findViewById(R.id.tenx);
        fivteenx = (Button) layoutBottomSheet.findViewById(R.id.fivteenx);
        twentyx = (Button) layoutBottomSheet.findViewById(R.id.twentyx);
        no_of_picks_count = (TextView) layoutBottomSheet.findViewById(R.id.no_of_picks_count);
        multiplier_count = (TextView) layoutBottomSheet.findViewById(R.id.multiplier_count);
        winpayout = (TextView) layoutBottomSheet.findViewById(R.id.winpayout);
        dollarchangetxt = (TextView) layoutBottomSheet.findViewById(R.id.dollarchangetxt);
        playBtn = (Button) layoutBottomSheet.findViewById(R.id.playBtn);
        twox.setOnClickListener(this);
        fivex.setOnClickListener(this);
        tenx.setOnClickListener(this);
        fivteenx.setOnClickListener(this);
        twentyx.setOnClickListener(this);
        LinearLayout apply_promo_code = (LinearLayout) findViewById(R.id.apply_promo_code);
        RecyclerView content_rv = (RecyclerView) findViewById(R.id.content_rv);
        arrow_up = (ImageView) findViewById(R.id.arrow_up);
        playerGridView = (RecyclerView) findViewById(R.id.playerGridView);
        TextView toolbar_title_x = (TextView) findViewById(R.id.toolbar_title_x);
        arrow_up.setOnClickListener(MatchupScreen.this);
        toolbar_title_x.setOnClickListener(this);
        apply_promo_code.setOnClickListener(this);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
        List<Picks> picksList = new ArrayList<>();

        for (int i = 0; i < getResources().getStringArray(R.array.picks).length; i++) {
            Picks mPicks = new Picks();
            mPicks.setMultiplier(getResources().getStringArray(R.array.multiplier)[i]);
            mPicks.setPicks(getResources().getStringArray(R.array.picks)[i]);
            mPicks.setWinpayout(getResources().getStringArray(R.array.winpayout)[i]);
            picksList.add(mPicks);
        }

        CustomLinearLayoutManager custom = new CustomLinearLayoutManager(getApplicationContext());
        content_rv.setLayoutManager(custom);
        PicksAdapter picksAdapter = new PicksAdapter(picksList, this);
        content_rv.setAdapter(picksAdapter);



    }

    private void initApiCall() {

        if( !TextUtils.isEmpty(Dashboard.SESSIONTOKEN)){
            getApiCallWith(Dashboard.SESSIONTOKEN);
        }else{
            getApiWithOut();
        }

    }
    private void getApiWithOut() {




        AndroidNetworking.get(APIs.CARD_DETAILS + getcardID + "/matchups")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("***match : :", response.toString());
                        JSONArray jsonArray = new JSONArray();

                        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
                        final Gson gson = builder.create();
                        final  Type type = new TypeToken<MatchupModel>() {
                        }.getType();

                        try {

                            final MatchupModel matchupModel = gson.fromJson(response.toString(), type);



                                String json = response.toString();
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                jsonArray = jsonObject.getJSONArray("matchups");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < matchupModel.getMatchups().size(); i++) {
                                try {
                                    if (!jsonArray.getJSONObject(i).isNull("playerA") && !jsonArray.getJSONObject(i).getJSONObject("playerA").isNull("avgStats")) {
                                        Map<String, Object> playerA = toMap(jsonArray.getJSONObject(i).getJSONObject("playerA").getJSONObject("avgStats").getJSONObject("stats"));
                                        playerA_Stats.add(playerA);
                                    } else {
                                        JSONObject playerA_dummy_jsonObject = new JSONObject(AppConstants.PLAYERA_DUMMY_JSON);
                                        Map<String, Object> playerA = toMap(playerA_dummy_jsonObject);
                                        playerA_Stats.add(playerA);
                                    }
                                    if (!jsonArray.getJSONObject(i).isNull("playerB") && !jsonArray.getJSONObject(i).getJSONObject("playerB").isNull("avgStats")) {
                                        Map<String, Object> playerB = toMap(jsonArray.getJSONObject(i).getJSONObject("playerB").getJSONObject("avgStats").getJSONObject("stats"));
                                        playerB_Stats.add(playerB);
                                    } else {
                                        Map<String, Object> playerB = new HashMap<>();
                                        playerB_Stats.add(playerB);

                       /* if (!jsonArray.getJSONObject(i).isNull("playerA") && !jsonArray.getJSONObject(i).getJSONObject("playerA").isNull("avgStats")) {
                            Map<String, Object> playerB = toMap(jsonArray.getJSONObject(i).getJSONObject("playerA").getJSONObject("avgStats").getJSONObject("stats"));
                            playerB_Stats.add(playerB);
                        } else {
                            JSONObject playerB_dummy_jsonObject = new JSONObject(AppConstants.PLAYERB_DUMMY_JSON);
                            Map<String, Object> playerB = toMap(playerB_dummy_jsonObject);
                            playerB_Stats.add(playerB);
                        }*/

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            for (int j = 0; j < matchupModel.getMatchups().size(); j++) {
                                List<Double> first_A_points = new ArrayList<>();
                                List<String> first_B_points = new ArrayList<>();
                                List<String> keyNames = new ArrayList<>();
                                Set<String> A = playerA_Stats.get(j).keySet();
                                Set<String> B = playerB_Stats.get(j).keySet();
                                Iterator<String> a = A.iterator();

                                while (a.hasNext()) {
                                    String playerA_object_data = a.next();
                                    // recreate iterator for second list
                                    Iterator<String> secondIt = B.iterator();
                                    if (!secondIt.hasNext()) {
                                        first_B_points.add("000");
                                        try {
                                            JSONObject AjsonObject = new JSONObject(playerA_Stats.get(j).get(playerA_object_data).toString());
                                            first_A_points.add(AjsonObject.getDouble("value"));
                                            keyNames.add(playerA_object_data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    while (secondIt.hasNext()) {
                                        String playerB_object_data = secondIt.next();
                                        if (playerA_object_data.equals(playerB_object_data)) {
                                            JSONObject AjsonObject = null;
                                            JSONObject BjsonObject = null;
                                            Log.v("KEY" + j + "==>", playerA_object_data + "value==>" + playerA_Stats.get(j).get(playerA_object_data));
                                            try {
                                                AjsonObject = new JSONObject(playerA_Stats.get(j).get(playerA_object_data).toString());
                                                BjsonObject = new JSONObject(playerB_Stats.get(j).get(playerA_object_data).toString());
                                                Log.v("VALUE==>", AjsonObject.getDouble("value") + "");
                                                first_A_points.add(AjsonObject.getDouble("value"));
                                                first_B_points.add(String.valueOf(BjsonObject.getDouble("value")));
                                                keyNames.add(playerA_object_data);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                NewPlayerStatistics mNewPlayerStatistics = new NewPlayerStatistics();
                                mNewPlayerStatistics.setFirst_player_points(first_A_points);
                                mNewPlayerStatistics.setSecond_player_points(first_B_points);
                                mNewPlayerStatistics.setPlayer_positions(keyNames);
                                mNewPlayerStatisticsList.add(mNewPlayerStatistics);

                            }


                            if (matchupModel.getIsPurchased()) {
                                layoutBottomSheet.setVisibility(View.GONE);
                            } else {
                                layoutBottomSheet.setVisibility(View.VISIBLE);
                            }
                            no_of_picks_count.setText("0" + '/' + matchupModel.getMatchups().size());


                            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
//                            playerGridView.setLayoutManager(gridLayoutManager);
                            playerGridView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            playerGridView.setItemAnimator(new DefaultItemAnimator());

                            NewPlayerListAdapter playerListAdapter = new NewPlayerListAdapter(matchupModel.getMatchups(), mNewPlayerStatisticsList, matchupModel.getIsPurchased(), MatchupScreen.this, new NewPlayerListAdapter.OnItemClick() {
                                @Override
                                public void onClick(List<String> first_player_list, List<String> second_player_list, List<String> matchup_Ids, List<String> picIndex_Ids, List<String> over_pickIndex_Ids, List<String> under_pickIndex_Ids) {
                                    if ((first_player_list.size() + second_player_list.size() + over_pickIndex_Ids.size() + under_pickIndex_Ids.size()) >= 4 && ((first_player_list.size() + second_player_list.size() + over_pickIndex_Ids.size() + under_pickIndex_Ids.size()) <= 9)) {
                                        playBtn.setBackgroundColor(getResources().getColor(R.color.darkred));
                                        playBtn.setEnabled(true);
                                    } else {
                                        playBtn.setBackgroundColor(getResources().getColor(R.color.hint));
                                        playBtn.setEnabled(false);
                                    }
                                    setUpPicksMultiplerWinPayOut(first_player_list.size() + second_player_list.size() + over_pickIndex_Ids.size() + under_pickIndex_Ids.size());
                                    no_of_picks_count.setText(first_player_list.size() + second_player_list.size() + over_pickIndex_Ids.size() + under_pickIndex_Ids.size() + "/" + matchupModel.getMatchups().size());
                                }
                            });
                            playerGridView.setAdapter(playerListAdapter);
//                            playBtn.setOnClickListener(this);
//                            progressBar.setVisibility(0);    --visible
//                            progressBar.setVisibility(4);    --invisible
                            progressBar.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("js", "Login----error-------" + anError);

                        if (anError.getErrorCode() != 0) {
                            Log.d("", "onError errorCode : " + anError.getErrorCode());
                            Log.d("", "onError errorBody : " + anError.getErrorBody());
                            Log.d("", "onError errorDetail : " + anError.getErrorDetail());

                        } else {
                            Log.d("", "onError errorDetail  0: " + anError.getErrorDetail());
                        }

                    }
                });


    }
//                .addHeaders("sessionToken", sessiontoken)

    private void getApiCallWith(String sessiontoken) {

    }

    private void setUpPicksMultiplerWinPayOut(int picks) {
        switch (picks) {
            case 4:
                multiplier_count.setText("X10");
                winpayout.setText("$100");
                break;
            case 5:
                multiplier_count.setText("X18");
                winpayout.setText("$180");
                break;
            case 6:
                multiplier_count.setText("X35");
                winpayout.setText("$350");
                break;
            case 7:
                multiplier_count.setText("X70");
                winpayout.setText("$700");
                break;
            case 8:
                multiplier_count.setText("X125");
                winpayout.setText("$1250");
                break;
            case 9:
                multiplier_count.setText("X250");
                winpayout.setText("$2500");
                break;
            default:
                multiplier_count.setText("X0");
                winpayout.setText("$0");
                break;

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playBtn:


                break;
        }

    }


}