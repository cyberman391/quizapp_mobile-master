package com.quizest.quizestapp.ModelPackage;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserSetting implements Serializable{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public class User implements Serializable {

		@SerializedName("language")
		private String language;

		@SerializedName("id")
		private int id;

		public String getLanguage(){
			return language;
		}

		public int getId(){
			return id;
		}
	}

	public class LangItem implements Serializable{

		@SerializedName("value")
		private String value;

		@SerializedName("key")
		private String key;

		public String getValue(){
			return value;
		}

		public String getKey(){
			return key;
		}
	}

	public class Data implements Serializable{

		@SerializedName("lang")
		private List<LangItem> lang;

		@SerializedName("user")
		private User user;

		public List<LangItem> getLang(){
			return lang;
		}

		public User getUser(){
			return user;
		}
	}
}