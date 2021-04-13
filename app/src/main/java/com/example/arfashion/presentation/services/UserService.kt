package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.register.UserActiveResponse
import com.example.arfashion.presentation.app.models.register.UserRegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("/users/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): Call<UserLoginResponse>

    @POST("/users/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String, @Field("password") password: String,
                 @Field("name") name: String): Call<UserRegisterResponse>

    @POST("/users/active")
    @FormUrlEncoded
    fun active(@Header("token") token: String, @Field("active_code") active_code: String): Call<UserActiveResponse>

    companion object {

        fun create(): UserService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(UserService::class.java)
        }
    }
}