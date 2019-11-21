package com.quizest.quizestapp.ActivityPackage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.quizest.quizestapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    String url, title;
    WebView wvPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        wvPrivacyPolicy = findViewById(R.id.wv_privacy);
       // wvPrivacyPolicy.getSettings().setJavaScriptEnabled(true);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(getIntent()!=null){
            url = getIntent().getStringExtra("URL");
            title=  getIntent().getStringExtra("TITLE");
            wvPrivacyPolicy.loadUrl(url);
            getSupportActionBar().setTitle(title);
        }



        wvPrivacyPolicy.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });


    }


}
