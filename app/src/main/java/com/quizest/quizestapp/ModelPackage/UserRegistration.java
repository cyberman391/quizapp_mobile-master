package com.quizest.quizestapp.ModelPackage;

import com.google.gson.annotations.SerializedName;

public class UserRegistration{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("key")
	private String key;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public String getKey(){
		return key;
	}


	public class UserInfo{

		@SerializedName("reset_code")
		private String resetCode;

		@SerializedName("role")
		private int role;

		@SerializedName("active_status")
		private int activeStatus;

		@SerializedName("email_verified")
		private int emailVerified;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("phone")
		private String phone;

		@SerializedName("name")
		private String name;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("language")
		private String language;

		@SerializedName("id")
		private int id;

		@SerializedName("email")
		private String email;

		public String getResetCode(){
			return resetCode;
		}

		public int getRole(){
			return role;
		}

		public int getActiveStatus(){
			return activeStatus;
		}

		public int getEmailVerified(){
			return emailVerified;
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

		public String getCreatedAt(){
			return createdAt;
		}

		public String getLanguage(){
			return language;
		}

		public int getId(){
			return id;
		}

		public String getEmail(){
			return email;
		}


	}

	public class Data{

		@SerializedName("access_token")
		private String accessToken;

		@SerializedName("access_type")
		private String accessType;

		@SerializedName("user_info")
		private UserInfo userInfo;

		@SerializedName("admob_coin")
		private int admobCoin;

		public int getAdmobCoin() {
			return admobCoin;
		}

		public String getAccessToken(){
			return accessToken;
		}

		public String getAccessType(){
			return accessType;
		}

		public UserInfo getUserInfo(){
			return userInfo;
		}

	}
}