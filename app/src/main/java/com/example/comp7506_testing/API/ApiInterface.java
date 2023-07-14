package com.example.comp7506_testing.API;

import com.example.comp7506_testing.Model.Group;
import com.example.comp7506_testing.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

// API setting
public interface ApiInterface {

    // field = key in body (e.g. {"username": "abc", "password": "123456"})

    @POST("/user/addUser")
    Call<User> postRegister(@Body User user);

    @POST("/user/login")
    Call<User> postLogin(@Body User user);

    @POST("/user/updateInfo")
    Call<User> postUpdateInfo(@Body User user, @Header("Cookie") String cookie);

    @POST("/group/addGroup")
    Call<User> postAddGroup(@Body Group group, @Header("Cookie") String cookie);

    @GET("/group/getGroup")
    Call<Group[]> getGroup();

}
