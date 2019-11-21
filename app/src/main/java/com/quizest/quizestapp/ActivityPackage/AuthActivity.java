package com.quizest.quizestapp.ActivityPackage;

import android.graphics.Color;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.quizest.quizestapp.FragmentPackage.AuthFragments.ForgotPasswordFragment;
import com.quizest.quizestapp.FragmentPackage.AuthFragments.LogInFragment;
import com.quizest.quizestapp.FragmentPackage.AuthFragments.RegisterFragment;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.R;

public class AuthActivity extends AppCompatActivity {

    /*all global field instances here*/
    Fragment currentFragment;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*adding layout for the activity*/
        setContentView(R.layout.activity_auth);
        //*make the status transparent if version is above kitkat*//*

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

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(getApplicationContext());

        /*the first is opened for first time*/
        Storage storage = new Storage(this);
        storage.saveIsFirstTime(true);

        /*make log in fragment as default*/
        fragmentTransition(new LogInFragment());
    }


    /*do fragment replace with new fragment*/
    public void fragmentTransition(Fragment fragment) {
        this.currentFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fl_auth_container, fragment);
        fragmentTransaction.commit();
    }


    /*this function called when the user press back button*/
    @Override
    public void onBackPressed() {

        /*if the current fragment is register fragment or forgotpassword fragment then take the user to the log in fragment*/
        if (currentFragment instanceof RegisterFragment || currentFragment instanceof ForgotPasswordFragment) {

            this.fragmentTransition(new LogInFragment());

        } else if (currentFragment instanceof LogInFragment) {

            count++;

            if (count == 1) {

                Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();

            } else if (count == 2) {

                super.onBackPressed();
            }
        }


    }


}
