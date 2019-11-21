package com.quizest.quizestapp.ModelPackage;

import com.google.gson.annotations.SerializedName;

public class UserLogin{


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

	public class UserInfo{

		@SerializedName("zip")
		private Object zip;

		@SerializedName("country")
		private Object country;

		@SerializedName("email_notification_status")
		private int emailNotificationStatus;

		@SerializedName("role")
		private int role;

		@SerializedName("email_verified")
		private String emailVerified;

		@SerializedName("address")
		private Object address;

		@SerializedName("device_id")
		private Object deviceId;

		@SerializedName("city")
		private Object city;

		@SerializedName("photo")
		private Object photo;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("language")
		private String language;

		@SerializedName("device_type")
		private int deviceType;

		@SerializedName("push_notification_status")
		private int pushNotificationStatus;

		@SerializedName("user_coin")
		private Object userCoin;

		@SerializedName("reset_code")
		private String resetCode;

		@SerializedName("active_status")
		private int activeStatus;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("phone")
		private Object phone;

		@SerializedName("name")
		private String name;

		@SerializedName("id")
		private int id;

		@SerializedName("state")
		private Object state;

		@SerializedName("email")
		private String email;

		public Object getZip(){
			return zip;
		}

		public Object getCountry(){
			return country;
		}

		public int getEmailNotificationStatus(){
			return emailNotificationStatus;
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

		public Object getDeviceId(){
			return deviceId;
		}

		public Object getCity(){
			return city;
		}

		public Object getPhoto(){
			return photo;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public String getLanguage(){
			return language;
		}

		public int getDeviceType(){
			return deviceType;
		}

		public int getPushNotificationStatus(){
			return pushNotificationStatus;
		}

		public Object getUserCoin(){
			return userCoin;
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

		public Object getPhone(){
			return phone;
		}

		public String getName(){
			return name;
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

	public class Data{

		@SerializedName("access_token")
		private String accessToken;

		@SerializedName("total_coin")
		private int totalCoin;

		@SerializedName("access_type")
		private String accessType;

		@SerializedName("admob_coin")
		private int admobCoin;

		@SerializedName("user_info")
		private UserInfo userInfo;

		@SerializedName("total_point")
		private String totalPoint;

		public String getAccessToken(){
			return accessToken;
		}

		public int getTotalCoin(){
			return totalCoin;
		}

		public String getAccessType(){
			return accessType;
		}

		public int getAdmobCoin(){
			return admobCoin;
		}

		public UserInfo getUserInfo(){
			return userInfo;
		}

		public String getTotalPoint(){
			return totalPoint;
		}
	}
}