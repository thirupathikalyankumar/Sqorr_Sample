package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.vernos.sqorr.R;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.Response;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText edOldPassword,edNewPassword,edConfirmNewPassword;
    private TextView btnSubmit,tvAtLeastEightChars,tvAtLeastOneNumber,tvOnceSpecialChar;
    private String sessionToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_TOKEN", MODE_PRIVATE);
        sessionToken= sharedPreferences.getString("token", "");

        init();

    }

    private void init(){
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        edOldPassword=findViewById(R.id.edOldPassword);
        edNewPassword=findViewById(R.id.edNewPassword);
        edConfirmNewPassword=findViewById(R.id.edConfirmNewPassword);
        tvAtLeastEightChars=findViewById(R.id.tvAtLeastEightChars);
        tvAtLeastOneNumber=findViewById(R.id.tvAtLeastOneNumber);
        tvOnceSpecialChar=findViewById(R.id.tvOnceSpecialChar);
        btnSubmit =findViewById(R.id.btnSubmit);

        toolbar_title_x.setText(getString(R.string.change_password));
        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        edOldPassword.addTextChangedListener(this);
        edNewPassword.addTextChangedListener(this);
        edConfirmNewPassword.addTextChangedListener(this);
        btnSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title_x:
                finish();
                break;
            case R.id.btnSubmit:
                submitData();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        int old_length=edOldPassword.getText().toString().trim().length();
        int new_length=edNewPassword.getText().toString().trim().length();
        int cnf_length=edConfirmNewPassword.getText().toString().trim().length();

        String newPwdStr=edNewPassword.getText().toString();

        passwordValidation(new_length,newPwdStr);

        submitValidation(old_length,new_length,cnf_length);

    }



    private void passwordValidation(int pwd_length,String  pwdStr){

        if (pwd_length>=8){
            tvAtLeastEightChars.setTextColor(getResources().getColor(R.color.bg_green));
            tvAtLeastEightChars.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_success, 0, 0, 0);
        }else{
            tvAtLeastEightChars.setTextColor(getResources().getColor(R.color.validation_color));
            tvAtLeastEightChars.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_wrong, 0, 0, 0);
        }


        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwdStr);

        boolean isMatched = matcher.matches();

        try {
            if (isMatched){
                tvAtLeastOneNumber.setTextColor(getResources().getColor(R.color.bg_green));
                tvAtLeastOneNumber.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_success, 0, 0, 0);
            } else {
                tvAtLeastOneNumber.setTextColor(getResources().getColor(R.color.validation_color));
                tvAtLeastOneNumber.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_wrong, 0, 0, 0);
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
            Log.d("ERROR", ex.toString());
        }

        String regex1 = "\\p{Punct}"; //Special character : `~!@#$%^&*()-_+=\|}{]["';:/?.,><

        Pattern  pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(pwdStr);

        if (matcher1.find()){
            Log.d("YES::","we had a special char");
            tvOnceSpecialChar.setTextColor(getResources().getColor(R.color.bg_green));
            tvOnceSpecialChar.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_success, 0, 0, 0);
        }else{
            Log.d("NO::","special char");
            tvOnceSpecialChar.setTextColor(getResources().getColor(R.color.validation_color));
            tvOnceSpecialChar.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_wrong, 0, 0, 0);
        }


    }

    private void submitValidation(int old_pwd,int  new_pwd,int confirm_pwd){
        String newStr =edNewPassword.getText().toString();
        String confirmStr=edConfirmNewPassword.getText().toString();
        if(old_pwd>0&&new_pwd>0&&confirm_pwd>0&&newStr.equals(confirmStr)){
            btnSubmit.setEnabled(true);
            btnSubmit.setTextColor(getResources().getColor(R.color.white));
            btnSubmit.setBackgroundResource(R.drawable.btn_bg_red);
        }else {
            btnSubmit.setEnabled(false);
            btnSubmit.setTextColor(getResources().getColor(R.color.btn_dis_text));
            btnSubmit.setBackgroundResource(R.drawable.login_bg_disable);
        }
    }

    //Service call to send data to server
    private void submitData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldpassword", edOldPassword.getText().toString().trim());
            jsonObject.put("newpassword", edNewPassword.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(APIs.CHANGE_PWD_URL)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders("Content-Type", "application/json")
                .addHeaders("sessionToken", sessionToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if(response.code()!=200) {
//                            Utilities.showToast(getApplicationContext(), ""+response.message());
                            Utilities.showAlertBox(ChangePassword.this," ",response.message());

                        }
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
//                        Utilities.showToast(getApplicationContext(),""+anError.getErrorBody());
                        Utilities.showAlertBox(ChangePassword.this," ",anError.getErrorBody());
                        finish();
                    }

                });

    }
}
