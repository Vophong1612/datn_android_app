package com.example.arfashion.presentation.app.models.forgot_password

import com.google.gson.annotations.SerializedName

data class UserForgotPasswordResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int = 0,
    @SerializedName("accessToken")
    val accessToken: String = ""
)