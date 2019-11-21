package com.quizest.quizestapp.ModelPackage;

public class Coin {
    private String coinName;
    private String amount;
    private String date;

    public Coin(String coinName, String amount, String date) {
        this.coinName = coinName;
        this.amount = amount;
        this.date = date;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
