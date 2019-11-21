package com.quizest.quizestapp.ModelPackage;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProfileSection{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("daily_score")
	private List<DailyScoreItem> dailyScore;

	@SerializedName("message")
	private String message;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public List<DailyScoreItem> getDailyScore(){
		return dailyScore;
	}

	public String getMessage(){
		return message;
	}


	public class User{

		@SerializedName("zip")
		private Object zip;

		@SerializedName("participated_questions")
		private int participated_question;

		@SerializedName("country")
		private String country;

		@SerializedName("role")
		private int role;

		@SerializedName("coins")
		private int coin;

		public int getCoin() {
			return coin;
		}

		@SerializedName("email_verified")
		private String emailVerified;

		@SerializedName("address")
		private Object address;

		@SerializedName("city")
		private Object city;

		public int getParticipated_question() {
			return participated_question;
		}

		@SerializedName("photo")
		private String photo;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("language")
		private String language;

		@SerializedName("points")
		private String points;

		@SerializedName("reset_code")
		private String resetCode;

		@SerializedName("active_status")
		private int activeStatus;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("phone")
		private String phone;

		@SerializedName("name")
		private String name;

		@SerializedName("ranking")
		private int ranking;

		@SerializedName("id")
		private int id;

		@SerializedName("state")
		private Object state;

		@SerializedName("email")
		private String email;

		public Object getZip(){
			return zip;
		}

		public String getCountry(){
			return country;
		}

		public int getRole(){
			return role;
		}

		public String getEmailVerified(){
			return emailVerified;
		}

		public Object getAddress(){
			return address;
		}

		public Object getCity(){
			return city;
		}

		public String getPhoto(){
			return photo;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public String getLanguage(){
			return language;
		}

		public String getPoints(){
			return points;
		}

		public String getResetCode(){
			return resetCode;
		}

		public int getActiveStatus(){
			return activeStatus;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public String getPhone(){
			return phone;
		}

		public String getName(){
			return name;
		}

		public int getRanking(){
			return ranking;
		}

		public int getId(){
			return id;
		}

		public Object getState(){
			return state;
		}

		public String getEmail(){
			return email;
		}
	}

	public class DailyScoreItem{

		@SerializedName("date")
		private String date;

		@SerializedName("score")
		private String score;

		@SerializedName("score_percentage")
		private double scorePercentage;

		@SerializedName("total_score")
		private String totalScore;

		public String getDate(){
			return date;
		}

		public String getScore(){
			return score;
		}

		public double getScorePercentage(){
			return scorePercentage;
		}

		public String getTotalScore(){
			return totalScore;
		}
	}


	public class Data{

		@SerializedName("user")
		private User user;

		public User getUser(){
			return user;
		}
	}
}