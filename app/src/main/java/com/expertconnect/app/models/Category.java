package com.expertconnect.app.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chinar on 24/10/16.
 */
public class Category{
        @SerializedName("category_id")
        private String categoryId;

        @SerializedName("category_name")
        private String categoryName;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }


}
