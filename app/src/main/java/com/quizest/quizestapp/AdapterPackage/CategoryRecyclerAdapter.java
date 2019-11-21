package com.quizest.quizestapp.AdapterPackage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quizest.quizestapp.ActivityPackage.BuyCoin.BuyCoinActivity;
import com.quizest.quizestapp.ActivityPackage.QuizActivity;
import com.quizest.quizestapp.ActivityPackage.SubCategoryActivity;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.CategoryList;
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
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryHolder> {

    /*all global field instances*/
    private Activity activity;
    private List<CategoryList.CategoryListItem> categoryModelList;
    private int userCoin;

    public CategoryRecyclerAdapter(List<CategoryList.CategoryListItem> categoryModelList, Activity activity, int userAvailableCoin) {
        this.categoryModelList = categoryModelList;
        this.activity = activity;
        this.userCoin = userAvailableCoin;
    }


    /*here all row layout get inflated*/
    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_recycler_row, parent, false);
        return new CategoryHolder(view);
    }


    /*here all data binding happens*/
    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        if (categoryModelList.get(position).getIsLocked() == 1) {
            holder.categoryLockIndicator.setVisibility(View.VISIBLE);
        } else if (categoryModelList.get(position).getIsLocked() == 0) {
            holder.categoryLockIndicator.setVisibility(View.INVISIBLE);
        }
        holder.categoryName.setText(categoryModelList.get(position).getName());
        GlideApp.with(activity).load(categoryModelList.get(position).getImage()).into(holder.categoryImageView);
        holder.categoryBackgroundImage.setImageResource(Util.getRandomCategoryGradient(Util.generateRandom(Util.LAST_GRADIENT)));
    }


    //    get the count of the list
    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    /*this is the custom view holder class for recycler view */

    class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView categoryName;

        ImageView categoryImageView, categoryBackgroundImage, categoryLockIndicator;

        CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            categoryImageView = itemView.findViewById(R.id.iv_category_img);
            categoryName = itemView.findViewById(R.id.tv_category_name);
            categoryBackgroundImage = itemView.findViewById(R.id.iv_category_bg);
            categoryLockIndicator = itemView.findViewById(R.id.iv_lock_indicator);
        }


        //        if user clicks on any category then take him to the Quiz acitivty
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("DefaultLocale")
        @Override
        public void onClick(View view) {

            Storage storage = new Storage(activity);
            if (categoryModelList.get(getAdapterPosition()).getIsLocked() == 1) {

                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.unlock_category_dialog_layout);
                final TextView categoryName = dialog.findViewById(R.id.tv_dialog_categoryname);
                TextView coinNeed = dialog.findViewById(R.id.tv_dialog_coin_need);
                TextView coinIHave = dialog.findViewById(R.id.tv_dialog_coin_ihave);
                Button letStart = dialog.findViewById(R.id.btn_let_start);
                Button cancel = dialog.findViewById(R.id.btn_cancel);
                final Button buyCoin = dialog.findViewById(R.id.btn_buy_coin);

                final LinearLayout coin;
                final ProgressBar progressBar;
                final TextView message;

                message = dialog.findViewById(R.id.tv_message);
                coin = dialog.findViewById(R.id.layout_coin);
                progressBar = dialog.findViewById(R.id.pb_progress);

                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.alert_dialog;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                categoryName.setText(String.format("To Unlock %s", categoryModelList.get(getAdapterPosition()).getName()));
                coinNeed.setText(String.format("%d Coin", categoryModelList.get(getAdapterPosition()).getCoin()));
                coinIHave.setText(String.format("%d", userCoin));

                letStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandCoin(categoryModelList.get(getAdapterPosition()).getCoin(), progressBar, coin, message, categoryName, buyCoin);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                // coinIHave.setText(String.format("%d Coin", categoryModelList.get(getAdapterPosition()).get));

                dialog.show();

            } else if (categoryModelList.get(getAdapterPosition()).getIsLocked() == 0) {
                if (categoryModelList.get(getAdapterPosition()).getSub_category() > 0) {
                    /*there is subcategory to opoen the subcategory section*/
                    Intent intent = new Intent(activity, SubCategoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Util.QUIZLIST, categoryModelList.get(getAdapterPosition()).getCategoryId());
                    intent.putExtra("CATEGORY_NAME", categoryModelList.get(getAdapterPosition()).getName());
                    intent.putExtra("CATEGORY_IMAGE", categoryModelList.get(getAdapterPosition()).getImage());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

                } else {
                    /*there is no sub category so direct browse to question*/
                    Intent intent = new Intent(activity, QuizActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Util.QUIZLIST, categoryModelList.get(getAdapterPosition()).getCategoryId());
                    intent.putExtra("CATEGORY_NAME", categoryModelList.get(getAdapterPosition()).getName());
                    intent.putExtra("TYPE", Util.CATEGORY_QUESTION);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                    activity.finish();
                }

            }


        }


        private void expandCoin(int amount, final ProgressBar progressBar, LinearLayout coin, final TextView message, TextView categoryName, final Button buyCoin) {
            progressBar.setVisibility(View.VISIBLE);
            categoryName.setVisibility(View.GONE);
            coin.setVisibility(View.GONE);
            Storage storage = new Storage(activity);
            RetrofitInterface networkInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
            Call<String> expandCoint = networkInterface.expandCoin(storage.getAccessToken(), amount);
            expandCoint.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    /*handle error globally */
                    ErrorHandler.getInstance().handleError(response.code(), activity, null);
                    if (response.isSuccessful()) {
                        /*success true*/
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                /*get all the error messages and show to the user*/
                                String message = jsonObject.getString("message");
                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                /*request to unlock the category*/
                                unLockCategory(categoryModelList.get(getAdapterPosition()).getId());

                            } else {
                                /*get all the error messages and show to the user*/
                                String messageTxt = jsonObject.getString("message");
                                progressBar.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);
                                buyCoin.setVisibility(View.VISIBLE);
                                buyCoin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        activity.startActivity(
                                                new Intent(
                                                        activity,
                                                        BuyCoinActivity.class
                                                )
                                        );
                                    }
                                });
                                message.setText(messageTxt);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(activity, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    /*handle network error and notify the user*/
                    if (t instanceof SocketTimeoutException || t instanceof IOException) {
                        Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void unLockCategory(int id) {
            Storage storage = new Storage(activity);
            RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
            Call<String> unlockCall = retrofitInterface.unlockCategory(storage.getAccessToken(), id);
            unlockCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    /*handle error globally */
                    ErrorHandler.getInstance().handleError(response.code(), activity, null);
                    if (response.isSuccessful()) {
                        /*success true*/
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                /*get all the error messages and show to the user*/
                                String message = jsonObject.getString("message");
                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                if (categoryModelList.get(getAdapterPosition()).getSub_category() > 0) {
                                    /*there is subcategory to opoen the subcategory section*/
                                    Intent intent = new Intent(activity, SubCategoryActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra(Util.QUIZLIST, categoryModelList.get(getAdapterPosition()).getCategoryId());
                                    intent.putExtra("CATEGORY_NAME", categoryModelList.get(getAdapterPosition()).getName());
                                    intent.putExtra("CATEGORY_IMAGE", categoryModelList.get(getAdapterPosition()).getImage());
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                                } else {
                                    /*there is no sub category so direct browse to question*/
                                    Intent intent = new Intent(activity, QuizActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra(Util.QUIZLIST, categoryModelList.get(getAdapterPosition()).getCategoryId());
                                    intent.putExtra("CATEGORY_NAME", categoryModelList.get(getAdapterPosition()).getName());
                                    intent.putExtra("TYPE", Util.CATEGORY_QUESTION);
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                                    activity.finish();
                                }
                            } else {
                                /*get all the error messages and show to the user*/
                                String messageTxt = jsonObject.getString("message");
                                Toast.makeText(activity, messageTxt, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(activity, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    /*handle network error and notify the user*/
                    if (t instanceof SocketTimeoutException || t instanceof IOException) {
                        Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
