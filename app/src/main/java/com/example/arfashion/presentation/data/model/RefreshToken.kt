package com.example.arfashion.presentation.data.model

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("token")
    val token: String = "",
    @SerializedName("iat")
    val iat: String = "",
    @SerializedName("exp")
    val exp: String = "",
)