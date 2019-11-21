package com.quizest.quizestapp.ModelPackage;


import com.google.gson.annotations.SerializedName;


public class CoinInfo{

	@SerializedName("amount")
	private String amount;

	@SerializedName("coin_id")
	private int coinId;

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("available_amount")
	private int availableAmount;

	public String getAmount(){
		return amount;
	}

	public int getCoinId(){
		return coinId;
	}

	public String getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public int getAvailableAmount(){
		return availableAmount;
	}
}