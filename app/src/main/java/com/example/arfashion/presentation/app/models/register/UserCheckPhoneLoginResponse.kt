package com.example.arfashion.presentation.app.models.register

import com.google.gson.annotations.SerializedName

data class CheckPhoneLoginResponse (
        @SerializedName("message")
        val message: String = "",
        @SerializedName("status_code")
        val status_code: Int
)