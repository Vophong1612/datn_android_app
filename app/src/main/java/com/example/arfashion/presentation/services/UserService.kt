package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {
    @POST("/users/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): Call<UserLoginResponse>

    companion object {

        fun create(): UserService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(UserService::class.java)
        }
    }
}