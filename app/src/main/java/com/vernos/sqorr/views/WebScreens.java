package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.WebLinksPojo;
import com.vernos.sqorr.ui.AppConstants;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.Utilities;

import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WebScreens extends AppCompatActivity implements View.OnClickListener {

     private   String title = "";
     private ArrayList<WebLinksPojo> webLinksList=new ArrayList<>();
     private WebView htmlWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_screens);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if (bundle.containsKey("title"))
                title=bundle.getString("title");
        }

        init();
        getWebPageData();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        toolbar_title_x.setText(title);
        htmlWebView =findViewById(R.id.webu);
        htmlWebView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);

    }

    private void getWebPageData() {
        AndroidNetworking.get(APIs.WEB_LINKS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.e("93-----------", response.toString());
                        try {
                            Type listType = new TypeToken<List<WebLinksPojo>>() {
                            }.getType();
                            webLinksList = new Gson().fromJson(response.toString(), listType);
                            if (title != null && !title.equals("")) {
                                if (title.equalsIgnoreCase(AppConstants.ABOUT_VETNOS)) {
                                    htmlWebView.loadUrl(webLinksList.get(0).getPageUrl());
                                } else if (title.equalsIgnoreCase(AppConstants.HOW_TO_PLAY)) {
                                    htmlWebView.loadUrl(webLinksList.get(1).getPageUrl());
                                } else if (title.equalsIgnoreCase(AppConstants.FAQS)) {
                                    htmlWebView.loadUrl(webLinksList.get(2).getPageUrl());
                                } else if (title.equalsIgnoreCase(AppConstants.CUSTOMER_SUPPORT)) {
                                    htmlWebView.loadUrl(webLinksList.get(3).getPageUrl());
                                } else if (title.equalsIgnoreCase(AppConstants.PRIVACY_POLICY)) {
                                    htmlWebView.loadUrl(webLinksList.get(4).getPageUrl());
                                } else if (title.equalsIgnoreCase(AppConstants.TERMS_OF_SERVICE)) {
                                    htmlWebView.loadUrl(webLinksList.get(4).getPageUrl());
                                }
                            }else{
                                htmlWebView.loadUrl(webLinksList.get(0).getPageUrl());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Utilities.showToast(WebScreens.this,error.getErrorBody());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title_x:
//                overridePendingTransition(  R.anim.slide_down,R.anim.slide_up );
                finish();
                break;

        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
