package com.quizest.quizestapp.ActivityPackage.BuyCoin;

import android.app.ProgressDialog;

import com.quizest.quizestapp.ModelPackage.AvaiableCoin;
import com.quizest.quizestapp.ModelPackage.PaymentMethodOption;

import java.util.List;

public interface BuyCoinView {
    void onSuccess(List<PaymentMethodOption> paymentMethodOptionList);

    void onError(String message);

    void onCoinInfoGotSuccess(AvaiableCoin avaiableCoin);

    void onCoinInfoGotError(String message);

    void buyCoinSuccess(String message, ProgressDialog progressDialog);

    void buyCoinError(String message, ProgressDialog progressDialog);

    void onClientSettingError(String message);

    void onClientSettingSuccess(String client_token);
}
