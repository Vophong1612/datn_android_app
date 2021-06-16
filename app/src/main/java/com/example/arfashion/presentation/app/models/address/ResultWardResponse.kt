package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class ResultWardResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("villages")
    val results: ArrayList<AddressWardResponse> = ArrayList()
)