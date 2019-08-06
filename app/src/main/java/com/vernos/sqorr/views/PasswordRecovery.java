package com.vernos.sqorr.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.ResponsePojo;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class PasswordRecovery extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText et_email_address;
    private TextView tv_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }
        init();
    }
    private void init(){
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        et_email_address=findViewById(R.id.et_email_address);
        tv_submit=findViewById(R.id.tv_submit);

        toolbar_title_x.setText(getString(R.string.pwd_recovery));

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        et_email_address.addTextChangedListener(this);
        tv_submit.setEnabled(false);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title_x:
                finish();
                break;
            case R.id.tv_submit:
               submitData();
                break;
             default:break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
            int email_length=et_email_address.getText().toString().trim().length();
            if(email_length>0){
                tv_submit.setEnabled(true);
                tv_submit.setTextColor(getResources().getColor(R.color.white));
                tv_submit.setBackgroundResource(R.drawable.btn_bg_red);
            }else{
                tv_submit.setEnabled(false);
                tv_submit.setTextColor(getResources().getColor(R.color.btn_dis_text));
                tv_submit.setBackgroundResource(R.drawable.login_bg_disable);
            }
    }


    //Service call to send data to server
    private void submitData(){
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", et_email_address.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(APIs.FORGOT_PASSWORD_URL)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if(response.code()==200) {
//                            Utilities.showToast(getApplicationContext(), "HUrray");
                            Utilities.showAlertBox(PasswordRecovery.this,"Email sent successfully",getResources().getString(R.string.rec_pwd));

                        }else{
                            Utilities.showToast(getApplicationContext(), ""+response.message());
//                            Utilities.showAlertBox(PasswordRecovery.this,"Email sent successfully","Please check email for password reset instructions");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Utilities.showToast(getApplicationContext(),""+anError.getErrorBody());
                    }
                });


    }
}
