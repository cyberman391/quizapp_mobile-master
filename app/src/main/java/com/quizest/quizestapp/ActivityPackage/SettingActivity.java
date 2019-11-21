package com.quizest.quizestapp.ActivityPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.UserSetting;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

/*all global field instances are here*/

    Switch sound;
    UserSetting userSetting;
    String[] langSettingInArray;
    LinearLayout btnPrivacyPolicy, btnTermsOfCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        /**/
        final Storage storage = new Storage(this);

        /*view type casting*/
        sound = findViewById(R.id.sw_sound);
        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy);
        btnTermsOfCondition = findViewById(R.id.btn_terms_of_conditions);

        btnTermsOfCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                intent.putExtra("URL", "http://quiz.itech-softsolutions.com/terms-and-conditions");
                intent.putExtra("TITLE","Terms & Conditions");
             startActivity(intent);
            }
        });

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                intent.putExtra("URL", "http://quiz.itech-softsolutions.com/privacy-and-policy");
                intent.putExtra("TITLE","Privacy & Policy");
                startActivity(intent);
            }
        });

        /*check the state of sound and set value upon it the setting*/
        if (storage.getSoundState()) {
            sound.setChecked(true);
        } else {
            sound.setChecked(false);
        }

        /*change the state of the sound on user input*/
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    storage.saveSoundState(true);
                } else {
                    storage.saveSoundState(false);
                }
            }
        });


        /*get the language list from server*/
        getUserSetting();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    /*show the dialog of current language list*/
    public void onChangeLang(View view) {
        if (userSetting == null) {
            /*again call to get the language list*/
            getUserSetting();

            final AlertDialog.Builder showNoDataFound = new AlertDialog.Builder(SettingActivity.this);
            showNoDataFound.setMessage("No Language Found");
            showNoDataFound.setIcon(android.R.drawable.stat_sys_warning);
            showNoDataFound.show();
            showNoDataFound.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

        } else {
            //String[] list = new String[]{"Bangladesh", "India", "USA", "Portugal"};
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setItems(langSettingInArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    postUserLang(userSetting.getData().getLang().get(i).getKey());
                }
            });
            builder.setTitle("Select your Language");
            builder.show();
        }

    }


    /*when user press the back button take him to the main activity*/
    @Override
    public void onBackPressed() {
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
    }


    /*update the language of the app by taking input of language key*/
    private void postUserLang(String langKey) {
        Storage storage = new Storage(SettingActivity.this);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> langCall = retrofitInterface.postUserLang(storage.getAccessToken(), langKey);
        langCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), SettingActivity.this, null);

                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*dismiss the dialog*/
                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            /*dismiss the dialog*/
                            /*get all the error messages and show to the user*/
                            JSONArray message = jsonObject.getJSONArray("message");
                            Toast.makeText(SettingActivity.this, message.getString(0), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    Toast.makeText(SettingActivity.this, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*the language list of setting*/
    private void getUserSetting() {
        Storage storage = new Storage(this);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> userSettingCall = retrofitInterface.getUserSetting(storage.getAccessToken());
        userSettingCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), SettingActivity.this, null);

                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {


                            /*serialize the String response  */
                            Gson gson = new Gson();
                            userSetting = gson.fromJson(response.body(), UserSetting.class);

                            langSettingInArray = new String[userSetting.getData().getLang().size()];

                            for (int i = 0; i < userSetting.getData().getLang().size(); i++) {
                                langSettingInArray[i] = userSetting.getData().getLang().get(i).getValue();
                            }

                        } else {
                            /*dismiss the dialog*/

                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    Toast.makeText(SettingActivity.this, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
