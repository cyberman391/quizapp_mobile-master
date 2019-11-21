package com.quizest.quizestapp.FragmentPackage.AuthFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.quizest.quizestapp.ActivityPackage.AuthActivity;
import com.quizest.quizestapp.ActivityPackage.MainActivity;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.ModelPackage.UserLogin;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;
import com.quizest.quizestapp.UtilPackge.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LogInFragment extends Fragment {


    /*global field instances*/
    EditText edtEmail, edtPassword;
    TextView tvLogInChangePassword, tvLogInSignUp;
    Button btnSignIn;
    Activity activity;
    GoogleSignInClient googleSignInClient;
    SignInButton signInButton;
    CardView fbLoginButton;
    private int RC_SIGN_IN = 1;
    CallbackManager callbackManager;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() != null && isAdded()) {
            activity = getActivity();
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getActivity() != null && isAdded()) {
            activity = getActivity();
        }

        /*view type casting*/
        initViews();

        /*sign up button click*/
        tvLogInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((AuthActivity) Objects
                            .requireNonNull(getActivity()))
                            .fragmentTransition(new RegisterFragment());
                }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


      //  fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null) {
                    callbackManager = CallbackManager.Factory.create();
                    loginWithFacebook(callbackManager);
                }

            }
        });




        /*change password button click*/
        tvLogInChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((AuthActivity) Objects.requireNonNull(getActivity()))
                            .fragmentTransition(new ForgotPasswordFragment());
                }
            }
        });

//        action for the sign in button click
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isInternetAvaiable(activity)) {
                    if (Validator.validateInputField(new EditText[]{edtEmail, edtPassword},
                            activity)) {
                        doLogIn(edtEmail.getText().toString().trim(),
                                edtPassword.getText().toString().trim());
                    }
                } else {
                    Toast.makeText(activity,
                            R.string.no_internet,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loginWithFacebook(CallbackManager callbackManager){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                    if (jsonObject != null) {
                                        if (jsonObject.has("id")) {
                                            try {
                                                String firstName = "", lastName = "", email = "", imageUrl = "", facebookID;
                                                facebookID = jsonObject.getString("id");
                                                if (jsonObject.has("first_name")) {
                                                    firstName = jsonObject.getString("first_name");
                                                }
                                                if (jsonObject.has("middle_name")) {
                                                    firstName = firstName + jsonObject.getString("middle_name");
                                                }
                                                if (jsonObject.has("last_name")) {
                                                    lastName = jsonObject.getString("last_name");
                                                }
                                                if (jsonObject.has("email")) {
                                                    email = jsonObject.getString("email");
                                                }
                                                if (jsonObject.has("picture")) {
                                                    JSONObject pictureObject = jsonObject.getJSONObject("picture");
                                                    JSONObject pictureDataObject = pictureObject.getJSONObject("data");
                                                    if (pictureDataObject.has("url")) {
                                                        imageUrl = pictureDataObject.getString("url");
                                                    }
                                                }

                                                socialLogin(email, firstName + " " + lastName);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getActivity(), "Something went wrong!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Something went wrong!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, middle_name, last_name, link, email, picture.height(720)");
                    request.setParameters(parameters);
                    request.executeAsync();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "Login canceled!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList(getActivity().getResources().getStringArray(R.array.facebook_login_permissions)));
    }

    private void socialLogin(String email, String name) {
        final Storage storage =
                new Storage(getActivity());
        final ProgressDialog dialog = Util.showDialog(getActivity());
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> stringCall = retrofitInterface.logInWithGoogle(name, email);
        stringCall.enqueue(new Callback<String>() {
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
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            UserLogin userLogIn = gson.fromJson(response.body(), UserLogin.class);
                            /*save the user data to local storage*/
                            storage.SaveAccessToken(userLogIn.getData().getAccessToken());
                            storage.SaveAccessType(userLogIn.getData().getAccessType());
                            storage.SaveLogInSate(true);
                            storage.saveUserName(userLogIn.getData().getUserInfo().getName());
                            storage.saveUserId(userLogIn.getData().getUserInfo().getId());
                            storage.saveUserTotalCoin(userLogIn.getData().getTotalCoin());
                            storage.saveUserTotalPoint(Integer.parseInt(userLogIn.getData().getTotalPoint()));
                            storage.saveUserAdmobPoint(userLogIn.getData().getAdmobCoin());
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
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
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            socialLogin(account.getEmail(), account.getDisplayName());

        } catch (ApiException e) {
            if (e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                Toast.makeText(activity, "Canceled by user!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Sign in failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*log in api call*/
    private void doLogIn(String email, String password) {
        final Storage storage = new Storage(activity);
        final ProgressDialog dialog = Util.showDialog(activity);
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> doLoginCall = retrofitInterface.doLogin(email, password);
        doLoginCall.enqueue(new Callback<String>() {
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
                            /*serialize the String response  */
                            Gson gson = new Gson();
                            UserLogin userLogIn = gson.fromJson(response.body(), UserLogin.class);
                            /*save the user data to local storage*/
                            storage.SaveAccessToken(userLogIn.getData().getAccessToken());
                            storage.SaveAccessType(userLogIn.getData().getAccessType());
                            storage.SaveLogInSate(true);
                            storage.saveUserName(userLogIn.getData().getUserInfo().getName());
                            storage.saveUserId(userLogIn.getData().getUserInfo().getId());
                            storage.saveUserTotalCoin(userLogIn.getData().getTotalCoin());
                            storage.saveUserTotalPoint(Integer.parseInt(userLogIn.getData().getTotalPoint()));
                            storage.saveUserAdmobPoint(userLogIn.getData().getAdmobCoin());
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
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
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(activity, R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*view type casting*/
    private void initViews() {
        View view = getView();
        if (view != null) {
            signInButton = view.findViewById(R.id.sign_in_button);
            fbLoginButton = view.findViewById(R.id.fb_login_button);
            edtEmail = view.findViewById(R.id.edt_login_email);
            edtEmail.setTag("Email");
            edtPassword = view.findViewById(R.id.edt_login_password);
            edtPassword.setTag("Password");
            tvLogInSignUp = view.findViewById(R.id.tv_login_signup);
            btnSignIn = view.findViewById(R.id.btn_sign_in);
            tvLogInChangePassword = view.findViewById(R.id.tv_login_change_password);
        }
    }

}
