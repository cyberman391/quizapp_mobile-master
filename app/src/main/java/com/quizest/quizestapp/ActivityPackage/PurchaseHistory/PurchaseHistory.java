package com.quizest.quizestapp.ActivityPackage.PurchaseHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.quizest.quizestapp.ActivityPackage.SettingActivity;
import com.quizest.quizestapp.ModelPackage.Coin;
import com.quizest.quizestapp.R;

import java.util.List;

public class PurchaseHistory extends AppCompatActivity implements PurchaseHistoryView {
    RecyclerView purchaseHistoryRecyclerView;
    PurchaseHistoryPresenter purchaseHistoryPresenter;
    ImageButton btnBack, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        initViews();
        initializeRecyclerView();
        purchaseHistoryPresenter = new PurchaseHistoryPresenter(this);
        purchaseHistoryPresenter.getPurchaseHistory(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        PurchaseHistory.this, SettingActivity.class
                ));
            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnSetting = findViewById(R.id.btn_setting);
        purchaseHistoryRecyclerView = findViewById(R.id.recycler_view_purchase_history);
    }

    private void initializeRecyclerView() {
        purchaseHistoryRecyclerView.setHasFixedSize(true);
        purchaseHistoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false
                )
        );
    }

    @Override
    public void onSuccess(List<Coin> coinList, ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        PurchaseHistoryAdapter purchaseHistoryAdapter = new PurchaseHistoryAdapter(
                coinList, this
        );
        purchaseHistoryRecyclerView.setAdapter(purchaseHistoryAdapter);
    }

    @Override
    public void onError(String message, ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
