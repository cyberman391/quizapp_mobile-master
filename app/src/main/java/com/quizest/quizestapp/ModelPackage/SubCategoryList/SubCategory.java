package com.quizest.quizestapp.ModelPackage.SubCategoryList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SubCategory{

	@SerializedName("user_available_point")
	private String userAvailablePoint;

	@SerializedName("user_available_coin")
	private int userAvailableCoin;

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("sub_category_list")
	private List<SubCategoryListItem> subCategoryList;

	@SerializedName("success")
	private boolean success;

	@SerializedName("parent_category_name")
	private String parentCategoryName;

	@SerializedName("message")
	private String message;

	public String getUserAvailablePoint(){
		return userAvailablePoint;
	}

	public int getUserAvailableCoin(){
		return userAvailableCoin;
	}

	public List<Object> getData(){
		return data;
	}

	public List<SubCategoryListItem> getSubCategoryList(){
		return subCategoryList;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getParentCategoryName(){
		return parentCategoryName;
	}

	public String getMessage(){
		return message;
	}


	public class SubCategoryListItem{

		@SerializedName("max_limit")
		private int maxLimit;

		@SerializedName("image")
		private String image;

		@SerializedName("is_locked")
		private int isLocked;

		@SerializedName("time_limit")
		private int timeLimit;

		@SerializedName("description")
		private String description;

		@SerializedName("qs_limit")
		private int qsLimit;

		@SerializedName("serial")
		private int serial;

		@SerializedName("sub_category_id")
		private String subCategoryId;

		@SerializedName("name")
		private String name;

		@SerializedName("id")
		private int id;

		@SerializedName("status")
		private int status;

		@SerializedName("coin")
		private int coin;

		@SerializedName("question_amount")
		private int questionAmount;

		public int getMaxLimit(){
			return maxLimit;
		}

		public String getImage(){
			return image;
		}

		public int getIsLocked(){
			return isLocked;
		}

		public int getTimeLimit(){
			return timeLimit;
		}

		public String getDescription(){
			return description;
		}

		public int getQsLimit(){
			return qsLimit;
		}

		public int getSerial(){
			return serial;
		}

		public String getSubCategoryId(){
			return subCategoryId;
		}

		public String getName(){
			return name;
		}

		public int getId(){
			return id;
		}

		public int getStatus(){
			return status;
		}

		public int getCoin(){
			return coin;
		}

		public int getQuestionAmount(){
			return questionAmount;
		}
	}
}