package com.quizest.quizestapp.ModelPackage;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LeaderBoard{

	@SerializedName("success")
	private boolean success;

	@SerializedName("leaderList")
	private List<LeaderListItem> leaderList;

	public boolean isSuccess(){
		return success;
	}

	public List<LeaderListItem> getLeaderList(){
		return leaderList;
	}


	public class LeaderListItem{

		@SerializedName("score")
		private String score;

		@SerializedName("user_id")
		private int userId;

		@SerializedName("name")
		private String name;

		@SerializedName("photo")
		private String photo;

		@SerializedName("ranking")
		private int ranking;

		public String getScore(){
			return score;
		}

		public int getUserId(){
			return userId;
		}

		public String getName(){
			return name;
		}

		public String getPhoto(){
			return photo;
		}

		public int getRanking(){
			return ranking;
		}
	}
}