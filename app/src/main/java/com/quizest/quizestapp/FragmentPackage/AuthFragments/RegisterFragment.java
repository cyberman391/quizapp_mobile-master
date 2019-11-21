package com.quizest.quizestapp.FragmentPackage.AuthFragments;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quizest.quizestapp.ActivityPackage.AuthActivity;
import com.quizest.quizestapp.ActivityPackage.MainActivity;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.UserRegistration;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;
import com.quizest.quizestapp.UtilPackge.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RegisterFragment extends Fragment {

    /*global field instances*/
    Activity activity;
    TextView tv_RegisterSignIn;
    Button btn_reg_signup;
    EditText edtUserName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getActivity() != null && isAdded()) {
            activity = getActivity();
        }
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        view type casting
        initViews();
        if (getActivity() != null && isAdded()) {
            activity = getActivity();
        }
//        sign in button calls
        tv_RegisterSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && isAdded())
                    ((AuthActivity) getActivity()).fragmentTransition(new LogInFragment());
            }
        });
//        sign up button call
        btn_reg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isInternetAvaiable(activity)) {
                    boolean isValid = Validator.validateInputField(new EditText[]{edtUserName,
                                    edtEmail,
                                    edtPhone,
                                    edtPassword,
                                    edtConfirmPassword},
                            activity);
                    if (isValid) {
                        if (edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {
                            /*if everything is ok then call the do registration */
                            doRegistration(edtUserName.getText().toString(), edtEmail.getText().toString().trim(), edtPhone.getText().toString()
                                    , edtPassword.getText().toString(), edtConfirmPassword.getText().toString()
                            );
                        } else {
                            Toast.makeText(activity, R.string.password_not_match,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(activity, R.string.no_internet,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*type casting of views*/
    private void initViews() {
        View view = getView();
        if (view != null) {
            edtConfirmPassword = view.findViewById(R.id.edt_reg_confim_password);
            edtConfirmPassword.setTag("Confirm Password");
            edtEmail = view.findViewById(R.id.edt_reg_email);
            edtEmail.setTag("Email");
            edtPhone = view.findViewById(R.id.edt_reg_phone);
            edtPhone.setTag("Phone");
            edtPassword = view.findViewById(R.id.edt_reg_password);
            edtPassword.setTag("Password");
            tv_RegisterSignIn = view.findViewById(R.id.tv_register_sign_in);
            btn_reg_signup = view.findViewById(R.id.btn_reg_signup);
            edtUserName = view.findViewById(R.id.edt_reg_name);
            edtUserName.setTag("Name");
        }
    }

    /*send the firebase token to the server*/
    private void addUserToFireBase() {
        Storage storage = new Storage(activity);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> tokenCall = retrofitInterface.addUserToFreebase(RetrofitClient.FIREBASE_ENDPOINT + "/" + storage.getUserId() + "/" + storage.getFirebaseToken(), storage.getAccessToken());
        tokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(activity, "Firebase success!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "Failed to add you in Firebase!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /*registration call */
    private void doRegistration(String name, String email, String phone, String password, String confirmPassword) {
        final ProgressDialog dialog = Util.showDialog(activity);
        final Storage storage = new Storage(activity);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> doRegisterCall = retrofitInterface.doRegistration(name, phone, email, password, confirmPassword);
        doRegisterCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*globally handle error*/
                ErrorHandler.getInstance().handleError(response.code(), activity, dialog);
                if (response.body() != null && response.isSuccessful()) {
                    /*success true*/
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            UserRegistration userRegistration = gson.fromJson(response.body(), UserRegistration.class);
                            /*save theuser data to local storage*/
                            storage.saveUserId(userRegistration.getData().getUserInfo().getId());
                            storage.saveUserName(userRegistration.getData().getUserInfo().getName());
                            storage.SaveAccessToken(userRegistration.getData().getAccessToken());
                            storage.SaveAccessType(userRegistration.getData().getAccessType());
                            storage.saveUserAdmobPoint(userRegistration.getData().getAdmobCoin());
                            storage.SaveLogInSate(true);
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*show success message to user*/
                            Toast.makeText(activity, "Registration success!", Toast.LENGTH_SHORT).show();

                            addUserToFireBase();

                            /*take the user to the dashboard activity*/
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            activity.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                            activity.finish();
                        } else {
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*get all the error messages and show to the user*/
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Toast.makeText(activity, jsonArray.getString(i), Toast.LENGTH_SHORT).show();
                            }
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

            /*handle error when you have any error*/
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*dismiss the dialog*/
                Util.dissmisDialog(dialog);
                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


}
