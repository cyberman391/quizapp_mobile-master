package com.quizest.quizestapp.ModelPackage;


import com.google.gson.annotations.SerializedName;

public class SubminAnswer{

	@SerializedName("total_coin")
	private int totalCoin;

	@SerializedName("right_answer")
	private RightAnswer rightAnswer;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("total_point")
	private String totalPoint;

	public int getTotalCoin(){
		return totalCoin;
	}

	public RightAnswer getRightAnswer(){
		return rightAnswer;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public String getTotalPoint(){
		return totalPoint;
	}

	public class RightAnswer{

		@SerializedName("option_title")
		private String optionTitle;

		@SerializedName("option_id")
		private int optionId;

		public String getOptionTitle(){
			return optionTitle;
		}

		public int getOptionId(){
			return optionId;
		}
	}
}