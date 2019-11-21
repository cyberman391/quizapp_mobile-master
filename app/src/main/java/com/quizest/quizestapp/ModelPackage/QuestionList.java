package com.quizest.quizestapp.ModelPackage;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class QuestionList implements Serializable {

	@SerializedName("user_available_point")
	private int userAvailablePoint;

	@SerializedName("user_available_coin")
	private int userAvailableCoin;

	@SerializedName("totalPoint")
	private int totalPoint;

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("hints_coin")
	private String hintsCoin;

	@SerializedName("totalCoin")
	private int totalCoin;

	@SerializedName("availableQuestionList")
	private List<AvailableQuestionListItem> availableQuestionList;

	@SerializedName("message")
	private String message;

	@SerializedName("totalQuestion")
	private int totalQuestion;

	public int getUserAvailablePoint(){
		return userAvailablePoint;
	}

	public int getUserAvailableCoin(){
		return userAvailableCoin;
	}

	public int getTotalPoint(){
		return totalPoint;
	}

	public List<Object> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getHintsCoin(){
		return hintsCoin;
	}

	public int getTotalCoin(){
		return totalCoin;
	}

	public List<AvailableQuestionListItem> getAvailableQuestionList(){
		return availableQuestionList;
	}

	public String getMessage(){
		return message;
	}

	public int getTotalQuestion(){
		return totalQuestion;
	}

	public class OptionsItem implements Serializable{

		@SerializedName("question_option")
		private String questionOption;

		@SerializedName("id")
		private int id;

		@SerializedName("type")
		private int type;

		public String getQuestionOption(){
			return questionOption;
		}

		public int getId(){
			return id;
		}

		public int getType(){
			return type;
		}
	}

	public class AvailableQuestionListItem implements Serializable{

		@SerializedName("image")
		private String image;

		@SerializedName("skip_coin")
		private int skipCoin;

		@SerializedName("time_limit")
		private int timeLimit;

		@SerializedName("hints")
		private String hints;

		@SerializedName("option_type")
		private int optionType;

		@SerializedName("title")
		private String title;

		@SerializedName("question_id")
		private String questionId;

		@SerializedName("point")
		private int point;

		@SerializedName("category_id")
		private int categoryId;

		@SerializedName("has_image")
		private int hasImage;

		@SerializedName("options")
		private List<OptionsItem> options;

		@SerializedName("id")
		private int id;

		@SerializedName("category")
		private String category;

		@SerializedName("coin")
		private int coin;

		@SerializedName("status")
		private int status;

		public String getImage(){
			return image;
		}

		public int getSkipCoin(){
			return skipCoin;
		}

		public int getTimeLimit(){
			return timeLimit;
		}

		public String getHints(){
			return hints;
		}

		public int getOptionType(){
			return optionType;
		}

		public String getTitle(){
			return title;
		}

		public String getQuestionId(){
			return questionId;
		}

		public int getPoint(){
			return point;
		}

		public int getCategoryId(){
			return categoryId;
		}

		public int getHasImage(){
			return hasImage;
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

		public int getCoin(){
			return coin;
		}

		public int getStatus(){
			return status;
		}
	}
}