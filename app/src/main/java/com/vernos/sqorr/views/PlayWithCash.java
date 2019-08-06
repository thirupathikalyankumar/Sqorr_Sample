package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.vernos.sqorr.BuildConfig;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.ResponsePojo;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PlayWithCash extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText et_full_name, et_password, et_email_address, et_dob, et_ph_no, et_promo_code;
    private TextView tv_sign_up,tvAtLeastEightChars,tvAtLeastOneNumber,tvOnceSpecialChar,tvEmailError,tvPromoError,tvPromoS;;
    private View email_view,promo_view;
    private String userId,userToken;
    private String signUpOrigin="EMAIL";
    private CallbackManager mFacebookCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private String userEmail, userFullName;

    private static final int RC_SIGN_IN = 20;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_cash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }
        init();
        getUserData();
        configureFb();
        configureGoogleLogin();
    }

    private void init() {
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        TextView already_title_x = findViewById(R.id.already_have_account);

        et_full_name = findViewById(R.id.et_full_name);
        et_email_address = findViewById(R.id.et_email_address);
        et_password = findViewById(R.id.et_password);
        et_dob = findViewById(R.id.et_dob);
        et_ph_no = findViewById(R.id.et_ph_no);
        et_promo_code = findViewById(R.id.et_promo_code);
        tv_sign_up = findViewById(R.id.tv_sign_up);
        tv_sign_up=findViewById(R.id.tv_sign_up);
        tvAtLeastEightChars=findViewById(R.id.tvAtLeastEightChars);
        tvAtLeastOneNumber=findViewById(R.id.tvAtLeastOneNumber);
        tvOnceSpecialChar=findViewById(R.id.tvOnceSpecialChar);
        tvEmailError=findViewById(R.id.tvEmailError);
        email_view=findViewById(R.id.email_view);
        CardView cvFacebook = findViewById(R.id.cvFacebook);
        CardView cvGmail = findViewById(R.id.cvGmail);
        tvPromoError = findViewById(R.id.tvPromoError);
        tvPromoS = findViewById(R.id.tvPromos);
        promo_view = findViewById(R.id.promo_view);


        toolbar_title_x.setText(getString(R.string.sign_up));

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        already_title_x.setOnClickListener(this);
        et_full_name.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_email_address.addTextChangedListener(this);
        et_dob.addTextChangedListener(this);
        et_dob.setClickable(true);
        et_dob.setOnClickListener(this);
        tv_sign_up.setEnabled(false);
        tv_sign_up.setOnClickListener(this);
        cvFacebook.setOnClickListener(this);
        cvGmail.setOnClickListener(this);

    }

    private void getUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        String uEmail = sharedPreferences.getString("user_email", "");
        String uFullName = sharedPreferences.getString("user_name", "");

        et_email_address.setText(uEmail);
        et_email_address.setSelection(uEmail.length());

        et_full_name.setText(uFullName);
        et_full_name.setSelection(uFullName.length());

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title_x:
                finish();
                break;
            case R.id.already_have_account:
                Intent in = new Intent(PlayWithCash.this, Login.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                break;
            case R.id.tv_sign_up:
                tv_sign_up.setBackgroundResource(R.drawable.btn_bg_red_ripple);
                int promos = et_promo_code.getText().toString().trim().length();
                if (promos == 0) {
                    submitSignUpData();
                } else if (promos >= 6) {
                    et_promo_code.setEnabled(false);
                    tvPromoS.setText("$10 Bonus cash on signup");
                    tvPromoS.setVisibility(View.VISIBLE);
                    tvPromoError.setVisibility(View.GONE);

                    submitSignUpData();
                } else if (promos >= 1 && promos < 6) {
                    et_promo_code.requestFocus();
                    tvPromoError.setVisibility(View.VISIBLE);
                    promo_view.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }
                break;
            case R.id.et_dob:
                showCalendar();
                break;
            case R.id.cvFacebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.cvGmail:
                googleSignIn();
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

        int name_length = et_full_name.getText().toString().trim().length();
        int dob_length = et_dob.getText().toString().trim().length();


        tvEmailError.setVisibility(View.GONE);
        email_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.sep_view_color,null));
        int email_length=et_email_address.getText().toString().trim().length();
        int pwd_length=et_password.getText().toString().trim().length();
        String pwdStr=et_password.getText().toString();

        passwordValidation(pwd_length,pwdStr);

        submitValidation(name_length,email_length,pwd_length,dob_length);

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

    private void submitValidation(int name_length,int email_length,int pwd_length,int dob_length){
        if (name_length > 0 && email_length > 0 && pwd_length > 0 && dob_length > 0) {
            tv_sign_up.setEnabled(true);
            tv_sign_up.setTextColor(getResources().getColor(R.color.white));
            tv_sign_up.setBackgroundResource(R.drawable.btn_bg_red);
        } else {
            tv_sign_up.setEnabled(false);
            tv_sign_up.setTextColor(getResources().getColor(R.color.btn_dis_text));
            tv_sign_up.setBackgroundResource(R.drawable.login_bg_disable);
        }
    }
    private void showCalendar(){
        Calendar calendar1 = Calendar.getInstance();

        DatePickerDialog datePickerDialog =new DatePickerDialog(PlayWithCash.this, R.style.MyDatePickerDialogTheme,listener, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        //    datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            et_dob.setText((Utilities.getMonthName((monthOfYear+1)) +" "+ dayOfMonth+", " + year));

            Integer age = getAge(year,monthOfYear+1,dayOfMonth);

//            Toast.makeText(getApplicationContext(),"a00--"+age,Toast.LENGTH_LONG).show();

            if(age >= 21){
                et_dob.setText((Utilities.getMonthName((monthOfYear+1)) +" "+ dayOfMonth+", " + year));
            }else{
//                et_dob.setText((Utilities.getMonthName((monthOfYear+1)) +" "+ dayOfMonth+", " + year));
//                Toast.makeText(getApplicationContext(),"a00--Error",Toast.LENGTH_LONG).show();
                Utilities.showAlertBox(PlayWithCash.this,getResources().getString(R.string.dob_title),getResources().getString(R.string.dob_msg));
            }
        }
    };

    private Integer getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageInt;
    }

    private JSONObject buildRequest(){

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("email",et_email_address.getText().toString().trim());
            jsonObj.put("password",et_password.getText().toString().trim());
            jsonObj.put("fullName",et_full_name.getText().toString().trim());
            jsonObj.put("dob",et_dob.getText().toString().trim());
            jsonObj.put("phoneNumber",et_ph_no.getText().toString().trim());
            jsonObj.put("countryCode","1");
            jsonObj.put("promoCode",et_promo_code.getText().toString().trim());
            jsonObj.put("userOrigin","none");
            jsonObj.put("platformOrigin",getString(R.string.platform));
            jsonObj.put("signUpOrigin",signUpOrigin);
            jsonObj.put("signUpFor","cash");
            jsonObj.put("referrerCode","");

            if(signUpOrigin!=null&&!signUpOrigin.equals("EMAIL")) {
                JSONObject fbObject = new JSONObject();
                fbObject.put("email", et_email_address.getText().toString().trim());
                fbObject.put("id", userId);
                fbObject.put("token", userToken);
                fbObject.put("displayName", et_full_name.getText().toString().trim());
                jsonObj.put("facebook", fbObject);
            }

            JSONObject metrics_json = new JSONObject();
            metrics_json.put("platform",getString(R.string.platform));
            metrics_json.put("appVersion", BuildConfig.VERSION_NAME);

            jsonObj.put("metrics", metrics_json);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    private void submitSignUpData() {

        Log.e("sign",""+buildRequest());
        AndroidNetworking.post(APIs.SIGN_UP_URL)
                .addJSONObjectBody(buildRequest()) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("***SIGNUP::",response.toString());

                        try {
                            JSONObject signup_res = response.getJSONObject("account");
                            ResponsePojo responsePojo = new ResponsePojo();
                            responsePojo.set_id(signup_res.getString("_id"));
                            responsePojo.setEmail(signup_res.getString("email"));
                            responsePojo.setFullName(signup_res.getString("fullName"));
                            responsePojo.setCity(signup_res.getString("city"));
                            responsePojo.setState(signup_res.getString("state"));
                            responsePojo.setCountry(signup_res.getString("country"));
                            responsePojo.setUserPlayMode(signup_res.getString("userPlayMode"));
                            responsePojo.setTotalCashBalance(signup_res.getString("totalCashBalance"));
                            responsePojo.setAvatar(signup_res.getString("avatar"));
                            responsePojo.setTokenBalance(signup_res.getString("tokenBalance"));
                            responsePojo.setSessionToken(response.getString("sessionToken"));
                            responsePojo.setTotalWins(signup_res.getString("totalWins"));

                            Intent _intent = new Intent(getApplicationContext(),UserLocation.class);
                            _intent.putExtra("sessionToken", response.getString("sessionToken"));
                            _intent.putExtra("userPlayMode", signup_res.getString("userPlayMode"));
                            _intent.putExtra("avatar", signup_res.getString("avatar"));
                            _intent.putExtra("amount_cash", signup_res.getString("totalCashBalance"));
                            _intent.putExtra("amount_token", signup_res.getString("tokenBalance"));
                            _intent.putExtra("wins", signup_res.getString("totalWins"));
                            _intent.putExtra("acc_name", signup_res.getString("fullName"));
                            _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(_intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("js","Login----error-------"+anError);

                        if (anError.getErrorCode() != 0) {
                            Log.d("", "onError errorCode : " + anError.getErrorCode());
                            Log.d("", "onError errorBody : " + anError.getErrorBody());
                            Log.d("", "onError errorDetail : " + anError.getErrorDetail());

                            if(anError.getErrorBody().contains("Email Address already exists")){
                                et_email_address.requestFocus();
                                tvEmailError.setVisibility(View.VISIBLE);
                                email_view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            }
                        } else {
                            Log.d("", "onError errorDetail  0: " + anError.getErrorDetail());
                        }

                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String idToken = account.getIdToken();
                // Signed in successfully, show authenticated UI.
                Log.e("GOOGLE TOKEN::", "::" + idToken);
                userToken =idToken;
                updateUI(account);
            } else {
                updateUI(null);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("PLAY WITH CASH", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount signInAccount) {
        try {
            if (signInAccount != null) {
                userEmail = signInAccount.getEmail();
                String firstName = signInAccount.getGivenName();
                String Last_name = signInAccount.getFamilyName();
                userFullName = firstName + " " + Last_name;
                if (userEmail != null && !userEmail.equals("")) {
                    et_full_name.setText(userFullName);
                    et_full_name.setSelection(userFullName.length());
                    et_email_address.setText(userEmail);
                    et_email_address.setEnabled(false);
                    signUpOrigin="google";
                }

            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void configureFb() {

        mFacebookCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        String fb_email;
                                        String firstName;
                                        String lastName;
                                        try {
                                            userId = object.optString("id");
                                            fb_email = object.optString("email");
                                            firstName = object.optString("first_name");
                                            lastName = object.optString("last_name");

                                            userEmail = fb_email;
                                            userFullName = firstName + " " + lastName;
                                            if (!userEmail.equals("")) {
                                                et_full_name.setText(userFullName);
                                                et_full_name.setSelection(userFullName.length());
                                                et_email_address.setText(userEmail);
                                                et_email_address.setEnabled(false);
                                                signUpOrigin="facebook";
                                            }

                                            Log.d("data::", "Got info::" + userId + "#" + fb_email + "#" + firstName + "#" + lastName);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void configureGoogleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {


            if (AccessToken.getCurrentAccessToken() != null) {

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                String fb_email;
                                String firstName;
                                String lastName;

                                try {
                                    userId = object.optString("id");
                                    fb_email = object.optString("email");
                                    firstName = object.optString("first_name");
                                    lastName = object.optString("last_name");

                                    userEmail = fb_email;
                                    userFullName = firstName + " " + lastName;
                                    if (!userEmail.equals("")) {
                                        et_full_name.setText(userFullName);
                                        et_full_name.setSelection(userFullName.length());
                                        et_email_address.setText(userEmail);
                                        et_email_address.setEnabled(false);
                                        signUpOrigin="facebook";
                                    }

                                    Log.d("data::", "Got info::" + userId + "#" + fb_email + "#" + firstName + "#" + lastName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            } else {
                mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}


