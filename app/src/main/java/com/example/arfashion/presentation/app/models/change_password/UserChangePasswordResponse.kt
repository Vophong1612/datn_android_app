package com.example.arfashion.presentation.app.models.change_password

import com.google.gson.annotations.SerializedName

data class UserChangePasswordResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int
)