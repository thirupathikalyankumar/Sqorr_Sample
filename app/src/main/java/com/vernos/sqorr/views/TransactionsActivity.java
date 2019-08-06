package com.vernos.sqorr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vernos.sqorr.R;
import com.vernos.sqorr.model.Transactions;
import com.vernos.sqorr.ui.AppConstants;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.Utilities;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_add_funds,rlBalance;
    private RecyclerView rvTransactions;
    private TextView tvBalanceHeader,tvBalAmount,tvWithdrawFunds;
    private ImageView ivTokens;
    private ArrayList<Transactions> transactionsList=new ArrayList<>();
    private String sessionToken;
    private TransactionsAdapter transactionsAdapter;
    private String userRole;
    private ProgressBar trans_progressBar;
    private LinearLayout llFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasactions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_TOKEN", MODE_PRIVATE);
        sessionToken= sharedPreferences.getString("token", "");

        userRole = Dashboard.ROLE;

        init();

        getTransactionsList();
    }

    @SuppressLint("SetTextI18n")
    private void setPageData(){
        if(userRole!=null){
            if(userRole.equalsIgnoreCase("cash")){
                tvBalanceHeader.setText("Cash balance");
                tvBalAmount.setText("$"+Dashboard.AMOUNT_CASH);
                ivTokens.setVisibility(View.GONE);
                rl_add_funds.setVisibility(View.GONE);
                tvWithdrawFunds.setVisibility(View.VISIBLE);
                llFaq.setVisibility(View.VISIBLE);
            }else if(userRole.equalsIgnoreCase("tokens")){
                tvBalanceHeader.setText("Token balance");
                tvBalAmount.setText(Dashboard.AMOUNT_TOKEN);
                ivTokens.setVisibility(View.VISIBLE);
                rl_add_funds.setVisibility(View.VISIBLE);
                tvWithdrawFunds.setVisibility(View.GONE);
                llFaq.setVisibility(View.GONE);
            }else{
                rlBalance.setVisibility(View.GONE);
            }
        }else{
            rlBalance.setVisibility(View.GONE);
        }
    }

    private void init() {
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        rl_add_funds=findViewById(R.id.rl_add_funds);
        TextView btn_add_funds=findViewById(R.id.btn_add_funds);
        rvTransactions=findViewById(R.id.rvTransactions);
        tvBalanceHeader=findViewById(R.id.tvBalanceHeader);
        ivTokens=findViewById(R.id.ivTokens);
        tvBalAmount=findViewById(R.id.tvBalAmount);
        rlBalance=findViewById(R.id.rlBalance);
        trans_progressBar=findViewById(R.id.trans_progressBar);
        tvWithdrawFunds=findViewById(R.id.tvWithdrawFunds);
       TextView  tvFaq=findViewById(R.id.tvFaq);
        llFaq=findViewById(R.id.llFaq);

        trans_progressBar.setVisibility(View.VISIBLE);
        rvTransactions.setVisibility(View.GONE);

        setPageData();

        LinearLayoutManager llm = new LinearLayoutManager(TransactionsActivity.this);
        rvTransactions.setLayoutManager(llm);
        transactionsAdapter=new TransactionsAdapter(transactionsList, TransactionsActivity.this);
        rvTransactions.setAdapter(transactionsAdapter);

        toolbar_title_x.setText(getString(R.string._transactions));

        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);
        btn_add_funds.setOnClickListener(this);
        tvWithdrawFunds.setOnClickListener(this);
        tvFaq.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(transactionsAdapter!=null){
            transactionsAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title_x:
                finish();
                break;
            case R.id.btn_add_funds:
                Intent addFundsIntent = new Intent(TransactionsActivity.this,AddFunds.class);
                addFundsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(addFundsIntent,1212);
                break;
            case R.id.tvFaq:
                Intent webIntent = new Intent(TransactionsActivity.this,WebScreens.class);
                webIntent.putExtra("title", AppConstants.FAQS);
                startActivity(webIntent);
                break;
            case R.id.tvWithdrawFunds:
                Intent withdrawIntent = new Intent(TransactionsActivity.this,WithdrawFunds.class);
                withdrawIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(withdrawIntent,1212);

                break;
            default:
                break;
        }
    }

    //get user transactions list
    private void getTransactionsList() {

        AndroidNetworking.get(APIs.TRANSACTIONS_URL)
                .setPriority(Priority.HIGH)
                .addHeaders("sessionToken",sessionToken)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        trans_progressBar.setVisibility(View.GONE);

                        Log.e("getTransactionsList :: ", response.toString());

                        transactionsList.clear();
                        Type listType = new TypeToken<List<Transactions>>() {}.getType();
                        transactionsList = new Gson().fromJson(response.toString(), listType);

                        if(transactionsList.size()>0){
                            rvTransactions.setVisibility(View.VISIBLE);
                        }else{
                            rvTransactions.setVisibility(View.GONE);
                            Utilities.showToast(TransactionsActivity.this, "No Transactions ");
                        }

                        if (transactionsAdapter != null)
                            transactionsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        trans_progressBar.setVisibility(View.GONE);
                        Utilities.showToast(TransactionsActivity.this, error.getErrorDetail());
                    }
                });

    }


    public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

        ArrayList<Transactions> transactionList;
        private Context context;

        public TransactionsAdapter(ArrayList<Transactions> items, Context context) {
            this.transactionList= items;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.transactions_cell, parent, false);

            return new ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final int pos=holder.getAdapterPosition();

            String transType=transactionsList.get(pos).getType();

            holder.tvTransactionType.setText(transType);

            holder.tvDescription.setText(transactionsList.get(pos).getDescription());

            if(userRole!=null&&userRole.equalsIgnoreCase("cash")) {
                holder.llToken.setVisibility(View.GONE);
                holder.tvTokenAmount.setVisibility(View.GONE);
                holder.tvAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setText("$"+transactionsList.get(pos).getAmount());
                holder.tvBalAmount.setText("$"+transactionsList.get(pos).getBalance());
            }else{
                holder.llToken.setVisibility(View.VISIBLE);
                holder.tvTokenAmount.setVisibility(View.VISIBLE);
                holder.tvAmount.setVisibility(View.GONE);
                holder.tvTokenAmount.setText(transactionsList.get(pos).getAmount());
                holder.tvBalAmount.setText(transactionsList.get(pos).getBalance());
            }

            if(transType!=null&&transType.equalsIgnoreCase("withdraw")){
                holder.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_withdraw,null));
                holder.rlIcon.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.bg_withdraw,null));
                holder.tvAddOrRemove.setText("-");
                holder.tvAddOrRemoveToken.setText("-");
            }else if(transType!=null&&transType.equalsIgnoreCase("deposit")){
                holder.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_deposit,null));
                holder.rlIcon.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.bg_deposit,null));
                holder.tvAddOrRemove.setText("+");
                holder.tvAddOrRemoveToken.setText("+");
            } else if(transType!=null&&transType.equalsIgnoreCase("winnings")){
                holder.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_trophy,null));
                holder.rlIcon.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.bg_deposit,null));
                holder.tvAddOrRemove.setText("+");
                holder.tvAddOrRemoveToken.setText("+");
            } else if(transType!=null&&transType.equalsIgnoreCase("wager")){
                holder.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_wager,null));
                holder.rlIcon.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.bg_withdraw,null));
                holder.tvAddOrRemove.setText("-");
                holder.tvAddOrRemoveToken.setText("-");
            }


            //Set Date
             try {
                 holder.tvDate.setVisibility(View.GONE);
                 String dateFromServer= transactionsList.get(pos).getDate();
                 if(dateFromServer!=null&&!dateFromServer.equals("")) {
                     String[] dateTime = dateFromServer.split("T");
                     if (dateTime.length > 0) {
                         String[] dateArray = dateTime[0].split("-");
                         if(dateArray.length==3) {
                             holder.tvDate.setText(dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0].substring(2,4));
                             holder.tvDate.setVisibility(View.VISIBLE);
                         }
                     }
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }
        }

        @Override
        public int getItemCount() {
            return transactionsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTransactionType,tvDescription,tvAddOrRemove,tvDate,tvBalAmount,tvAmount,
                    tvTokenAmount,tvAddOrRemoveToken;
            private ImageView ivIcon;
            private RelativeLayout rlIcon;
            private LinearLayout llToken;

            public ViewHolder(View view) {
                super(view);
                this.tvTransactionType = itemView.findViewById(R.id.tvTransactionType);
                this.tvDescription=itemView.findViewById(R.id.tvDescription);
                this.tvAddOrRemove=itemView.findViewById(R.id.tvAddOrRemove);
                this.tvDate=itemView.findViewById(R.id.tvDate);
                this.tvBalAmount=itemView.findViewById(R.id.tvBalAmount);
                this.tvAmount=itemView.findViewById(R.id.tvAmount);
                this.ivIcon=itemView.findViewById(R.id.ivIcon);
                this.rlIcon=itemView.findViewById(R.id.rlIcon);
                this.tvDate=itemView.findViewById(R.id.tvDate);
                this.tvTokenAmount=itemView.findViewById(R.id.tvTokenAmount);
                this.llToken=itemView.findViewById(R.id.llToken);
                this.tvAddOrRemoveToken=itemView.findViewById(R.id.tvAddOrRemoveToken);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if(resultCode==RESULT_OK){
                if(requestCode==1212) {
                    Log.e("HEy called Back", "FRom Add FUNDS");
                }
            }

        } catch (Exception ex) {
            Toast.makeText(TransactionsActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
