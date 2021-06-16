package com.example.arfashion.presentation.app.models.login

import com.example.arfashion.presentation.data.model.RefreshToken
import com.example.arfashion.presentation.data.model.User
import com.google.gson.annotations.SerializedName

data class UserLoginFacebookResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("user")
        val user: User,
        @SerializedName("accessToken")
        val accessToken: String = "",
)