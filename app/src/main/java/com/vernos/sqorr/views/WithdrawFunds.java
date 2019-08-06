package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vernos.sqorr.R;

public class WithdrawFunds extends AppCompatActivity implements View.OnClickListener {

    private String sessionToken;
    private TextView tvCashBalance,tvWithdrawBalance;
    private View amountView;
    private EditText etAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_funds);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.second_status_bar_color,null));

        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_TOKEN", MODE_PRIVATE);
        sessionToken= sharedPreferences.getString("token", "");
        initComponents();

    }
    @SuppressLint("SetTextI18n")
    private void initComponents() {
        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        tvCashBalance=findViewById(R.id.tvCashBalance);
        tvWithdrawBalance=findViewById(R.id.tvWithdrawBalance);
        etAmount=findViewById(R.id.etAmount);
        amountView=findViewById(R.id.amountView);

        toolbar_title_x.setText("Withdraw funds");
        tvCashBalance.setText("$"+Dashboard.AMOUNT_CASH);
        tvWithdrawBalance.setText("$"+Dashboard.AMOUNT_CASH);
        //Add listener(s)
        toolbar_title_x.setOnClickListener(this);

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0&&!s.toString().startsWith("$")){
                    etAmount.setText("$");
                    Selection.setSelection(etAmount.getText(), etAmount.getText().length());
                }
                if (etAmount.getText().toString().trim().length() == 0) {
                    amountView.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.sep_view_color,null));
                } else {
                    amountView.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.text_color_new,null));
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
            default:
                break;
        }
    }
}
