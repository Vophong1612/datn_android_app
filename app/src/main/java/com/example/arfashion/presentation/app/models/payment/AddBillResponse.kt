package com.example.arfashion.presentation.app.models.payment

import com.google.gson.annotations.SerializedName

data class AddBillResponse (
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int = 0,
)