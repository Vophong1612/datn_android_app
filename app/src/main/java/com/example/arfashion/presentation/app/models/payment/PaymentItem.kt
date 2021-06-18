package com.example.arfashion.presentation.app.models.payment

import com.google.gson.annotations.SerializedName

data class PaymentItem(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("name")
    val name: String = ""
)