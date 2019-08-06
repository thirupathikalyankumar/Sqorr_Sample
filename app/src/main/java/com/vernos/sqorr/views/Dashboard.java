package com.vernos.sqorr.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vernos.sqorr.R;
import com.vernos.sqorr.fragments.AllHomeFragment;
import com.vernos.sqorr.fragments.CardSFrag;
import com.vernos.sqorr.fragments.HomeFrag;
import com.vernos.sqorr.fragments.PromosFragment;
import com.vernos.sqorr.fragments.SqoortvFrag;
import com.vernos.sqorr.pojos.DummyContent;
import com.vernos.sqorr.pojos.ResponsePojo;
import com.vernos.sqorr.utilities.Utilities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity implements PromosFragment.OnListFragmentInteractionListener {
    private TextView mTextMessage;

    public static String ROLE = "";
    public static String SESSIONTOKEN = "";
    public static String AMOUNT_TOKEN = "";
    public static String AMOUNT_CASH = "";
    public static String AVATAR = "";
    public static String MYWiNS = "";
    public static String ACCNAME = "";//_intent.putExtra("acc_name", signup_res.getString("fullName"));
//    public static String leagueId="";
//    public static String leagueName="";
//    public static String leagueAbbreviation="";
//    public static String sportId="";


    private Toolbar toolbar;
    ImageView mTitle, img_home, img_para;

    TextView cort_tx, mywins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mTitle = findViewById(R.id.icsett);
        img_home = findViewById(R.id.img_home);
        img_para = findViewById(R.id.img_para);

        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Hai", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(Dashboard.this, Profile.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });


        LinearLayout pro = findViewById(R.id.pro_in);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Hai", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(Dashboard.this, Profile.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView j_tv = findViewById(R.id.joinnow);
        mywins = findViewById(R.id.mywins);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "Exo_Bold.ttf");
        j_tv.setTypeface(tf);
        j_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Dashboard.this, Login.class);
                startActivity(in);
            }
        });

        TextView tokens_tv = findViewById(R.id.rl);
        Typeface tf_tok = Typeface.createFromAsset(getApplicationContext().getAssets(), "Exo_ExtraBold.ttf");
        tokens_tv.setTypeface(tf_tok);

        cort_tx = (TextView) findViewById(R.id.cort_tx);
        cort_tx.setTypeface(tf_tok);


        ROLE = getIntent().getExtras().getString("userrole");


        if (ROLE!=null&&ROLE.equalsIgnoreCase("cash") || ROLE!=null&&ROLE.equalsIgnoreCase("tokens")) {

            AMOUNT_CASH = getIntent().getExtras().getString("amount_cash");
            AMOUNT_TOKEN = getIntent().getExtras().getString("amount_token");
            AVATAR = getIntent().getExtras().getString("avatar");
            MYWiNS = getIntent().getExtras().getString("wins");
            ACCNAME = getIntent().getExtras().getString("acc_name");


        }


        init();


        if (Utilities.isNetworkAvailable(getApplicationContext())) {

            View fl = findViewById(R.id.inc);
            View ull = findViewById(R.id.htabs);
            if (ROLE != null && ROLE.equalsIgnoreCase("0")) {


                fl.setVisibility(View.VISIBLE);
                ull.setVisibility(View.GONE);

            } else if (ROLE.equalsIgnoreCase("cash")) {


                fl.setVisibility(View.GONE);
                ull.setVisibility(View.VISIBLE);
                SESSIONTOKEN = getIntent().getExtras().getString("sessionToken");

                cort_tx.setText(AMOUNT_CASH);
                mywins.setText(MYWiNS);

                cort_tx.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cash, 0, 0, 0);
//                img_home.setImageDrawable(R.drawable.adam_scott);

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.adam_scott));

                img_para.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addFundsIntent = new Intent(Dashboard.this, AddFunds.class);
                        addFundsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(addFundsIntent);
                    }
                });
            } else if (ROLE.equalsIgnoreCase("tokens")) {

                img_para.setVisibility(View.GONE);
                SESSIONTOKEN = getIntent().getExtras().getString("sessionToken");
                fl.setVisibility(View.GONE);
                ull.setVisibility(View.VISIBLE);
                cort_tx.setText(AMOUNT_TOKEN);
                mywins.setText(" " + MYWiNS);
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.adam_scott));
                cort_tx.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_token_sm, 0, 0, 0);

            }

        } else {
            Utilities.showNoInternetAlert(Dashboard.this);
            View fl = findViewById(R.id.inc);
            View ull = findViewById(R.id.htabs);
            if (ROLE != null && ROLE.equalsIgnoreCase("0")) {


                fl.setVisibility(View.VISIBLE);
                ull.setVisibility(View.GONE);

            } else if (ROLE!=null&&ROLE.equalsIgnoreCase("cash")) {


                fl.setVisibility(View.GONE);
                ull.setVisibility(View.VISIBLE);
                SESSIONTOKEN = getIntent().getExtras().getString("sessionToken");

                cort_tx.setText(AMOUNT_CASH);
                mywins.setText(MYWiNS);

                cort_tx.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cash, 0, 0, 0);
//                img_home.setImageDrawable(R.drawable.adam_scott);

                img_home.setImageDrawable(getResources().getDrawable(R.drawable.adam_scott));


            } else if (ROLE.equalsIgnoreCase("tokens")) {

                img_para.setVisibility(View.GONE);
                SESSIONTOKEN = getIntent().getExtras().getString("sessionToken");
                fl.setVisibility(View.GONE);
                ull.setVisibility(View.VISIBLE);
                cort_tx.setText(AMOUNT_TOKEN);
                mywins.setText(MYWiNS);
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.adam_scott));
                cort_tx.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_token_sm, 0, 0, 0);

            }
        }
    }

    private void init() {

        TextView htxt_tv = findViewById(R.id.hometxt);
        TextView cardtxt_tv = findViewById(R.id.cardtxt);
        TextView tvtxt_tv = findViewById(R.id.tvtxt);
        TextView protxt_tv = findViewById(R.id.promotxt);
        Typeface tf_htxt = Typeface.createFromAsset(getApplicationContext().getAssets(), "Exo_SemiBold.ttf");
        htxt_tv.setTypeface(tf_htxt);
        cardtxt_tv.setTypeface(tf_htxt);
        tvtxt_tv.setTypeface(tf_htxt);
        protxt_tv.setTypeface(tf_htxt);

        findViewById(R.id.footer1).setSelected(true);
        findViewById(R.id.footer2).setSelected(false);
        findViewById(R.id.footer3).setSelected(false);
        findViewById(R.id.footer4).setSelected(false);
        setHome();
    }

    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.footer1 /*2131230804*/:
                findViewById(R.id.footer1).setSelected(true);
                findViewById(R.id.footer2).setSelected(false);
                findViewById(R.id.footer3).setSelected(false);
                findViewById(R.id.footer4).setSelected(false);


                setHome();
                return;
            case R.id.footer2 /*2131230805*/:
                navToMyCards();
                return;
            case R.id.footer3 /*2131230806*/:
                findViewById(R.id.footer1).setSelected(false);
                findViewById(R.id.footer2).setSelected(false);
                findViewById(R.id.footer3).setSelected(true);
                findViewById(R.id.footer4).setSelected(false);
//                setTV();
                Fragment someFragment3 = new SqoortvFrag();
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
                transaction3.replace(R.id.frame_layout, someFragment3); // give your fragment container id in first parameter
                transaction3.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction3.commit();
                return;
            case R.id.footer4 /*2131230807*/:
                findViewById(R.id.footer1).setSelected(false);
                findViewById(R.id.footer2).setSelected(false);
                findViewById(R.id.footer3).setSelected(false);
                findViewById(R.id.footer4).setSelected(true);
//                setPromos();
                Fragment someFragment4 = new PromosFragment();
                FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
                transaction4.replace(R.id.frame_layout, someFragment4); // give your fragment container id in first parameter
                transaction4.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction4.commit();
                return;
//            case R.id.joinnow:
//                Intent in = new Intent(Dashboard.this, Login.class);
//                startActivity(in);
//                return;
            default:
                return;
        }
    }

    private void setHome() {
        Fragment someFragment = new HomeFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, someFragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();


    }
//    public void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        pagerAdapter.addFragment(new HomeFrag(), "Frag One");
//        pagerAdapter.addFragment(new HomeFrag(), "Frag Two");
//        pagerAdapter.addFragment(new HomeFrag(), "Frag three");
//        pagerAdapter.addFragment(new HomeFrag(), "Frag four");
//        pagerAdapter.addFragment(new HomeFrag(), "Frag five");
//        pagerAdapter.addFragment(new HomeFrag(), "Frag Six");
//        viewPager.setAdapter(pagerAdapter);
//    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    public void navToMyCards() {
        findViewById(R.id.footer1).setSelected(false);
        findViewById(R.id.footer2).setSelected(true);
        findViewById(R.id.footer3).setSelected(false);
        findViewById(R.id.footer4).setSelected(false);

        Fragment someFragment2 = new CardSFrag();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();//getFragmentManager().beginTransaction();
        transaction2.replace(R.id.frame_layout, someFragment2); // give your fragment container id in first parameter
        transaction2.addToBackStack(null);  // if written, this transaction will be added to back stack
        transaction2.commit();
    }
}