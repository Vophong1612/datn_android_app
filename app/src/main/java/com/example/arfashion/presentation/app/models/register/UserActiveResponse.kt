package com.example.arfashion.presentation.app.models.register

import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.RefreshToken
import com.example.arfashion.presentation.data.model.User
import com.google.gson.annotations.SerializedName

data class UserActiveResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("status_code")
        val status_code: Int,
        @SerializedName("user")
        val user: Profile,
        @SerializedName("accessToken")
        val accessToken: String = "",
        @SerializedName("refreshToken")
        val refreshToken: RefreshToken,
)



