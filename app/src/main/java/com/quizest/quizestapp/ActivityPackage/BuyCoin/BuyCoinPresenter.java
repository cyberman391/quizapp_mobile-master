package com.quizest.quizestapp.ActivityPackage.BuyCoin;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.AvaiableCoin;
import com.quizest.quizestapp.ModelPackage.PaymentMethodOption;
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

public class BuyCoinPresenter {

    BuyCoinView buyCoinView;

    public BuyCoinPresenter(BuyCoinView buyCoinView) {
        this.buyCoinView = buyCoinView;
    }

    public void getPaymentMethodInfo(final Context context) {
        Storage storage = new Storage(context);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> stringCall = retrofitInterface.getPaymentMetodList(storage.getAccessToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean("success")) {
                            JSONArray dataArray = jsonObject.getJSONArray("payment_methods");
                            List<PaymentMethodOption> paymentMethodOptionList = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                paymentMethodOptionList.add(new PaymentMethodOption(
                                        dataArray.getJSONObject(i)
                                                .getString("name"),
                                        dataArray.getJSONObject(i)
                                                .getInt("id")
                                ));
                            }
                            buyCoinView.onSuccess(paymentMethodOptionList);
                        } else {
                            Toast.makeText(context, jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        buyCoinView.onError(context.getString(R.string.someting_wrong));
                        e.printStackTrace();
                    }
                } else {
                    buyCoinView.onError(context.getString(R.string.someting_wrong));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    buyCoinView.onError(context.getString(R.string.connection_timeout));
                } else {
                    buyCoinView.onError(context.getString(R.string.someting_wrong));
                }
            }
        });
    }

    public void getAvaibaleCoinInfo(final Context context) {
        Storage storage = new Storage(context);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> stringCall = retrofitInterface.getAvaiablePayment(storage.getAccessToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Gson gson = new Gson();
                        AvaiableCoin avaiableCoin = gson.fromJson(
                                response.body(), AvaiableCoin.class
                        );
                        buyCoinView.onCoinInfoGotSuccess(
                                avaiableCoin
                        );
                    } else {
                        buyCoinView.onCoinInfoGotError(context.getString(R.string.someting_wrong));
                    }
                } else {
                    buyCoinView.onCoinInfoGotError(context.getString(R.string.someting_wrong));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    buyCoinView.onCoinInfoGotError(context.getString(R.string.connection_timeout));
                } else {
                    buyCoinView.onCoinInfoGotError(context.getString(R.string.someting_wrong));
                }
            }
        });
    }

    public void buyCoin(final Context context, Integer coinId, Integer paymentId, Integer amount, String paymentNonce) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Storage storage = new Storage(context);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> stringCall = retrofitInterface.buyCoin(
                storage.getAccessToken(),
                coinId,
                paymentId,
                amount, paymentNonce
        );
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getBoolean("success")) {
                                buyCoinView.buyCoinSuccess(jsonObject.getString("message"), progressDialog);
                            } else {
                                buyCoinView.buyCoinError(jsonObject.getString("message"), progressDialog);
                            }
                        } catch (JSONException e) {
                            buyCoinView.buyCoinError(context.getString(R.string.someting_wrong), progressDialog);
                            e.printStackTrace();
                        }
                    }
                } else {
                    buyCoinView.buyCoinError(context.getString(R.string.someting_wrong), progressDialog);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    buyCoinView.buyCoinError(context.getString(R.string.connection_timeout), progressDialog);
                } else {
                    buyCoinView.buyCoinSuccess(context.getString(R.string.someting_wrong), progressDialog);
                }
            }
        });
    }

    public void getClientSetting(final Context context) {
        Storage storage = new Storage(context);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> stringCall = retrofitInterface.getClientSetting(storage.getAccessToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getBoolean("success")) {
                                buyCoinView.onClientSettingSuccess(jsonObject.getString("braintree_client_token"));
                            } else {
                                buyCoinView.onClientSettingError(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            buyCoinView.onClientSettingError(context.getString(R.string.someting_wrong));
                        }

                    } else {
                        buyCoinView.onClientSettingError(context.getString(R.string.someting_wrong));
                    }
                } else {
                    buyCoinView.onClientSettingError(context.getString(R.string.someting_wrong));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    buyCoinView.onClientSettingError(context.getString(R.string.connection_timeout));
                } else {
                    buyCoinView.onClientSettingError(context.getString(R.string.someting_wrong));
                }
            }
        });
    }
}
