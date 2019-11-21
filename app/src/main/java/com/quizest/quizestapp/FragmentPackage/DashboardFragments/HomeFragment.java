package com.quizest.quizestapp.FragmentPackage.DashboardFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.AdapterPackage.CategoryRecyclerAdapter;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.CategoryList;
import com.quizest.quizestapp.ModelPackage.CategoryModel;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment {

    /*all global field instances are here*/
    TextView tvUserName;
    Activity activity;
    List<CategoryModel> categoryModels;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    RecyclerView categoryRecycler;

    public HomeFragment() {
        // Required empty public constructor
    }


    /*this is a fragment lifecycle method */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (getActivity() != null) {
            activity = getActivity();
        }

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        if (getActivity() != null) {
            activity = getActivity();
        }


        Storage storage = new Storage(activity);
        /*simple data binding for profile section*/
        if (storage.getUserName() != null)
            tvUserName.setText(String.format("Hello %s, \nWelcome back ", storage.getUserName()));


        /*adding options to category recycler*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setLayoutManager(gridLayoutManager);
        /*do api call here and set data to the recycler view */

        /*if there is internet call get data from server, otherwise load for local response*/
        if (Util.isInternetAvaiable(activity)) {
            getCategories();
        } else {
            Storage storage1 = new Storage(activity);
            if (storage1.getCategoryResponse() != null) {
                /*load data from offline*/
                /*serialize the String response  */
                Gson gson = new Gson();
                CategoryList categoryList = gson.fromJson(storage1.getCategoryResponse(), CategoryList.class);
                /*add the data to the recycler view*/
                if (categoryList.getCategoryList() != null)
                    categoryRecyclerAdapter = new CategoryRecyclerAdapter(categoryList.getCategoryList(), activity, categoryList.getUserAvailableCoin());
                categoryRecycler.setAdapter(categoryRecyclerAdapter);
            }


        }


    }


    /**/
    @Override
    public void onResume() {
        /*if there is internet call get data from server, otherwise load for local response*/
        if (Util.isInternetAvaiable(activity)) {
            getCategories();
        } else {
            Storage storage1 = new Storage(activity);
            if (storage1.getCategoryResponse() != null) {
                /*load data from offline*/
                /*serialize the String response  */
                Gson gson = new Gson();
                CategoryList categoryList = gson.fromJson(storage1.getCategoryResponse(), CategoryList.class);
                /*add the data to the recycler view*/
                if (categoryList.getCategoryList() != null)
                    categoryRecyclerAdapter = new CategoryRecyclerAdapter(categoryList.getCategoryList(), activity, categoryList.getUserAvailableCoin());
                categoryRecycler.setAdapter(categoryRecyclerAdapter);
            }


        }
        super.onResume();
    }

    private void getCategories() {
        final ProgressDialog dialog = Util.showDialog(activity);
        final Storage storage = new Storage(activity);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> categoryCall = retrofitInterface.getCategoryList(storage.getAccessToken(), Util.REQUEST_TYPE);
        categoryCall.enqueue(new Callback<String>() {
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
                            /*save the category response for offline uses*/
                            storage.saveCategoryResponse(response.body());
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            CategoryList categoryList = gson.fromJson(response.body(), CategoryList.class);
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*add the data to the recycler view*/
                            categoryRecyclerAdapter = new CategoryRecyclerAdapter(categoryList.getCategoryList(), activity, categoryList.getUserAvailableCoin());
                            categoryRecycler.setAdapter(categoryRecyclerAdapter);

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


    /*view type casting*/
    private void initViews() {
        View view = getView();
        if (view != null) {
            tvUserName = view.findViewById(R.id.tv_username);
            categoryRecycler = view.findViewById(R.id.recycler_categories);
        }
    }


}

