package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.ResponsePojo;
import com.vernos.sqorr.utilities.APIs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddNewCard extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etCardNumber,etExpiry,etCVV,etFullName,etStreet,etCity,etZipCode;
    private TextView tvAddCard;
    private Spinner spnrState;
    private ImageView ivCardType;
    private LinearLayout llAddNewCard;
    private String sessionToken,cardType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }
        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_TOKEN", MODE_PRIVATE);
        sessionToken= sharedPreferences.getString("token", "");
        init();
    }

    private void init() {
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        etCardNumber=findViewById(R.id.etCardNumber);
        etExpiry=findViewById(R.id.etExpiry);
        etCVV=findViewById(R.id.etCVV);
        etFullName=findViewById(R.id.etFullName);
        etStreet=findViewById(R.id.etStreet);
        etCity=findViewById(R.id.etCity);
        spnrState=findViewById(R.id.spnrState);
        etZipCode=findViewById(R.id.etZipCode);
        ivCardType = findViewById(R.id.ivCardType);

        tvAddCard=findViewById(R.id.tvAddCard);
        llAddNewCard=findViewById(R.id.llAddNewCard);

        toolbar_title_x.setText(getString(R.string.add_new_card));

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        etCardNumber.addTextChangedListener(this);
        //tvAddCard.setOnClickListener(this);
        llAddNewCard.setOnClickListener(this);
        etCVV.addTextChangedListener(this);
        etFullName.addTextChangedListener(this);
        etStreet.addTextChangedListener(this);
        etCity.addTextChangedListener(this);
        etZipCode.addTextChangedListener(this);

        String [] statesArray=getResources().getStringArray(R.array.states_array);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, statesArray);
        spnrState.setAdapter(courseAdapter);

        etExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String current = s.toString();
                if (current.length() == 2 && start == 1) {
                    etExpiry.setText(current + "/");
                    etExpiry.setSelection(current.length() + 1);
                } else if (current.length() == 2 && before == 1) {
                    current = current.substring(0, 1);
                    etExpiry.setText(current);
                    etExpiry.setSelection(current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


        ArrayList<String> listOfPattern=new ArrayList<String>();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);



        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // etCreditCardNumber.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);

                String initial = s.toString();
                // remove all non-digits characters
                String processed = initial.replaceAll("\\D", "");

                // insert a space after all groups of 4 digits that are followed by another digit
                processed = processed.replaceAll("(\\d{4})(?=\\d)(?=\\d)(?=\\d)", "$1 ");

                //Remove the listener
                etCardNumber.removeTextChangedListener(this);

                int index = etCardNumber.getSelectionEnd();

                if (index == 5 || index == 10 || index == 15)
                    if (count > before)
                        index++;
                    else
                        index--;

                //Assign processed text
                etCardNumber.setText(processed);

                try {
                    etCardNumber.setSelection(index);
                } catch (Exception e) {
                    e.printStackTrace();
                    etCardNumber.setSelection(s.length() - 1);
                }
                //Give back the listener
                etCardNumber.addTextChangedListener(this);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String cardNumber=etCardNumber.getText().toString();
                if (cardNumber.startsWith("4")) //VISA
                {
                    ivCardType.setImageResource(R.drawable.ic_cc_visa);
                    cardType="visa";
                }
                else if (cardNumber.startsWith("5")) //Mastercard
                {
                    ivCardType.setImageResource(R.drawable.ic_cc_mastercard);
                    cardType="mastercard";
                }
                else if (cardNumber.startsWith("6")) //Discover
                {
                    ivCardType.setImageResource(R.drawable.ic_cc_generic);
                    cardType="";
                }
                else if (cardNumber.startsWith("37")) //American Express
                {
                    ivCardType.setImageResource(R.drawable.ic_cc_generic);
                    cardType="";
                }
                else if (cardNumber.startsWith("2")) //American Express
                {
                    ivCardType.setImageResource(R.drawable.ic_cc_generic);
                    cardType="JCB";
                } else //Default
                {
                    ivCardType.setImageResource(R.drawable.ic_cc_generic);
                    cardType="";
                }

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title_x:
                finish();
                break;
            case R.id.llAddNewCard:
                buildRequest();
                break;
            default:
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int card_length= etCardNumber.getText().toString().trim().length();
        int expLength =etExpiry.getText().toString().trim().length();
        int cvvLength = etCVV.getText().toString().trim().length();
        int nameLength=etFullName.getText().toString().trim().length();
        int streetLength=etStreet.getText().toString().trim().length();
        int cityLength=etCity.getText().toString().trim().length();
        int zipLength=etZipCode.getText().toString().trim().length();

        if(!cardType.equals("")&&card_length>14&&expLength>4&&cvvLength==3
                &&nameLength>0&&streetLength>0&&cityLength>0&&zipLength>4){
            llAddNewCard.setEnabled(true);
            llAddNewCard.setBackgroundResource(R.drawable.btn_bg_green);
            tvAddCard.setTextColor(getResources().getColor(R.color.white));
        }else{
            llAddNewCard.setEnabled(false);
            llAddNewCard.setBackgroundResource(R.drawable.btn_bg_grey);
            tvAddCard.setTextColor(getResources().getColor(R.color.add_fund_clr));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void buildRequest(){
        try{
            JSONObject jsonObject = new JSONObject();
            String [] expiry = etExpiry.getText().toString().trim().split("/");

            jsonObject.put("cardType",cardType);
            jsonObject.put("cardNumber",etCardNumber.getText().toString().trim().replaceAll("\\s+",""));
            jsonObject.put("securityCde",etCVV.getText().toString().trim());
            jsonObject.put("expMonth",expiry[0]);
            jsonObject.put( "expYear",expiry[1]);
            jsonObject.put(   "fullName",etFullName.getText().toString().trim());
            jsonObject.put( "billingAddress",etStreet.getText().toString().trim());
            jsonObject.put("city",etCity.getText().toString().trim());
            jsonObject.put("state",spnrState.getSelectedItem().toString());
            jsonObject.put("country","USA");
            jsonObject.put("zipCode",etZipCode.getText().toString().trim());

            SendDataToAddCard(jsonObject);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SendDataToAddCard(JSONObject addCardObject){
        Log.e("ADDCARD::REQESTOBJ",addCardObject.toString());
        AndroidNetworking.post(APIs.ADD_CARD_URL)
                .addJSONObjectBody(addCardObject) // posting json
                .addHeaders("Content-Type", "application/json")
                .addHeaders("sessionToken",sessionToken)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Intent intent= new Intent();
                            //intent.putExtra("result",result);
                            setResult(RESULT_OK, intent);
                            finish();
                            Log.e("***ADDCARD::REsponse: :",response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Intent intent= new Intent();
                        //intent.putExtra("result",result);
                        setResult(RESULT_OK, intent);
                        finish();
                        Log.e("ERROR::","-------"+anError);

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
}
