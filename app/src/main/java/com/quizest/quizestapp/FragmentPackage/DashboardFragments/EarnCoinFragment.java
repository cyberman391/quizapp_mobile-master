package com.quizest.quizestapp.FragmentPackage.DashboardFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.ActivityPackage.BuyCoin.BuyCoinActivity;
import com.quizest.quizestapp.ActivityPackage.PurchaseHistory.PurchaseHistory;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.ProfileSection;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EarnCoinFragment extends Fragment {

    AppCompatButton btnBuyCoin, btnPurchaseHistory;
    TextView tv_coin;

    public EarnCoinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earn_coin, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getProfileData();

        btnBuyCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        BuyCoinActivity.class);
                startActivity(intent);
            }
        });

        btnPurchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PurchaseHistory.class);
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        View view = getView();
        if (view != null) {
            btnBuyCoin = view.findViewById(R.id.btn_buy_coin);
            btnPurchaseHistory = view.findViewById(R.id.btn_purchase_history);
            tv_coin = view.findViewById(R.id.tv_coin);
        }
    }

    @Override
    public void onResume() {
        getProfileData();
        super.onResume();
    }

    private void getProfileData() {
        final Storage storage = new Storage(getActivity());
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        final Call<String> profileCall = retrofitInterface.getProfileData(storage.getAccessToken());
        profileCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), getActivity(), null);
                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            ProfileSection profileSection = gson.fromJson(response.body(), ProfileSection.class);
                            tv_coin.setText(String.valueOf(profileSection.getData().getUser().getCoin()));

                        } else {

                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (getActivity() != null)

                        Toast.makeText(getActivity(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    if (getActivity() != null && isAdded())
                        Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
