package com.quizest.quizestapp.NetworkPackage;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitInterface {


    /*those are the interface for networks calls of this project*/

    @FormUrlEncoded
    @POST("registration")
    Call<String> doRegistration(@Field("name") String name, @Field("phone") String phone, @Field("email") String email, @Field("password") String password, @Field("password_confirmation") String confirmPassword);


    @FormUrlEncoded
    @POST("login")
    Call<String> doLogin(@Field("email") String email, @Field("password") String password);


    @GET("category")
    Call<String> getCategoryList(@Header("Authorization") String token, @Header("Accept") String type);


    @GET
    Call<String> getQuizList(@Url String url, @Header("Authorization") String token);

    @GET
    Call<String> getSubCategoryList(@Url String url, @Header("Authorization") String token);

    @FormUrlEncoded
    @POST
    Call<String> submitAnswer(@Url String url, @Header("Authorization") String token, @Field("answer") int answer);


    @GET
    Call<String> getLeaderboardList(@Url String url, @Header("Authorization") String token);

    @GET("profile")
    Call<String> getProfileData(@Header("Authorization") String token);


    @GET("user-setting")
    Call<String> getUserSetting(@Header("Authorization") String token);

    @GET("payment-methods")
    Call<String> getPaymentMetodList(@Header("Authorization") String token);

    @GET("available-coin")
    Call<String> getAvaiablePayment(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("buy-coin")
    Call<String> buyCoin(
            @Header("Authorization") String token,
            @Field("coin_id") Integer coinId,
            @Field("payment_id") Integer paymentId,
            @Field("amount") Integer amount,
            @Field("payment_method_nonce") String paymentNonce
    );
    @Headers({"Accept: application/json"})
    @GET("coin-setting")
    Call<String> getClientSetting(@Header("Authorization") String token);

    @GET("buy-coin-history")
    Call<String> getPurchaseHistory(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("save-user-setting")
    Call<String> postUserLang(@Header("Authorization") String token, @Field("language") String lang);


    @Multipart
    @POST("update-profile")
    Call<String> updateProfile(@Header("Authorization") String token
            , @Part("name") RequestBody name, @Part("country") RequestBody country, @Part("phone") RequestBody phone, @Part MultipartBody.Part image
    );

    @GET
    Call<String> addUserToFreebase(@Url String url, @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("deduct-coin")
    Call<String> expandCoin(@Header("Authorization") String token, @Field("coin") int coin);

    @FormUrlEncoded
    @POST("earn-coin")
    Call<String> earnCoin(@Header("Authorization") String token, @Field("coin") int coin);

    @FormUrlEncoded
    @POST("category-unlock")
    Call<String> unlockCategory(@Header("Authorization") String token, @Field("category_id") int id);

    @FormUrlEncoded
    @POST("send-reset-password-code")
    Call<String> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset-password")
    Call<String> resetPassowrd(@Field("access_code") String access_code, @Field("password") String password, @Field("password_confirmation") String confirmPassword);

    @FormUrlEncoded
    @POST("login-with-google")
    Call<String> logInWithGoogle(@Field("name") String name, @Field("email") String email);


}
