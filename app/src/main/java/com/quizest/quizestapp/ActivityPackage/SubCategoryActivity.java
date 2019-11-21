package com.quizest.quizestapp.ActivityPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.AdapterPackage.SubCategoryRecyclerAdapter;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.SubCategoryList.SubCategory;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.GlideApp;
import com.quizest.quizestapp.UtilPackge.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView rv_sub_category;
    TextView tvCategoryName;
    ImageButton btnSetting, btnBack;
    ImageView iv_sub_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        initViews();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, SettingActivity.class);
                startActivity(intent);
                SubCategoryActivity.this.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                finish();
            }
        });


        String catID = getIntent().getStringExtra(Util.QUIZLIST);
        String catImage = getIntent().getStringExtra("CATEGORY_IMAGE");
        String catName = getIntent().getStringExtra("CATEGORY_NAME");

        if (catImage != null && !catImage.equals("")) {
            GlideApp.with(this).load(catImage).into(iv_sub_icon);
        }

        if (catName != null) {
            tvCategoryName.setText(catName);
        }

        if (catID != null) {
            getSubCategories(catID);
        }


        rv_sub_category.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getSubCategories(String id) {
        final ProgressDialog dialog = Util.showDialog(this);
        final Storage storage = new Storage(this);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> categoryCall = retrofitInterface.getSubCategoryList(RetrofitClient.SUBCATEGORY_URL + id, storage.getAccessToken());
        categoryCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), SubCategoryActivity.this, dialog);

                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*save the category response for offline uses*/
                            storage.saveCategoryResponse(response.body());
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            SubCategory categoryList = gson.fromJson(response.body(), SubCategory.class);
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*add the data to the recycler view*/
                            SubCategoryRecyclerAdapter subCategoryRecyclerAdapter = new SubCategoryRecyclerAdapter(SubCategoryActivity.this, categoryList.getSubCategoryList(), categoryList.getUserAvailableCoin());
                            rv_sub_category.setAdapter(subCategoryRecyclerAdapter);

                        } else {
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(SubCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Util.dissmisDialog(dialog);
                    Toast.makeText(SubCategoryActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*dismiss the dialog*/
                Util.dissmisDialog(dialog);
                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    Toast.makeText(SubCategoryActivity.this, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initViews() {
        rv_sub_category = findViewById(R.id.rv_sub_category);
        tvCategoryName = findViewById(R.id.tv_cat_name);
        btnSetting = findViewById(R.id.btn_setting);
        btnBack = findViewById(R.id.btn_back);
        iv_sub_icon = findViewById(R.id.iv_sub_icon);
    }
}
