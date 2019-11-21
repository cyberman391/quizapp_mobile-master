package com.quizest.quizestapp.ActivityPackage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.quizest.quizestapp.FragmentPackage.DashboardFragments.EarnCoinFragment;
import com.quizest.quizestapp.FragmentPackage.DashboardFragments.LeaderBoardFragment;
import com.quizest.quizestapp.FragmentPackage.DashboardFragments.HomeFragment;
import com.quizest.quizestapp.FragmentPackage.DashboardFragments.ViewProfileFragment;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;

public class MainActivity extends AppCompatActivity  {

    /*all global field instances are here*/

    Storage storage;
    private int count = 0;
    BottomNavigationView bv_BottomBar;
    ImageButton btn_logout, btn_setting;
    Fragment currentFragment;
    LinearLayout topPanel;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*add the layout file to the activity*/
        setContentView(R.layout.activity_main);


       // KotlinUtil.Companion.getAndroidHashKey(getApplicationContext());

        storage = new Storage(this);

        /*all permision need for this app*/
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };


        /*check if the there is permission that is needed*/

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        //*make the statusbar transparent if version is above kitkat*//*

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

     /*   relativeLayout = findViewById(R.id.test);
        relativeLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
*/

        /*type casting views */
        initviews();

        fragmentTransition(new HomeFragment());

        /*removing shifting animation from bottom view */
        Util.removeShiftMode(bv_BottomBar);



        /*log out the user*/
        final Storage storage = new Storage(this);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*change the state of the logged in to log out for the current user*/
                storage.SaveLogInSate(false);
                LoginManager.getInstance().logOut();
                /*take the current user to the log in page*/
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                finish();
            }
        });

        /*change the fragment on bottom bar item click*/
        bv_BottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        /*replace with home fragment*/
                        RestMode();
                        if (!(currentFragment instanceof HomeFragment))
                            currentFragment = new HomeFragment();
                        break;
                    case R.id.navigation_earncoin:
                        RestMode();
                        /*replace with Earn coin fragment*/
                        currentFragment
                                = new EarnCoinFragment();
                        break;
                    case R.id.navigation_profile:
                        profileMode();
                        /*replace with viewprofile fragment*/
                        currentFragment = new ViewProfileFragment();
                        break;
                    case R.id.navigation_leaderboard:
                        RestMode();
                        /*replace with home Leaderboard*/
                        if (!(currentFragment instanceof LeaderBoardFragment))
                            currentFragment = new LeaderBoardFragment();
                        break;
                }

                /*do the main fragment transition*/
                fragmentTransition(currentFragment);
                return true;
            }
        });


    }

    /*check if the app have all permissions*/
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    /*if user is in profile fragment make the status bar color blue*/
    private void profileMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_holo_blue));// SDK21
        }
        topPanel.setVisibility(View.GONE);
    }

    private void RestMode() {

        //*make the statusbar transparent if version is above kitkat*//*

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
        topPanel.setVisibility(View.VISIBLE);
    }


    /*replace the current fragment with new fragment*/
    public void fragmentTransition(Fragment fragment) {
        this.currentFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.commit();
    }


    /*type casting all the view*/
    private void initviews() {
        topPanel = findViewById(R.id.top_panel);
        bv_BottomBar = findViewById(R.id.bv_bottom_bar);
        btn_logout = findViewById(R.id.btn_logout);
        btn_setting = findViewById(R.id.btn_setting);
    }

    @Override
    public void onBackPressed() {
        if (!(currentFragment instanceof HomeFragment)) {
            bv_BottomBar.setSelectedItemId(R.id.navigation_home);
            fragmentTransition(new HomeFragment());

        } else {
            count++;
            if (count == 1) {
                Toast.makeText(this, "Press Again To Exit!", Toast.LENGTH_SHORT).show();
            } else if (count == 2) {
                super.onBackPressed();
            }
        }

    }

}
