package com.quizest.quizestapp.NetworkPackage;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quizest.quizestapp.ActivityPackage.AuthActivity;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.UtilPackge.Util;

public class ErrorHandler extends Application {
    static ErrorHandler mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*make the error handler class singleton*/
    public static synchronized ErrorHandler getInstance() {
        if (mInstance == null) {
            mInstance = new ErrorHandler();
        }
        return mInstance;
    }


    /*handle error globally by checking the http code*/
    public void handleError(int code, Activity activity, ProgressDialog dialog) {
        switch (code) {
            case 500:
                if(activity != null)
                Toast.makeText(activity, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                Util.dissmisDialog(dialog);
                break;

            case 400:
                if(activity != null)
                Toast.makeText(activity, "Invalid Request!", Toast.LENGTH_SHORT).show();
                Util.dissmisDialog(dialog);
                break;

            case 429:
                if(activity != null)
                Toast.makeText(activity, "Too Many Request, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                Util.dissmisDialog(dialog);
                break;

            case 401:
                Toast.makeText(activity, "Session Expired!", Toast.LENGTH_SHORT).show();
                if (dialog != null) {
                    Util.dissmisDialog(dialog);
                }
                goToLogInActivity(activity);
                break;
        }
    }


    /*take user to Auth Activity*/
    private void goToLogInActivity(Activity activity) {
        /*make the current user logged out*/
        Storage storage = new Storage(activity);
        storage.SaveLogInSate(false);
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

}
