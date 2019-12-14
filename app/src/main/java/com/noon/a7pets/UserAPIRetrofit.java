package com.noon.a7pets;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPIRetrofit {

    @FormUrlEncoded
    @POST("souq/insert_user_post.php")
    Call<User> createUser(
            @Field("name") String name,
            @Field("password") String password,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("image") String image
    );

    @GET("souq/get_user.php")
    Call<List<User>> loginUser(
            @Query("email") String email,
            @Query("password") String password
    );

}