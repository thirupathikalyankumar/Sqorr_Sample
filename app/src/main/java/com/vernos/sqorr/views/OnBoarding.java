package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.vernos.sqorr.R;
import com.vernos.sqorr.adapters.PagerAdapter;
import com.vernos.sqorr.fragments.PagerFragment;
import com.vernos.sqorr.utilities.Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class OnBoarding extends AppCompatActivity implements View.OnClickListener{
    private List<Fragment> fragments;
    private String TAG = OnBoarding.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.init_status_bar_color));
        }
        init();
//        Utilities.showAlertBox(OnBoarding.this,"My Title","My Message");


        printKeyHash(this);



    }


    //To Print the KeyHash of Fb in Logcat
    @SuppressLint("PackageManagerGetSignatures")
    public static void printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                System.out.println("Key Hash=> " + key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.d("OnBoard::", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("OnBoard::", e.toString());
        } catch (Exception e) {
            Log.d("OnBoard::", e.toString());
        }
    }

    TextView letsplay;

    private void init() {
        fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(getBundle(R.drawable.sqorr_logo, getResources().getString(R.string.welcome_msg), "",0));
        fragments.add(getBundle(R.drawable.sport_graphic, getResources().getString(R.string.on_boarding_2), getResources().getString(R.string.on_boarding_sub_2),1));
        fragments.add(getBundle(R.drawable.players_graphic, getResources().getString(R.string.on_boarding_3), getResources().getString(R.string.on_boarding_sub_3),2));
        fragments.add(getBundle(R.drawable.prize_graphic, getResources().getString(R.string.on_boarding_4), getResources().getString(R.string.on_boarding_sub_4),3));
         letsplay = findViewById(R.id.pagerBT);
        TextView btnSignup = (TextView) findViewById(R.id.btnSignup);
        TextView btnLogin = (TextView) findViewById(R.id.btnLogin);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        CircleIndicator circleIndicator = findViewById(R.id.indicator);
        letsplay.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        viewPager.setAdapter(new PagerAdapter(this.getSupportFragmentManager(), fragments));
        circleIndicator.setViewPager(viewPager);

    }

    Fragment getBundle(int position, String title, String desc,int pageNo) {
        Log.i(TAG, "position1 = " + position);
        Fragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pic", position);
        bundle.putString("title", title);
        bundle.putString("desc", desc);
        bundle.putInt("pageNo",pageNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignup:
                startActivity(new Intent(this, Signup.class));
                break;
            case R.id.btnLogin:
                startActivity(new Intent(this, Login.class));
                break;
            case  R.id.pagerBT:

                letsplay.setBackgroundResource(R.drawable.btn_bg_red_ripple);


                Intent letsplay = new Intent(this,Dashboard.class);

                letsplay.putExtra("userrole", "0");
                letsplay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(letsplay);

                break;
        }
    }



}

