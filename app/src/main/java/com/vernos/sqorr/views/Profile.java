package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vernos.sqorr.R;
import com.vernos.sqorr.ui.AppConstants;


public class Profile extends AppCompatActivity implements View.OnClickListener {

    private TextView login_txt, edit_profile,jn_home,tv_cash_toker,tvName,tvProfileScore;
    private LinearLayout ll_add_funds, ull;
    private FrameLayout fl;
    private RelativeLayout viewTransactions;

    LinearLayout how_ll, faq_ll, cust_ll, pp_ll, terms_ll, about_ll;
    ImageView cash_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }
        init();

    }

    private void init() {
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        login_txt = findViewById(R.id.tvSignOut);
        edit_profile = findViewById(R.id.tVEditProfile);
        fl = findViewById(R.id.notlogin);
        ull = findViewById(R.id.userlogin);
        ll_add_funds = findViewById(R.id.ll_add_funds);
        viewTransactions = findViewById(R.id.viewTransactions);
        how_ll = findViewById(R.id.linearViewHowToPaly);
        faq_ll = findViewById(R.id.linearViewFAQs);
        cust_ll = findViewById(R.id.linearViewCustomerSupport);
        pp_ll = findViewById(R.id.linearViewPrivacyPolicy);
        terms_ll = findViewById(R.id.linearViewTermsOfService);
        about_ll = findViewById(R.id.linearViewAboutVetnos);
        cash_add = findViewById(R.id.imgProfileAdd);
        jn_home = findViewById(R.id.jn);
        tv_cash_toker = findViewById(R.id.tv_cash_toker);
        tvName = findViewById(R.id.tvName);
        tvProfileScore = findViewById(R.id.tvProfileScore);

        toolbar_title_x.setText(getString(R.string._account));

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        login_txt.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        ll_add_funds.setOnClickListener(this);
        viewTransactions.setOnClickListener(this);
        how_ll.setOnClickListener(this);
        faq_ll.setOnClickListener(this);
        cust_ll.setOnClickListener(this);
        pp_ll.setOnClickListener(this);
        terms_ll.setOnClickListener(this);
        about_ll.setOnClickListener(this);
        jn_home.setOnClickListener(this);
        updateUI();
    }


    @SuppressLint("SetTextI18n")
    private void updateUI() {
        String rrole = Dashboard.ROLE;

        if (rrole != null && rrole.equalsIgnoreCase("0")) {
            login_txt.setText("Log in");
            fl.setVisibility(View.VISIBLE);
            ull.setVisibility(View.GONE);
        } else if (rrole!=null&&rrole.equalsIgnoreCase("cash")) {
            login_txt.setText("Sign out");
            fl.setVisibility(View.GONE);
            ull.setVisibility(View.VISIBLE);
            cash_add.setVisibility(View.VISIBLE);

            tv_cash_toker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cash, 0, 0, 0);
            tvName.setText(Dashboard.ACCNAME);
            tvProfileScore.setText(Dashboard.MYWiNS);
        } else if (rrole!=null&&rrole.equalsIgnoreCase("tokens")) {
            login_txt.setText("Sign out");
            fl.setVisibility(View.GONE);
            ull.setVisibility(View.VISIBLE);
            cash_add.setVisibility(View.GONE);
            tvName.setText(Dashboard.ACCNAME);
            tvProfileScore.setText(Dashboard.MYWiNS);
            tv_cash_toker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_token_sm, 0, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title_x:
//                overridePendingTransition(  R.anim.slide_down,R.anim.slide_up );
                finish();
                break;
            case R.id.jn:
                Intent in1 = new Intent(Profile.this, Login.class);
                startActivity(in1);
                break;
            case R.id.tvSignOut:
                if (login_txt.getText().equals("Log in")) {
                    Intent in = new Intent(Profile.this, Login.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(Profile.this, OnBoarding.class);
                    startActivity(in);
                }
                break;
            case R.id.ll_add_funds:
                Intent addFundsIntent = new Intent(Profile.this, AddFunds.class);
                addFundsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(addFundsIntent);
                break;

            case R.id.viewTransactions:
                Intent transactionsIntent = new Intent(Profile.this, TransactionsActivity.class);
                transactionsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(transactionsIntent);
                break;
            case R.id.tVEditProfile:
                Intent profileEditIntent = new Intent(Profile.this, ProfileEdit.class);
//                profileEditIntent.putExtra("sessionToken", sessionToken);
//                profileEditIntent.putExtra("acc_name", playerMode);
//                profileEditIntent.putExtra("avatar", avatar);
//                profileEditIntent.putExtra("amount_cash", amount_cash);
//                profileEditIntent.putExtra("amount_token", amount_token);
                profileEditIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(profileEditIntent);
                break;
            case R.id.linearViewHowToPaly:
                Intent how_Intent = new Intent(Profile.this, HowToPlay.class);
                how_Intent.putExtra("title", AppConstants.HOW_TO_PLAY);
                how_Intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(how_Intent);
                break;
            case R.id.linearViewFAQs:
                Intent faq_Intent = new Intent(Profile.this, WebScreens.class);
                faq_Intent.putExtra("title", AppConstants.FAQS);
                faq_Intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(faq_Intent);
                break;
            case R.id.linearViewCustomerSupport:
                Intent cust_Intent = new Intent(Profile.this, WebScreens.class);
                cust_Intent.putExtra("title", AppConstants.CUSTOMER_SUPPORT);
                cust_Intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(cust_Intent);
                break;
            case R.id.linearViewPrivacyPolicy:
                Intent pp_Intent = new Intent(Profile.this, WebScreens.class);
                pp_Intent.putExtra("title", AppConstants.PRIVACY_POLICY);
                pp_Intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(pp_Intent);
                break;
            case R.id.linearViewTermsOfService:
                Intent tos_Intent = new Intent(Profile.this, WebScreens.class);
                tos_Intent.putExtra("title", AppConstants.TERMS_OF_SERVICE);
                tos_Intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(tos_Intent);
                break;
            case R.id.linearViewAboutVetnos:
                Intent about_Intent = new Intent(Profile.this, WebScreens.class);
                about_Intent.putExtra("title", AppConstants.ABOUT_VETNOS);
                about_Intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(about_Intent);
                break;
        }
    }
}
