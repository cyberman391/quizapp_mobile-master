package com.quizest.quizestapp.NetworkPackage;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuizList{

	@SerializedName("success")
	private boolean success;

	@SerializedName("availableQuestionList")
	private List<AvailableQuestionListItem> availableQuestionList;

	public boolean isSuccess(){
		return success;
	}

	public List<AvailableQuestionListItem> getAvailableQuestionList(){
		return availableQuestionList;
	}


	public class OptionsItem{

		@SerializedName("option_title")
		private String optionTitle;

		@SerializedName("id")
		private int id;

		public String getOptionTitle(){
			return optionTitle;
		}

		public int getId(){
			return id;
		}
	}


	public class AvailableQuestionListItem{

		@SerializedName("image")
		private String image;

		@SerializedName("category_id")
		private int categoryId;

		@SerializedName("time_limit")
		private int timeLimit;

		@SerializedName("options")
		public  List<OptionsItem> options;

		@SerializedName("id")
		private int id;

		@SerializedName("category")
		private String category;

		@SerializedName("title")
		private String title;

		@SerializedName("type")
		private int type;

		@SerializedName("question_id")
		private String questionId;

		@SerializedName("point")
		private int point;

		@SerializedName("coin")
		private int coin;

		@SerializedName("status")
		private int status;

		public String getImage(){
			return image;
		}

		public int getCategoryId(){
			return categoryId;
		}

		public int getTimeLimit(){
			return timeLimit;
		}

		public List<OptionsItem> getOptions(){
			return options;
		}

		public int getId(){
			return id;
		}

		public String getCategory(){
			return category;
		}

		public String getTitle(){
			return title;
		}

		public int getType(){
			return type;
		}

		public String getQuestionId(){
			return questionId;
		}

		public int getPoint(){
			return point;
		}

		public int getCoin(){
			return coin;
		}

		public int getStatus(){
			return status;
		}
	}
}