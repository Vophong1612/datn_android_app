package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class ResultProvinceResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("provinces")
    val results: ArrayList<AddressProvinceResponse> = ArrayList()
)