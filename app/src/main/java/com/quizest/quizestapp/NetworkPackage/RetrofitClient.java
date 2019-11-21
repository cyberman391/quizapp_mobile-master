package com.quizest.quizestapp.NetworkPackage;

import android.app.Activity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/*this is the retrofit client, we used retrofit as our networking library*/
public class RetrofitClient {

    /*those are constants urls we used in this project*/
     private static final String BASE_URL = "https://quiz.coderantik.id/api/";
    //private static final String BASE_URL = "https://quiz.itech-softsolutions.com/api/";
    public static final String CATEGORY_URL = BASE_URL + "category/";
    public static final String SUBCATEGORY_URL = BASE_URL + "sub-category/";
    public static final String LEADERBOARD_URL = BASE_URL + "leader-board/";
    public static final String SUBMIT_ANSWER_ULR = BASE_URL + "submit-answer/";
    public static final String FIREBASE_ENDPOINT = BASE_URL + "set-user-device-id";
    private static Retrofit retrofit = null;

    /*here we made a global */
    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).
                    addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
