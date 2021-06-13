package com.example.arfashion.presentation.app.models.profile

import com.google.gson.annotations.SerializedName

data class AvatarResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int = 0,
    @SerializedName("avatar")
    val avatar : String = "",
)