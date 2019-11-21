package com.quizest.quizestapp.AdapterPackage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
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

import com.quizest.quizestapp.ActivityPackage.QuizActivity;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryRecyclerAdapter extends RecyclerView.Adapter<SubCategoryRecyclerAdapter.SubCatHolder> {


    /*all global field instances*/
    private Activity activity;
    private List<SubCategory.SubCategoryListItem> categoryModelList;
    private int userCoin;

    public SubCategoryRecyclerAdapter(Activity activity, List<SubCategory.SubCategoryListItem> categoryModelList, int userCoin) {
        this.activity = activity;
        this.categoryModelList = categoryModelList;
        this.userCoin = userCoin;
    }

    @NonNull
    @Override
    public SubCatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_subcategory_recycler_row, viewGroup, false);
        return new SubCatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCatHolder holder, int i) {
        if (categoryModelList.get(i).getIsLocked() == 1) {
            holder.ivSubLock.setVisibility(View.VISIBLE);
            holder.ivSubUnlock.setVisibility(View.GONE);
        } else if (categoryModelList.get(i).getIsLocked() == 0) {
            holder.ivSubUnlock.setVisibility(View.VISIBLE);
            holder.ivSubLock.setVisibility(View.GONE);
        }
        holder.tvSubCatName.setText(categoryModelList.get(i).getName());
        GlideApp.with(activity).load(categoryModelList.get(i).getImage()).into(holder.ivSubIcon);
        //Log.e("MKLOG", categoryModelList.get(i).getImage());
        holder.layout_sub_cat.setBackgroundResource(Util.getRandomCategoryGradient(Util.generateRandom(Util.LAST_GRADIENT)));
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class SubCatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivSubIcon;
        ImageView ivSubLock;
        ImageView ivSubUnlock;
        TextView tvSubCatName;
        LinearLayout layout_sub_cat;

        public SubCatHolder(@NonNull View itemView) {
            super(itemView);
            layout_sub_cat = itemView.findViewById(R.id.layout_sub_cat);
            ivSubIcon = itemView.findViewById(R.id.iv_sub_icon);
            ivSubLock = itemView.findViewById(R.id.iv_sub_lock);
            ivSubUnlock = itemView.findViewById(R.id.iv_sub_unlock);
            tvSubCatName = itemView.findViewById(R.id.tv_sub_name);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onClick(View v) {
            Storage storage = new Storage(activity);
            if (categoryModelList.get(getAdapterPosition()).getIsLocked() == 1) {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.unlock_category_dialog_layout);
                final TextView categoryName = dialog.findViewById(R.id.tv_dialog_categoryname);
                TextView coinNeed = dialog.findViewById(R.id.tv_dialog_coin_need);
                TextView coinIHave = dialog.findViewById(R.id.tv_dialog_coin_ihave);
                Button letStart = dialog.findViewById(R.id.btn_let_start);
                Button cancel = dialog.findViewById(R.id.btn_cancel);

                final LinearLayout coin;
                final ProgressBar progressBar;
                final TextView message;

                message = dialog.findViewById(R.id.tv_message);
                coin = dialog.findViewById(R.id.layout_coin);
                progressBar = dialog.findViewById(R.id.pb_progress);

                dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                categoryName.setText(String.format("To Unlock %s", categoryModelList.get(getAdapterPosition()).getName()));
                coinNeed.setText(String.format("%d Coin", categoryModelList.get(getAdapterPosition()).getCoin()));
                coinIHave.setText(String.format("%d", userCoin));

                letStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandCoin(categoryModelList.get(getAdapterPosition()).getCoin(), progressBar, coin, message, categoryName);
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
                Intent intent = new Intent(activity, QuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Util.QUIZLIST, categoryModelList.get(getAdapterPosition()).getSubCategoryId());
                intent.putExtra("TYPE", Util.SUBCATEGORY_QUESTON);
                intent.putExtra("CATEGORY_NAME", categoryModelList.get(getAdapterPosition()).getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                activity.finish();
            }
        }

        private void expandCoin(int amount, final ProgressBar progressBar, LinearLayout coin, final TextView message, TextView categoryName) {
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
                                Intent intent = new Intent(activity, QuizActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra(Util.QUIZLIST, categoryModelList.get(getAdapterPosition()).getSubCategoryId());
                                intent.putExtra("TYPE", Util.SUBCATEGORY_QUESTON);
                                intent.putExtra("CATEGORY_NAME", categoryModelList.get(getAdapterPosition()).getName());
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                                activity.finish();

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
