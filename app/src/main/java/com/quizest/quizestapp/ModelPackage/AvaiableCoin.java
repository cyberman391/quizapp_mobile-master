package com.quizest.quizestapp.ModelPackage;


import com.google.gson.annotations.SerializedName;


public class AvaiableCoin {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("coin")
    private CoinInfo coin;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public CoinInfo getCoin() {
        return coin;
    }
}