package com.quizest.quizestapp.FragmentPackage.DashboardFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.AdapterPackage.LeaderboardRecyclerAdapter;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.LeaderBoard;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LeaderBoardFragment extends Fragment {


    /*all global field instances are here*/
    private static final int ALL = 1;
    private static final int DAILY = 2;
    private static final int WEEKLY = 3;

    Activity activity;
    RecyclerView leadboardRecyclerView;
    LinearLayout layoutAll, layoutDaily, layoutWeekly;
    TextView tvAll, tv_all_user , tvDaily, tvDailyTop, tvWeekly, tvWeeklyTop;
    LeaderboardRecyclerAdapter leaderboardRecyclerAdapter;
    TextView tvMessage;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        /*if there is internet connection then we will call the server for data else we will use offline caches*/
        if (Util.isInternetAvaiable(activity)) {
            getLeaderBoardData(ALL);
        } else {
            Storage storage = new Storage(activity);
            /*serialize the String response  */
            if (storage.getLeaderBoardResponse() != null) {
                Gson gson = new Gson();
                LeaderBoard leaderBoard = gson.fromJson(storage.getLeaderBoardResponse(), LeaderBoard.class);
                if (leaderBoard.getLeaderList() != null)
                    leaderboardRecyclerAdapter = new LeaderboardRecyclerAdapter(leaderBoard.getLeaderList(), activity);
                leadboardRecyclerView.setAdapter(leaderboardRecyclerAdapter);
            }

        }
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() != null) {

            activity = getActivity();

        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leader_board, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        if (getActivity() != null) {
            activity = getActivity();
        }

        /*tab section */
        layoutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLeaderBoardData(ALL);

                layoutAll.setBackgroundResource(R.drawable.leader_board_tab);
                layoutDaily.setBackgroundResource(R.drawable.leader_borad_tab_bg_white);
                layoutWeekly.setBackgroundResource(R.drawable.leader_borad_tab_bg_white);

                /*change the color of the selected item text*/
                tvAll.setTextColor(activity.getResources().getColor(R.color.color_white));
                tv_all_user.setTextColor(activity.getResources().getColor(R.color.color_white));

                /*change other text color*/

                tvDaily.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvDailyTop.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvWeekly.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvWeeklyTop.setTextColor(activity.getResources().getColor(R.color.color_text));

            }
        });

        /*weekly top user section*/
        layoutWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLeaderBoardData(WEEKLY);

                layoutAll.setBackgroundResource(R.drawable.leader_borad_tab_bg_white);
                layoutDaily.setBackgroundResource(R.drawable.leader_borad_tab_bg_white);
                layoutWeekly.setBackgroundResource(R.drawable.leader_board_tab);


                /*change the color of the selected item text*/
                tvWeekly.setTextColor(activity.getResources().getColor(R.color.color_white));
                tvWeeklyTop.setTextColor(activity.getResources().getColor(R.color.color_white));


                /*change other text color*/

                tvDaily.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvDailyTop.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvAll.setTextColor(activity.getResources().getColor(R.color.color_text));
                tv_all_user.setTextColor(activity.getResources().getColor(R.color.color_text));
            }
        });


        /*daily top user section*/
        layoutDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLeaderBoardData(DAILY);

                layoutAll.setBackgroundResource(R.drawable.leader_borad_tab_bg_white);
                layoutDaily.setBackgroundResource(R.drawable.leader_board_tab);
                layoutWeekly.setBackgroundResource(R.drawable.leader_borad_tab_bg_white);



                /*change the color of the selected item text*/
                tvDaily.setTextColor(activity.getResources().getColor(R.color.color_white));
                tvDailyTop.setTextColor(activity.getResources().getColor(R.color.color_white));


                /*change other text color*/

                tvWeekly.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvWeeklyTop.setTextColor(activity.getResources().getColor(R.color.color_text));
                tvAll.setTextColor(activity.getResources().getColor(R.color.color_text));
                tv_all_user.setTextColor(activity.getResources().getColor(R.color.color_text));
            }
        });


        /*set options to recycler view*/
        leadboardRecyclerView.setHasFixedSize(true);
        leadboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*if there is internet connection then we will call the server for data else we will use offline caches*/
        if (Util.isInternetAvaiable(activity)) {
            getLeaderBoardData(ALL);
        } else {
            Storage storage = new Storage(activity);
            /*serialize the String response  */
            if (storage.getLeaderBoardResponse() != null) {
                Gson gson = new Gson();
                LeaderBoard leaderBoard = gson.fromJson(storage.getLeaderBoardResponse(), LeaderBoard.class);
                if (leaderBoard.getLeaderList() != null)
                    leaderboardRecyclerAdapter = new LeaderboardRecyclerAdapter(leaderBoard.getLeaderList(), activity);
                leadboardRecyclerView.setAdapter(leaderboardRecyclerAdapter);
            }

        }


    }

    private void initViews() {
        View view = getView();
        if (view != null) {
            tv_all_user= view.findViewById(R.id.tv_all_user);
            tvMessage = view.findViewById(R.id.tv_message);
            tvAll = view.findViewById(R.id.tv_all);
            tvDaily = view.findViewById(R.id.tv_daily);
            tvDailyTop = view.findViewById(R.id.tv_daily_top);
            tvWeekly = view.findViewById(R.id.tv_weekly);
            tvWeeklyTop = view.findViewById(R.id.tv_weekly_top);
            layoutAll = view.findViewById(R.id.layout_all);
            layoutDaily = view.findViewById(R.id.layout_daily);
            layoutWeekly = view.findViewById(R.id.layout_weekly);
            leadboardRecyclerView = view.findViewById(R.id.rv_leaderboard);
        }

    }

    private void getLeaderBoardData(int type) {
        final ProgressDialog dialog = Util.showDialog(activity);
        final Storage storage = new Storage(activity);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> leaderCall = retrofitInterface.getLeaderboardList(RetrofitClient.LEADERBOARD_URL + type, storage.getAccessToken());
        leaderCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*handle error globally */
                ErrorHandler.getInstance().handleError(response.code(), activity, dialog);
                if (response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*save leaderboard reponse to local storage*/
                            storage.saveLeaderBoardResponse(response.body());
                            tvMessage.setVisibility(View.GONE);
                            leadboardRecyclerView.setVisibility(View.VISIBLE);
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            LeaderBoard leaderBoard = gson.fromJson(response.body(), LeaderBoard.class);
                            leaderboardRecyclerAdapter = new LeaderboardRecyclerAdapter(leaderBoard.getLeaderList(), activity);
                            leadboardRecyclerView.setAdapter(leaderboardRecyclerAdapter);
                            Util.dissmisDialog(dialog);

                        } else {

                            tvMessage.setVisibility(View.VISIBLE);
                            leadboardRecyclerView.setVisibility(View.GONE);
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
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
