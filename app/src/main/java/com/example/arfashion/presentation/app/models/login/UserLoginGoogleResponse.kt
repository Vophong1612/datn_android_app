package com.example.arfashion.presentation.app.models.loginstatus_code

import com.example.arfashion.presentation.data.model.RefreshToken
import com.example.arfashion.presentation.data.model.User
import com.google.gson.annotations.SerializedName

data class UserLoginGoogleResponse(
        @SerializedName("message")
    val message: String = "",
        @SerializedName("user")
    val user: User,
        @SerializedName("accessToken")
    val accessToken: String = "",
        @SerializedName("refreshToken")
    val refreshToken: RefreshToken,
        @SerializedName("statusCode")
    val statusCode: String = ""
)