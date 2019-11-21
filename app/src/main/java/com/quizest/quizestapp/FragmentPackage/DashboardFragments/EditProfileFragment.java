package com.quizest.quizestapp.FragmentPackage.DashboardFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.quizest.quizestapp.UtilPackge.ImageFilePath;
import com.quizest.quizestapp.UtilPackge.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    /*global field instances*/
    private File file;
    private int PICK_IMAGE = 1;
    CircleImageView profileImage;
    TextView tvRanking, tvPoint, tvName, tvEmail, tv_participated_question;
    ImageButton btn_setting_edit, btn_pick_image, btn_back_edit;
    EditText edtUserName;
    EditText CountryName, Phone;
    Button btnSave;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        btn_setting_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                if (getActivity() != null)
                    getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                getActivity().finish();
            }
        });

        btn_back_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && isAdded())
                    ((MainActivity) getActivity()).fragmentTransition(new ViewProfileFragment());
            }
        });

        btn_pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viwe) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select Image"), PICK_IMAGE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUserName.getText().toString().length() != 0) {
                    if (CountryName.getText().toString().length() != 0) {
                        if (Phone.getText().toString().length() != 0) {
                            if (file != null) {
                                updateProfile(edtUserName.getText().toString(), Phone.getText().toString(), CountryName.getText().toString(), Util.getCompressedFile(file, getActivity()));
                            } else {
                                Toast.makeText(getActivity(), "Select Image First", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Phone Can't be empty!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Country Name Can't be empty!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Name Can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getProfileData();
    }


    private void updateProfile(String name, String phone, String country, File file) {
        final ProgressDialog dialog = Util.showDialog(getActivity());
        Storage storage = new Storage(getActivity());
        RetrofitInterface retrofitInterface = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        RequestBody requestName = RequestBody.create(MultipartBody.FORM, name);
        RequestBody requestPhone = RequestBody.create(MultipartBody.FORM, phone);
        RequestBody requestCountry = RequestBody.create(MultipartBody.FORM, country);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo",
                file.getName(), fileBody);
        Call<String> updateCall = retrofitInterface.updateProfile(storage.getAccessToken(), requestName, requestCountry, requestPhone, imagePart);
        updateCall.enqueue(new Callback<String>() {
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

                            String sucessMessage = jsonObject.getString("message");

                            Toast.makeText(getActivity(), sucessMessage, Toast.LENGTH_SHORT).show();

                            Util.dissmisDialog(dialog);

                            if (getActivity() != null && isAdded())
                                ((MainActivity) getActivity()).fragmentTransition(new ViewProfileFragment());

                        } else {
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*get all the error messages and show to the user*/
                            JSONArray messageArray = jsonObject.getJSONArray("message");
                            Toast.makeText(getActivity(), messageArray.getString(0), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    /*dismiss the dialog*/
                    Util.dissmisDialog(dialog);
                    Toast.makeText(getActivity(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*dismiss the dialog*/
                Util.dissmisDialog(dialog);

                if (t instanceof IOException) {
                    Log.e("MKIO", t.getMessage());
                }
                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException) {

                    if (getActivity() != null && isAdded())
                        Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*get the returned image*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    file = new File(ImageFilePath.getPath(getActivity(), data.getData()));

                    if (getActivity() != null)
                        GlideApp.with(getActivity()).load(data.getData()).into(profileImage);

                }

            }
        }
    }

    private File getFileFromUri(Uri uri) {
        String filePath = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, filePathColumn, null, null, null);
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int columnIndex = 0;
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        }
        if (cursor != null) {

            filePath = cursor.getString(columnIndex);
        }
        if (cursor != null) {
            cursor.close();
        }

        File file = null;
        if (filePath != null) {
            file = new File(filePath);
        }

        return file;

    }


    private void initViews() {
        View view = getView();
        if (view != null) {
            tv_participated_question= view.findViewById(R.id.tv_participated_question);
            btn_back_edit = view.findViewById(R.id.btn_back_edit);
            btnSave = view.findViewById(R.id.btn_save);
            btn_pick_image = view.findViewById(R.id.btn_pick_image);
            profileImage = view.findViewById(R.id.img_edit_profile);
            edtUserName = view.findViewById(R.id.edt_username_edit);
            CountryName = view.findViewById(R.id.edt_country_edit);
            Phone = view.findViewById(R.id.edt_phone_edit);
            tvEmail = view.findViewById(R.id.tv_email_edit);
            tvName = view.findViewById(R.id.tv_name_edit);
            tvPoint = view.findViewById(R.id.tv_total_point_edit);
            tvRanking = view.findViewById(R.id.tv_rank_edit);
            btn_setting_edit = view.findViewById(R.id.btn_setting_edit);
        }
    }


    /*get the profile data from the api*/
    private void getProfileData() {
        final ProgressDialog dialog = Util.showDialog(getActivity());
        Storage storage = new Storage(getActivity());
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

                            /*simple data binding for profile section*/
                            tvEmail.setText(profileSection.getData().getUser().getEmail());
                            tvName.setText(profileSection.getData().getUser().getName());
                            tvPoint.setText(profileSection.getData().getUser().getPoints());
                            edtUserName.setText(profileSection.getData().getUser().getName());
                            if (profileSection.getData().getUser().getCountry() != null) {
                                CountryName.setText(profileSection.getData().getUser().getCountry());
                            }
                            tv_participated_question.setText(String.valueOf(profileSection.getData().getUser().getParticipated_question()));
                            Phone.setText(profileSection.getData().getUser().getPhone());
                            tvRanking.setText(String.valueOf(profileSection.getData().getUser().getRanking()));
                            if (getActivity() != null)
                                GlideApp.with(getActivity()).load(profileSection.getData().getUser().getPhoto()).placeholder(R.drawable.avater).into(profileImage);
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);

                        } else {
                            /*dismiss the dialog*/
                            Util.dissmisDialog(dialog);
                            /*get all the error messages and show to the user*/
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    /*dismiss the dialog*/
                    Util.dissmisDialog(dialog);
                    Toast.makeText(getActivity(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*dismiss the dialog*/
                Util.dissmisDialog(dialog);
                /*handle network error and notify the user*/
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    if (getActivity() != null && isAdded())
                        Toast.makeText(getActivity(), R.string.connection_timeout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
