package com.quizest.quizestapp.FragmentPackage.AuthFragments;


import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.quizest.quizestapp.UtilPackge.Validator;

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
public class ForgotPasswordFragment extends Fragment {

    EditText etEmail;
    Button btnDone;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().trim().length() != 0) {
                    if (etEmail.getText().toString().matches(Validator.EMAIL_VERIFICATION)) {
                        sendResetCode(etEmail.getText().toString().trim());
                    } else {
                        Toast.makeText(getActivity(), "Invalid Email!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendResetCode(String email) {
        final ProgressDialog dialog = Util.showDialog(getActivity());
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<String> emailCall = retrofitInterface.forgotPassword(email);
        emailCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                ErrorHandler.getInstance().handleError(response.code(), getActivity(), dialog);

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean("success")) {
                            if (getActivity() != null)
                                ((AuthActivity) getActivity()).fragmentTransition(new ResetPasswordFragment());
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Util.dissmisDialog(dialog);
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        View view = getView();
        if (view != null) {
            etEmail = view.findViewById(R.id.et_email);
            btnDone = view.findViewById(R.id.btn_done);
        }
    }
}
