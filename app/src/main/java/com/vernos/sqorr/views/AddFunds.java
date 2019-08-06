package com.vernos.sqorr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.CardDataPojo;
import com.vernos.sqorr.utilities.Utilities;
import me.aflak.libraries.callback.FingerprintDialogCallback;
import me.aflak.libraries.dialog.DialogAnimation;
import me.aflak.libraries.dialog.FingerprintDialog;
import com.vernos.sqorr.utilities.APIs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class AddFunds extends AppCompatActivity implements View.OnClickListener, FingerprintDialogCallback ,PopupMenu.OnMenuItemClickListener {

    private RelativeLayout rl_add_new_card;
    // LinearLayout funds_add;

    private EditText etCustomAmt;
    private View amountView;
    private RecyclerView rvCardsList;
    private String sessionToken;
    private CardsListAdapter recycleAdapter;

    private  List<String > cardDetails=new ArrayList<>();
    private TextView tvAddFunds,tvFaq; 

    private LinearLayout llAddFunds;
    private String selCardId;
    private List<CardDataPojo> cardsResponse = new ArrayList<>();

    private static final int ADD_NEW_CARD = 1111,EDIT_CARD = 1112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_funds);
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

        rl_add_new_card=findViewById(R.id.rl_add_new_card);
        etCustomAmt=findViewById(R.id.etCustomAmt);
        amountView=findViewById(R.id.amountView);
        final LinearLayout llAdd5 = findViewById(R.id.llAdd5);
        LinearLayout llAdd10 = findViewById(R.id.llAdd10);
        LinearLayout llAdd25 = findViewById(R.id.llAdd25);
        LinearLayout llAdd50 = findViewById(R.id.llAdd50);
        tvAddFunds=findViewById(R.id.tvAddFunds);
        llAddFunds=findViewById(R.id.llAddFunds);
        tvFaq=findViewById(R.id.tvFaq);
        rvCardsList=findViewById(R.id.rvCardsList);
        LinearLayoutManager llm = new LinearLayoutManager(AddFunds.this);
        rvCardsList.setLayoutManager(llm);
        rvCardsList.setItemAnimator(null);
        rvCardsList.setNestedScrollingEnabled(false);
        recycleAdapter=new CardsListAdapter(cardsResponse,AddFunds.this);
        rvCardsList.setAdapter(recycleAdapter);


        toolbar_title_x.setText(getString(R.string.add_funds));

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        rl_add_new_card.setOnClickListener(this);
        llAddFunds.setOnClickListener(this);
        llAdd5.setOnClickListener(this);
        llAdd10.setOnClickListener(this);
        llAdd25.setOnClickListener(this);
        llAdd50.setOnClickListener(this);
        tvFaq.setOnClickListener(this);
        etCustomAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                llAddFunds.setBackgroundResource(R.drawable.btn_bg_grey);
                tvAddFunds.setTextColor(getResources().getColor(R.color.gray));
                llAddFunds.setClickable(false);
                if(s.toString().length()>0&&!s.toString().startsWith("$")){
                    etCustomAmt.setText("$");
                    Selection.setSelection(etCustomAmt.getText(), etCustomAmt.getText().length());
                }
                if (etCustomAmt.getText().toString().trim().length() == 0) {
                    amountView.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.sep_view_color,null));
                } else {
                    amountView.setBackgroundColor(getResources().getColor(R.color.text_color_new));
                }
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
         getAllCardsList();

      /*  if(recycleAdapter!=null)
            recycleAdapter.notifyDataSetChanged();*/

    }

    //get user saved cards list
    private void getAllCardsList() {

        AndroidNetworking.get(APIs.CARDS_LIST_URL)
                .setPriority(Priority.HIGH)
                .addHeaders("sessionToken",sessionToken)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        Log.e("getAllCardsList :: ", response.toString());

                        cardsResponse.clear();

                        // do anything with response
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jb = response.getJSONObject(i);

                                CardDataPojo cardData = new CardDataPojo();
                                cardData.set_id(jb.getString("_id"));
                                cardData.setAccount(jb.getString("account"));
                                cardData.setToken(jb.getString("token"));
                                cardData.setAuthorizeNetCustomerProfileId(jb.getString("authorizeNetCustomerProfileId"));
                                cardData.setAuthorizeNetCustomerPaymentProfileId(jb.getString("authorizeNetCustomerPaymentProfileId"));
                                cardData.setLastFourDigits(jb.getString("lastFourDigits"));
                                cardData.setExpiry(jb.getString("expiry"));
                                cardData.setCardType(jb.getString("cardType"));
                                cardData.setCreatedAt(jb.getString("createdAt"));
                                cardsResponse.add(cardData);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (recycleAdapter != null)
                            recycleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Utilities.showToast(AddFunds.this, error.getErrorDetail());
                    }
                });

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title_x:
                finish();
                break;
            case R.id.llAddFunds:
//                showAlertBoxFing(AddFunds.this);
                FingerprintDialog.initialize(AddFunds.this)
                        .title("Finger")
                        .message("touch")
                        .enterAnimation(DialogAnimation.Enter.RIGHT)
                        .exitAnimation(DialogAnimation.Exit.RIGHT)
                        .circleScanningColor(R.color.colorAccent)
                        .callback(AddFunds.this)
                        .show();
                     buildRequestForSubmit();
                break;
            case R.id.rl_add_new_card:
                Intent addNewCardIntent = new Intent(AddFunds.this, AddNewCard.class);
                addNewCardIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(addNewCardIntent,ADD_NEW_CARD);
                break;
            case R.id.llAdd5:
                etCustomAmt.setText("$5");
                etCustomAmt.setSelection(etCustomAmt.getText().toString().length());
                break;
            case R.id.llAdd10:
                etCustomAmt.setText("$10");
                etCustomAmt.setSelection(etCustomAmt.getText().toString().length());
                break;
            case R.id.llAdd25:
                etCustomAmt.setText("$25");
                etCustomAmt.setSelection(etCustomAmt.getText().toString().length());
                break;
            case R.id.llAdd50:
                etCustomAmt.setText("$50");
                etCustomAmt.setSelection(etCustomAmt.getText().toString().length());
                break;
            case R.id.tvFaq:
                break;
            default:
                break;
        }
    }

    @Override

    public void onAuthenticationSucceeded() {
    }

    @Override
    public void onAuthenticationCancel() {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.edit_card:
                Intent editCardIntent = new Intent(AddFunds.this ,EditCreditCard.class);
                editCardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                editCardIntent.putExtra("CARD_ID",selCardId);
                startActivityForResult(editCardIntent,EDIT_CARD);
                return true;
            case R.id.delete_card:
                try {
                    // do your code
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cardId", selCardId);
                    DeleteCreditCard(jsonObject);
                    Toast.makeText(this, "Selected Item1: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;

            default:
                return false;
        }

    }


    public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.ViewHolder> {


        private final List<CardDataPojo> mValues;
        //    private final PromosFragment.OnListFragmentInteractionListener mListener;
        private Context context;

        public CardsListAdapter(List<CardDataPojo> items, Context context) {
            mValues = items;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.added_card_view, parent, false);

            return new ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            String cardType = cardsResponse.get(position).getCardType();

            if(cardType.equalsIgnoreCase("master")){
                holder.ivCard.setImageResource(R.drawable.ic_cc_mastercard);
                holder.tvCardName.setText("Mastercard* "+cardsResponse.get(position).getLastFourDigits());
            }else if(cardType.equalsIgnoreCase("visa")){
                holder.ivCard.setImageResource(R.drawable.ic_cc_visa);
                holder.tvCardName.setText("Visa* "+cardsResponse.get(position).getLastFourDigits());
            }else if(cardType.equalsIgnoreCase("JCB")){
                holder.ivCard.setImageResource(R.drawable.ic_cc_generic);
                holder.tvCardName.setText("JCB* "+cardsResponse.get(position).getLastFourDigits());
            }else{
                holder.ivCard.setImageResource(R.drawable.ic_cc_generic);
            }

            String expDate=cardsResponse.get(position).getExpiry();
            if(expDate!=null&&!expDate.equalsIgnoreCase("")){
                holder.tvExpiry.setVisibility(View.VISIBLE);
                holder.tvExpiry.setText("exp "+expDate);
            }else{
                holder.tvExpiry.setVisibility(View.GONE);
            }

            holder.ivOverFlow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selCardId=cardsResponse.get(position).get_id();
                    Context wrapper = new ContextThemeWrapper(AddFunds.this, R.style.popupMenuStyle1);
                    PopupMenu popup = new PopupMenu(wrapper, view);
                    popup.setOnMenuItemClickListener(AddFunds.this);
                    popup.inflate(R.menu.cc_card_menu);
                    popup.show();
                }
            });

            holder.llCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selCardId=cardsResponse.get(position).get_id();
                    if(etCustomAmt.getText().toString().trim().length()>0){
                        llAddFunds.setClickable(true);
                        tvAddFunds.setTextColor(getResources().getColor(R.color.white));
                        llAddFunds.setBackground(getResources().getDrawable(R.drawable.btn_bg_green));
                    }else{
                        llAddFunds.setClickable(true);
                        llAddFunds.setBackground(getResources().getDrawable(R.drawable.btn_gray_bg));
                        tvAddFunds.setTextColor(getResources().getColor(R.color.add_fund_clr));
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView ivCard,ivOverFlow;
            TextView tvCardName,tvExpiry;
            LinearLayout llCard;

            public ViewHolder(View view) {
                super(view);
                llCard=view.findViewById(R.id.llCard);
                ivCard =view.findViewById(R.id.ivCard);
                tvCardName =view.findViewById(R.id.tvCardName);
                tvExpiry=view.findViewById(R.id.tvExpiry);
                ivOverFlow=view.findViewById(R.id.ivOverFlow);

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if(resultCode==RESULT_OK){
                if(requestCode==ADD_NEW_CARD||requestCode==EDIT_CARD) {

                    Log.e("HEy called Back", "FRom Add CARD");
                    getAllCardsList();
                }
            }

        } catch (Exception ex) {
            Toast.makeText(AddFunds.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    private void buildRequestForSubmit(){
        try {
            JSONObject jsonObject = new JSONObject();
            String amount= etCustomAmt.getText().toString().trim().replace("$", "");
            jsonObject.put("amount",amount);
            jsonObject.put("cardId",selCardId);
            SubmitFunds(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//Add Funds Submit
    private void SubmitFunds(JSONObject jsonObject) {
        Log.e("ADDFUNDSREQUEST::",jsonObject.toString());
        AndroidNetworking.post(APIs.ADD_FUNDS_URL)
                .setPriority(Priority.HIGH)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders("Content-Type", "application/json")
                .addHeaders("sessionToken",sessionToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("ADDFUNDS::RESPONSE::",response.toString());

                                String userPlayMode = response.getString("userPlayMode");
                                String totalCashBalance = response.getString("totalCashBalance");
                                String cashBalance = response.getString("cashBalance");
                                String promoBalance = response.getString("promoBalance");
                                String tokenBalance = response.getString("tokenBalance");


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR####", "ERROR-------" + anError);

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

    private void DeleteCreditCard(JSONObject jsonObject) {
        Log.e("DeleteCard::REQUEST::",jsonObject.toString());
        AndroidNetworking.delete(APIs.DELETE_CARD_URL)
                .setPriority(Priority.HIGH)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders("Content-Type", "application/json")
                .addHeaders("sessionToken",sessionToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for(int i=0;i<cardsResponse.size();i++){
                                if(cardsResponse.get(i).get_id().equalsIgnoreCase(selCardId)){
                                    if(cardsResponse.size()>i) {
                                        cardsResponse.remove(i);
                                        break;
                                    }
                                }
                            }

                            if(recycleAdapter!=null){
                                recycleAdapter.notifyDataSetChanged();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if(recycleAdapter!=null){
                            recycleAdapter.notifyDataSetChanged();
                        }
                        /*Till here */
                        Log.e("ERROR####", "ERROR-------" + anError.getErrorBody());

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
