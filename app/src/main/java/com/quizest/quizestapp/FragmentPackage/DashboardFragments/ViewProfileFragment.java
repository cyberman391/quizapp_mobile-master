package com.quizest.quizestapp.FragmentPackage.DashboardFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.quizest.quizestapp.ActivityPackage.MainActivity;
import com.quizest.quizestapp.ActivityPackage.SettingActivity;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.ProfileSection;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.GlideApp;
import com.quizest.quizestapp.UtilPackge.Util;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment {


    TextView tv_participated_question;
    Activity activity;
    ImageButton btnEditProfile, button_setting_profile;
    LineChart graphQuizReport;
    ImageView img_profile;
    ArrayList<Entry> lineChatEntryData;
    TextView userName, userEmail, userPoints, userRanking;
    String[] lebels;

    public ViewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() != null && isAdded()) {
            activity = getActivity();
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();


        lineChatEntryData = new ArrayList<>();

        getProfileData();


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) activity).fragmentTransition(new EditProfileFragment());
            }
        });

        button_setting_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SettingActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                activity.finish();
            }
        });


    }

    private void chartBuilder(final String[] lebels, final ArrayList<Entry> lineChatEntryData) {


        LineDataSet dataSet = new LineDataSet(lineChatEntryData, ""); // add entries to dataset
        dataSet.setFillDrawable(getResources().getDrawable(R.drawable.gradient_graph));
        dataSet.setDrawCircleHole(false);

        dataSet.setDrawCircles(false);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCubic(true);


        LineData lineData = new LineData(lebels, dataSet);
        graphQuizReport.setData(lineData);
        graphQuizReport.invalidate();


        graphQuizReport.getLegend().setEnabled(false);
        graphQuizReport.getAxisRight().setEnabled(false);
        graphQuizReport.getAxisLeft().setDrawGridLines(false);
        graphQuizReport.getXAxis().setDrawGridLines(false);



        XAxis xAxis = graphQuizReport.getXAxis();
        xAxis.setXOffset(0.2f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


    }


    private void initViews() {
        View view = getView();
        if (view != null) {
            tv_participated_question = view.findViewById(R.id.tv_participated_question);
            button_setting_profile = view.findViewById(R.id.button_setting_profile);
            userEmail = view.findViewById(R.id.tv_user_email);
            userName = view.findViewById(R.id.tv_profile_username);
            userPoints = view.findViewById(R.id.tv_user_points);
            userRanking = view.findViewById(R.id.tv_user_ranking);
            img_profile = view.findViewById(R.id.img_profile);
            btnEditProfile = view.findViewById(R.id.btn_edit_profile);
            graphQuizReport = view.findViewById(R.id.graph_quiz_report);
        }
    }

    private void getProfileData() {
        final ProgressDialog dialog = Util.showDialog(getActivity());
        final Storage storage = new Storage(getActivity());
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        final Call<String> profileCall = retrofitInterface.getProfileData(storage.getAccessToken());
        profileCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), getActivity(), dialog);
                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            ProfileSection profileSection = gson.fromJson(response.body(), ProfileSection.class);
                            storage.saveUserName(profileSection.getData().getUser().getName());
                            /*simple data binding for profile section*/
                            userEmail.setText(profileSection.getData().getUser().getEmail());
                            userName.setText(profileSection.getData().getUser().getName());
                            userPoints.setText(profileSection.getData().getUser().getPoints());
                            userRanking.setText(String.valueOf(profileSection.getData().getUser().getRanking()));
                            tv_participated_question.setText(String.valueOf(profileSection.getData().getUser().getParticipated_question()));
                            GlideApp.with(activity).load(profileSection.getData().getUser().getPhoto()).placeholder(R.drawable.avater).into(img_profile);

                            lebels = new String[profileSection.getDailyScore().size()];

                            /*make the chart*/
                            for (int i = 0; i < profileSection.getDailyScore().size(); i++) {
                               // Log.e("MKDATA", String.valueOf(Math.round((float) profileSection.getDailyScore().get(i).getScorePercentage())));
                                lineChatEntryData.add(new Entry((float) profileSection.getDailyScore().get(i).getScorePercentage(), i));
                                //  lebels.add(Util.getFormattedDate(profileSection.getDailyScore().get(i).getDate()));
                                lebels[i] = Util.getFormattedDate(profileSection.getDailyScore().get(i).getDate());

                            }


                         if(lebels.length != 0 && lineChatEntryData.size() != 0){
                             chartBuilder(lebels, lineChatEntryData);
                         }

                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);

                        } else {
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (getActivity() != null)
                        /*dismiss the dialog*/
                        Util.dissmisDialog(dialog);
                    Toast.makeText(activity, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*dismiss the dialog*/
                Util.dissmisDialog(dialog);
                /*handle network error and notify the user*/
                if (t instanceof IOException) {
                    if (activity != null && isAdded())
                        Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
