package com.quizest.quizestapp.AdapterPackage;
/*all used classes are imported here*/

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.quizest.quizestapp.ActivityPackage.QuizActivity;
import com.quizest.quizestapp.FragmentPackage.DashboardFragments.QuizFragment;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.QuestionList;
import com.quizest.quizestapp.ModelPackage.SubminAnswer;
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

public class QuizOptionsRecyclerRow extends RecyclerView.Adapter<QuizOptionsRecyclerRow.QuizOptionsHolder> {


    private List<QuestionList.OptionsItem> optionsItemList;
    private Activity activity;
    private String questionID;
    private int point;
    private int coin;
    private int Rightid = 0;
    TextView textView, userCoin;
    QuizFragment quizFragment;
    ImageView imageView;
    private boolean isAnswered = false;

    /*this is the constructor for the leaderboard */
    public QuizOptionsRecyclerRow(QuizFragment quizFragment, List<QuestionList.OptionsItem> optionsItemList, Activity activity, String questionID, int point, int coin, ImageView imageView, TextView tv_user_point, TextView tvUserCoin) {
        this.optionsItemList = optionsItemList;
        this.point = point;
        this.activity = activity;
        this.questionID = questionID;
        this.imageView = imageView;
        textView = tv_user_point;
        this.coin = coin;
        this.quizFragment = quizFragment;
        this.userCoin = tvUserCoin;
    }


    /*this is the function where every row layout inflate for the recycler view */
    @NonNull
    @Override
    public QuizOptionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_options_recycler_row, parent, false);
        return new QuizOptionsHolder(view);
    }

    /*this is the place for data binding for recycler view */
    @Override
    public void onBindViewHolder(@NonNull final QuizOptionsHolder holder, final int position) {
        /*check if user has not selected right answer*/
        if (Rightid != 0) {
            if (optionsItemList.get(position).getId() == Rightid) {
                /*if not then set the background green for the right answer*/
                holder.quizOptionName.setTextColor(activity.getResources().getColor(R.color.color_white));
                //holder.quizOptionBg.setImageResource(R.drawable.quiz_option_bg_right);
                if (optionsItemList.get(position).getType() == 0) {
                    holder.quizImageOverLap.setVisibility(View.GONE);
                    GlideApp.with(activity).load(R.drawable.quiz_option_bg_right).into(holder.quizOptionBg);
                } else if (optionsItemList.get(position).getType() == 1) {
                    holder.quizImageOverLap.setVisibility(View.VISIBLE);
                    GlideApp.with(activity).load(R.drawable.quzzle_option_right).into(holder.quizImageOverLap);
                }
                Rightid = 0;
            }
        }
        /*set the option name */
        if (optionsItemList.get(position).getQuestionOption() != null) {
            if (optionsItemList.get(position).getType() == 0) {
                holder.quizOptionName.setVisibility(View.VISIBLE);
                holder.quizOptionName.setText(Util.convertUnCapitalized(optionsItemList.get(position).getQuestionOption()));
            } else if (optionsItemList.get(position).getType() == 1) {
                holder.quizOptionName.setVisibility(View.GONE);
                holder.pb_load_profile.setVisibility(View.VISIBLE);
                GlideApp.with(activity).load(optionsItemList.get(position).getQuestionOption()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pb_load_profile.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_load_profile.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.quizOptionBg);
            }

        }

        holder.cardQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if user has not tap on any options, do the api call of submit answer */
                if (!isAnswered) {
                    submitAnswer(questionID, holder.getAdapterPosition(), holder);
                }
            }
        });

        /*    *//*click listener for the options*//*
        holder.quizOptionBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return optionsItemList.size();
    }


    /*quiz holder for the recycler view*/
    class QuizOptionsHolder extends RecyclerView.ViewHolder {
        ProgressBar pb_load_profile;
        ImageView quizOptionBg, quizImageOverLap;
        CardView cardQuiz;
        TextView quizOptionName;

        public QuizOptionsHolder(View itemView) {
            super(itemView);
            pb_load_profile = itemView.findViewById(R.id.pb_load_profile);
            quizImageOverLap = itemView.findViewById(R.id.img_quiz_overlap);
            cardQuiz = itemView.findViewById(R.id.card_quiz);
            quizOptionBg = itemView.findViewById(R.id.img_quiz_option);
            quizOptionName = itemView.findViewById(R.id.tv_quiz_option_name);
        }
    }


    /*call this function to submit anser by options id, position, and holder*/
    public void submitAnswer(final String optionID, final int position, final QuizOptionsHolder holder) {
        final Storage storage = new Storage(activity);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        final Call<String> submitAnswerCall = retrofitInterface.submitAnswer(RetrofitClient.SUBMIT_ANSWER_ULR + optionID, storage.getAccessToken(), optionsItemList.get(position).getId());
        submitAnswerCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), activity, null);
                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (!isSuccess) {
                            isAnswered = true;
                            imageView.setImageResource(R.drawable.ic_cat_worng);
                            //  holder.quizOptionBg.setImageResource(R.drawable.quiz_option_wrong);
                            /*if answer is not right*/
                            QuizActivity.isPlayed.put(optionID, true);
                            // try {

                            /*if sound is avaiable then play sound*/
                            if (storage.getSoundState()) {
                                Util.playWrongMusic(activity);
                                Util.vibratePhone(900, activity);
                                /*serialize the String response  */
                            }

//
                            holder.quizOptionName.setTextColor(activity.getResources().getColor(R.color.color_white));
                            if (optionsItemList.get(position).getType() == 0) {
                                holder.quizImageOverLap.setVisibility(View.GONE);
                                GlideApp.with(activity).load(R.drawable.quiz_option_wrong).into(holder.quizOptionBg);
                            } else if (optionsItemList.get(position).getType() == 1) {
                                holder.quizImageOverLap.setVisibility(View.VISIBLE);
                                GlideApp.with(activity).load(R.drawable.quzzle_option_wrong).into(holder.quizImageOverLap);
                            }

                            Gson gson = new Gson();
                            SubminAnswer submitAnswer = gson.fromJson(response.body(), SubminAnswer.class);

                            Rightid = submitAnswer.getRightAnswer().getOptionId();

                            notifyDataSetChanged();

                     /*       } catch (IllegalStateException e) {

                                QuizActivity.isPlayed.put(optionID, true);

                                imageView.setImageResource(R.drawable.ic_cat_worng);
                                if (storage.getSoundState()) {
                                    Util.playWrongMusic(activity);
                                    Util.vibratePhone(900, activity);
                                }
                                *//*serialize the String response  *//*

                                holder.quizOptionName.setTextColor(activity.getResources().getColor(R.color.color_white));
                                GlideApp.with(activity).load(R.drawable.quiz_option_wrong).into(holder.quizOptionBg);

                                isAnswered = true;

                            } catch (JsonSyntaxException e) {
                                QuizActivity.isPlayed.put(optionID, true);

                                imageView.setImageResource(R.drawable.ic_cat_worng);
                                if (storage.getSoundState()) {
                                    Util.playWrongMusic(activity);
                                    Util.vibratePhone(900, activity);
                                }

                                *//*serialize the String response  *//*

                                holder.quizOptionName.setTextColor(activity.getResources().getColor(R.color.color_white));
                                GlideApp.with(activity).load(R.drawable.quiz_option_wrong).into(holder.quizOptionBg);
                               // holder.quizOptionBg.setImageResource(R.drawable.quiz_option_wrong);

                                isAnswered = true;

                                Gson gson = new Gson();
                                SubminAnswer submitAnswer = gson.fromJson(response.body(), SubminAnswer.class);
                                isAnswered = true;
                                Rightid = submitAnswer.getRightAnswer().getOptionId();

                                notifyDataSetChanged();
                            }
*/

                        } else {

                            /*if the answer is right */

                            /*make global value isPlayed to true*/
                            QuizActivity.isPlayed.put(optionID, true);
                            String total_point = jsonObject.getString("total_point");
                            int total_coin = jsonObject.getInt("total_coin");
                            /*set global quiz point*/
                            Util.QuizPoint = Integer.parseInt(total_point);
                            textView.setText(String.valueOf(total_coin));
                            userCoin.setText(String.valueOf(total_point));

                            imageView.setImageResource(R.drawable.ic_cat);
                            /*update total point*/
                            Util.TOTAL_POINT = Util.TOTAL_POINT + point;

                            Util.TOTAL_COIN = Util.TOTAL_COIN + coin;

                            /*increase user total point and coin*/
                            quizFragment.TOTAL_COIN = quizFragment.TOTAL_COIN + coin;
                            quizFragment.TOTAL_POINT = quizFragment.TOTAL_POINT + point;

                            /*if sound options available then play the sound*/
                            if (storage.getSoundState()) {
                                Util.playRightMusing(activity);
                            }
                            isAnswered = true;
                            holder.quizOptionName.setTextColor(activity.getResources().getColor(R.color.color_white));
                            // holder.quizOptionBg.setImageResource(R.drawable.quiz_option_bg_right);
                            if (optionsItemList.get(position).getType() == 0) {
                                holder.quizImageOverLap.setVisibility(View.GONE);
                                GlideApp.with(activity).load(R.drawable.quiz_option_bg_right).into(holder.quizOptionBg);
                            } else if (optionsItemList.get(position).getType() == 1) {
                                holder.quizImageOverLap.setVisibility(View.VISIBLE);
                                GlideApp.with(activity).load(R.drawable.quzzle_option_right).into(holder.quizImageOverLap);
                            }


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
