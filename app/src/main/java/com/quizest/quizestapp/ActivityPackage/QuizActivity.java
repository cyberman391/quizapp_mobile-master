package com.quizest.quizestapp.ActivityPackage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.AdapterPackage.QuizViewPagerAdapter;
import com.quizest.quizestapp.FragmentPackage.DashboardFragments.QuizFragment;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.QuestionList;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.quizest.quizestapp.UtilPackge.Util.TOTAL_COIN;

public class QuizActivity extends AppCompatActivity {

    public boolean shownVideoAd = false;

    Storage storage;
    /*all global field instances are here*/
    ImageButton btn_back, btnSetting;
    Fragment currentFragment;
    public QuestionList questionList;
    String CATEGORYNAME;
    private Integer TYPE = null;

    private boolean isStarted = false;

    public static HashMap<String, Boolean> isPlayed;
    List<Fragment> quizList;
    public int x =
            1;
    public int c = 1;
    public static CountDownTimer countDownTimer;
    QuizViewPagerAdapter quizViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*add the layout to the activity*/
        setContentView(R.layout.activity_quiz);

        shownVideoAd = false;

        storage = new Storage(this);

        /*type casting the views*/
        btn_back = findViewById(R.id.btn_back);
        btnSetting = findViewById(R.id.btn_setting);

        /*if user clicks on the back button take user to MainActivity*/
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionList != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setMessage("This Quiz Session will be dismissed, wants to proceed?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    /*take the user to the result dialog*/
                                    Dialog dialogCongrats = new Dialog(QuizActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                                    dialogCongrats.setContentView(R.layout.layout_congrats_dialog);
                                    TextView earnedPoint = dialogCongrats.findViewById(R.id.tv_earned_point);
                                    TextView earnedCoin = dialogCongrats.findViewById(R.id.tv_earned_coin);
                                    Button quiz = dialogCongrats.findViewById(R.id.btn_quiz);

                                    dialogCongrats.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    quiz.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    earnedCoin.setText(String.valueOf(TOTAL_COIN));
                                    earnedPoint.setText(String.valueOf(Util.TOTAL_POINT));
                                    dialogCongrats.show();


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            //Set your icon here
                            .setTitle("Alert!")
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alert = builder.create();
                    alert.show();//showing the dialog
                } else {
                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    QuizActivity.this.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                    finish();
                }

            }
        });

        /*if user clicks on the setting button take the user to the Setting activity*/
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionList != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setMessage("This Quiz Session will be dismissed, wants to proceed?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    /*take the user to the result dialog*/
                                    Dialog dialogCongrats = new Dialog(QuizActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                                    dialogCongrats.setContentView(R.layout.layout_congrats_dialog);
                                    TextView earnedPoint = dialogCongrats.findViewById(R.id.tv_earned_point);
                                    TextView earnedCoin = dialogCongrats.findViewById(R.id.tv_earned_coin);
                                    Button quiz = dialogCongrats.findViewById(R.id.btn_quiz);

                                    dialogCongrats.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            Intent intent = new Intent(QuizActivity.this, SettingActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    quiz.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(QuizActivity.this, SettingActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    earnedCoin.setText(String.valueOf(TOTAL_COIN));
                                    earnedPoint.setText(String.valueOf(Util.TOTAL_POINT));
                                    dialogCongrats.show();


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            //Set your icon here
                            .setTitle("Alert!")
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alert = builder.create();
                    alert.show();//showing the dialog
                } else {
                    Intent intent = new Intent(QuizActivity.this, SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });

        /*track the total point of a user globally*/
        Util.TOTAL_POINT = 0;

        isStarted = false;

        TOTAL_COIN = 0;

        /*intilize the lists*/
        quizList = new ArrayList<>();

        isPlayed = new HashMap<>();

        isPlayed.clear();

        /*get the question id from intent and pass it to the getQuestionList function to do the api call for question list */

        String QUESTION_ID = getIntent().getStringExtra(Util.QUIZLIST);
        CATEGORYNAME = getIntent().getStringExtra("CATEGORY_NAME");
        TYPE = getIntent().getIntExtra("TYPE", 10);

        if (QUESTION_ID != null && TYPE != 10) {
            getQuestionList(QUESTION_ID, TYPE);
        }

    }


    /*this is called when user press back button*/
    @Override
    public void onBackPressed() {
        if (questionList != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
            builder.setMessage("This Quiz Session will be dismissed, wants to proceed?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            /*take the user to the result dialog*/
                            Dialog dialogCongrats = new Dialog(QuizActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                            dialogCongrats.setContentView(R.layout.layout_congrats_dialog);
                            TextView earnedPoint = dialogCongrats.findViewById(R.id.tv_earned_point);
                            TextView earnedCoin = dialogCongrats.findViewById(R.id.tv_earned_coin);
                            Button quiz = dialogCongrats.findViewById(R.id.btn_quiz);

                            dialogCongrats.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            quiz.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            earnedCoin.setText(String.valueOf(TOTAL_COIN));
                            earnedPoint.setText(String.valueOf(Util.TOTAL_POINT));
                            dialogCongrats.show();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    //Set your icon here
                    .setTitle("Alert!")
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alert = builder.create();
            alert.show();//showing the dialog
        } else {
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


    }


    /*this is the api call to get data from the server we passes question id to get the question list */
    private void getQuestionList(String QUESTION_ID, Integer type) {
        Storage storage = new Storage(this);
        final ProgressDialog dialog = Util.showDialog(this);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        final Call<String> questionCall = retrofitInterface.getQuizList(RetrofitClient.CATEGORY_URL + type + "/" + QUESTION_ID, storage.getAccessToken());
        Log.e("TOKEN", storage.getAccessToken());
        questionCall.enqueue(new Callback<String>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*handle http error globally*/
                ErrorHandler.getInstance().handleError(response.code(), QuizActivity.this, dialog);
                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        final boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*serialize the String response  */

                            Gson gson = new Gson();
                            questionList = gson.fromJson(response.body(), QuestionList.class);


                            if (questionList != null) {
                                if (questionList.getAvailableQuestionList().size() != 0) {

                                    /*show overview dialog first*/
                                    final Dialog overviewDialog = new Dialog(QuizActivity.this);
                                    overviewDialog.setContentView(R.layout.layout_questing_overview);
                                    overviewDialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
                                    overviewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    TextView categoryName = overviewDialog.findViewById(R.id.tv_which_category);
                                    TextView totalQuestion = overviewDialog.findViewById(R.id.tv_total_question);
                                    TextView totalPoint = overviewDialog.findViewById(R.id.tv_total_point);
                                    TextView totalCoin = overviewDialog.findViewById(R.id.tv_total_coin);


                                    categoryName.setText(String.format("In %s Category", CATEGORYNAME));
                                    totalQuestion.setText(String.format("%d", questionList.getTotalQuestion()));
                                    totalPoint.setText(String.format("%d", questionList.getTotalPoint()));
                                    totalCoin.setText(String.format("%d", questionList.getTotalCoin()));

                                    final Button letStart = overviewDialog.findViewById(R.id.btn_let_start);
                                    Button cancel = overviewDialog.findViewById(R.id.btn_cancel);

                                    letStart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (overviewDialog.isShowing()) {
                                                overviewDialog.dismiss();
                                            }

                                            isStarted = true;

                                            QuizFragment quizFragment = new QuizFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(Util.QUESTION, questionList.getAvailableQuestionList().get(0));
                                            bundle.putInt("TOTAL_POINT", questionList.getUserAvailablePoint());
                                            bundle.putInt("TOTAL_COIN", questionList.getUserAvailableCoin());
                                            if (questionList.getHintsCoin() != null) {
                                                bundle.putInt("HINT_POINT", Integer.parseInt(questionList.getHintsCoin()));
                                            } else {
                                                bundle.putInt("HINT_POINT", 0);
                                            }

                                            quizFragment.setArguments(bundle);
                                            fragmentTransition(quizFragment);
                                        }
                                    });

                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (overviewDialog.isShowing()) {
                                                overviewDialog.dismiss();
                                            }

                                        }
                                    });

                                    overviewDialog.show();

                                    overviewDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            if (!isStarted) {
                                                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    });


                                } else {
                                    Toast.makeText(QuizActivity.this, "NO Question Found!", Toast.LENGTH_SHORT).show();
                                }
                            }



                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                        } else {
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Util.dissmisDialog(dialog);
                    Toast.makeText(QuizActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*dismiss the dialog*/
                Util.dissmisDialog(dialog);
                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    Toast.makeText(QuizActivity.this, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*replace the current fragment with new fragment*/
    public void fragmentTransition(Fragment fragment) {
        this.currentFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.vp_quiz, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


}
