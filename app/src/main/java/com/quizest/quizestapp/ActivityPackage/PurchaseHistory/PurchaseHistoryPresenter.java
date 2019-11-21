package com.quizest.quizestapp.ActivityPackage.PurchaseHistory;

import android.app.ProgressDialog;
import android.content.Context;

import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.Coin;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHistoryPresenter {
    PurchaseHistoryView purchaseHistoryView;

    public PurchaseHistoryPresenter(PurchaseHistoryView purchaseHistoryView) {
        this.purchaseHistoryView = purchaseHistoryView;
    }

    public void getPurchaseHistory(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        Storage storage = new Storage(context);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> stringCall = retrofitInterface.getPurchaseHistory(storage.getAccessToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean("success")) {
                            JSONArray buyHistoryArray = jsonObject.getJSONArray("buy_history");
                            List<Coin> coinList = new ArrayList<>();
                            coinList.add(new Coin("","",""));
                            for (int i = 0; i < buyHistoryArray.length(); i++) {
                                coinList.add(new Coin(
                                        buyHistoryArray.getJSONObject(i)
                                                .getString("coin_name"),
                                        buyHistoryArray.getJSONObject(i).getString("amount"),
                                        buyHistoryArray.getJSONObject(i).getString("date")
                                ));
                            }
                            purchaseHistoryView.onSuccess(coinList, progressDialog);
                        } else {
                            purchaseHistoryView.onError(jsonObject.getString("message"), progressDialog);
                        }
                    } catch (JSONException e) {
                        purchaseHistoryView.onError(context.getString(R.string.someting_wrong), progressDialog);
                        e.printStackTrace();
                    }
                } else {
                    purchaseHistoryView.onError(context.getString(R.string.someting_wrong), progressDialog);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    purchaseHistoryView.onError(context.getString(R.string.connection_timeout), progressDialog);
                } else {
                    purchaseHistoryView.onError(context.getString(R.string.someting_wrong), progressDialog);
                }
            }
        });
    }
}
