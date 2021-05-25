package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.login.UserLoginFacebookResponse
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.loginstatus_code.UserLoginGoogleResponse
import com.example.arfashion.presentation.app.models.register.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {

    @POST("/users/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): Call<UserLoginResponse>

    @POST("/users/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String, @Field("password") password: String,
                 @Field("name") name: String): Call<UserRegisterResponse>

    @POST("/users/activate")
    @FormUrlEncoded
    fun active(@Field("active_code") active_code: String, @Field("email") email: String,): Call<UserActiveResponse>

    @POST("/users/resendActiveEmail")
    @FormUrlEncoded
    fun resendEmail(@Field("email") email: String): Call<UserResendActiveResponse>

    @POST("/users/googleLogin")
    @FormUrlEncoded
    fun loginGoogle(@Field("tokenId") tokenId: String): Call<UserLoginGoogleResponse>

    @POST("/users/facebookLogin")
    @FormUrlEncoded
    fun loginFacebook(@Field("accessToken") accessToken: String, @Field("userId") userId: String): Call<UserLoginFacebookResponse>

    @POST("/users/loginPhone")
    @FormUrlEncoded
    fun loginPhone(@Field("phone") phone: String, @Field("password") password: String): Call<UserLoginResponse>

    @POST("/users/registerPhone")
    @FormUrlEncoded
    fun registerPhone(@Field("phone") phone: String, @Field("password") password: String,
                      @Field("name") name: String): Call<UserRegisterResponse>

    @POST("/users/checkPhoneLogin")
    @FormUrlEncoded
    fun checkPhoneLogin(@Field("phone") phone: String): Call<UserCheckPhoneLoginResponse>

    @POST("/users/resendActivePhone")
    @FormUrlEncoded
    fun resendPhone(@Field("phone") phone: String): Call<UserResendActiveResponse>

    @POST("/users/verifyActivatePhone")
    @FormUrlEncoded
    fun verifyActivatePhone(@Field("phone") phone: String, @Field("active_code") active_code: String): Call<UserVerifyActivatePhoneResponse>

    companion object {

        fun create(): UserService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(UserService::class.java)
        }
    }
}