package com.example.arfashion.presentation.app.models.payment

import com.google.gson.annotations.SerializedName

data class PaymentMethodResponse(
    @SerializedName("payments")
    val payments: List<PaymentItem> = listOf(),
    @SerializedName("status_code")
    val status_code: Int = 0,
)
