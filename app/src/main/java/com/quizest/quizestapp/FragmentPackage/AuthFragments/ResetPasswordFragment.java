package com.quizest.quizestapp.FragmentPackage.AuthFragments;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quizest.quizestapp.ActivityPackage.AuthActivity;
import com.quizest.quizestapp.NetworkPackage.ErrorHandler;
import com.quizest.quizestapp.NetworkPackage.RetrofitClient;
import com.quizest.quizestapp.NetworkPackage.RetrofitInterface;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ResetPasswordFragment extends Fragment {

    EditText etVerifyCode, etPassword, etConfirmPassword;
    Button btnReset;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVerifyCode.getText().toString().length() != 0) {
                    if (etPassword.getText().length() >= 8) {
                        if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                            resetPassword(etVerifyCode.getText().toString().trim(), etPassword.getText().toString().trim(), etConfirmPassword.getText().toString().trim());
                        } else {
                            Toast.makeText(getActivity(), "Confirm Password doesn't match!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Password can't be less than 8 character!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Verify code can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetPassword(String accessCode, String password, String ConfirmPass) {
        final ProgressDialog dialog = Util.showDialog(getActivity());
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        final Call<String> resetPass = retrofitInterface.resetPassowrd(accessCode, password, ConfirmPass);
        resetPass.enqueue(new Callback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ErrorHandler.getInstance().handleError(response.code(), getActivity(), dialog);
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            ((AuthActivity) Objects.requireNonNull(getActivity())).fragmentTransition(new LogInFragment());
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }
                Util.dissmisDialog(dialog);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Util.dissmisDialog(dialog);
                    Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        View view = getView();
        if (view != null) {
            etVerifyCode = view.findViewById(R.id.edt_verify_code);
            etPassword = view.findViewById(R.id.edt_password);
            etConfirmPassword = view.findViewById(R.id.edt_confim_password);
            btnReset = view.findViewById(R.id.btn_reset_password);
        }
    }

}
