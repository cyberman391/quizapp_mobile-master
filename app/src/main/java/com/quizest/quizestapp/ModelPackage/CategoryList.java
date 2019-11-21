package com.quizest.quizestapp.ModelPackage;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CategoryList {

    @SerializedName("user_available_point")
    private String userAvailablePoint;

    @SerializedName("category_list")
    private List<CategoryListItem> categoryList;

    @SerializedName("user_available_coin")
    private int userAvailableCoin;

    @SerializedName("data")
    private List<Object> data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public String getUserAvailablePoint() {
        return userAvailablePoint;
    }

    public List<CategoryListItem> getCategoryList() {
        return categoryList;
    }

    public int getUserAvailableCoin() {
        return userAvailableCoin;
    }

    public List<Object> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public class CategoryListItem {

        @SerializedName("max_limit")
        private int maxLimit;

        @SerializedName("image")
        private String image;

        @SerializedName("is_locked")
        private int isLocked;

        @SerializedName("time_limit")
        private int timeLimit;

        @SerializedName("sub_category")
        private int sub_category;

        @SerializedName("description")
        private String description;

        @SerializedName("qs_limit")
        private int qsLimit;

        @SerializedName("category_id")
        private String categoryId;

        @SerializedName("serial")
        private int serial;

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

        public int getMaxLimit() {
            return maxLimit;
        }

        public String getImage() {
            return image;
        }

        public int getIsLocked() {
            return isLocked;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public String getDescription() {
            return description;
        }

        public int getQsLimit() {
            return qsLimit;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public int getSub_category() {
            return sub_category;
        }

        public int getSerial() {
            return serial;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getStatus() {
            return status;
        }

        public int getCoin() {
            return coin;
        }

        public int getQuestionAmount() {
            return questionAmount;
        }
    }
}