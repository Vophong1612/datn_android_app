package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.forgot_password.UserForgotPasswordResponse
import com.example.arfashion.presentation.app.models.login.UserLoginFacebookResponse
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.loginstatus_code.UserLoginGoogleResponse
import com.example.arfashion.presentation.app.models.profile.AvatarResponse
import com.example.arfashion.presentation.app.models.profile.ProfileResponse
import com.example.arfashion.presentation.app.models.register.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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

    @POST("/users/generateCodeForgot")
    @FormUrlEncoded
    fun generateCodeForgot(@Field("user_name") user_name: String, @Field("type") type: String): Call<UserForgotPasswordResponse>

    @POST("/users/forgot/validate")
    @FormUrlEncoded
    fun validateCodeForgot(@Field("user_name") user_name: String, @Field("code") code: String, @Field("type") type: String): Call<UserForgotPasswordResponse>

    @POST("/users/forgotPassword")
    @FormUrlEncoded
    fun changePasswordForgot(@Header("Authorization") token: String, @Field("password") password: String, @Field("confirmPassword") confirmPassword: String): Call<UserForgotPasswordResponse>

    @POST("/users/changePassword")
    @FormUrlEncoded
    fun changePasswordProfile(@Field("oldPassword") oldPassword: String, @Field("newPassword") newPassword: String, @Field("confirmNewPassword") confirmNewPassword: String): Call<UserForgotPasswordResponse>

    @GET("/users/profile")
    fun getProfile(): Call<ProfileResponse>

    @POST("/users/updateProfile")
    @FormUrlEncoded
    fun updateProfile(@Field("name") name: String,
                      @Field("email") email: String,
                      @Field("birthday") birthday: String,
                      @Field("gender") gender: Int): Call<UserLoginResponse>

    @Multipart
    @POST("/users/uploadAvatar")
    fun uploadAvatar(@Part avatar: MultipartBody.Part): Call<AvatarResponse>

    companion object {

        fun create(context: Context): UserService {
            val networkProvider = NetworkProvider.newInstance(context)
            return networkProvider.getService(UserService::class.java)
        }
    }
}