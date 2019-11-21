package com.quizest.quizestapp.ActivityPackage.PurchaseHistory;

import android.app.ProgressDialog;

import com.quizest.quizestapp.ModelPackage.Coin;

import java.util.List;

interface PurchaseHistoryView {
    void onSuccess(List<Coin> coinList, ProgressDialog progressDialog);

    void onError(String message, ProgressDialog progressDialog);
}
