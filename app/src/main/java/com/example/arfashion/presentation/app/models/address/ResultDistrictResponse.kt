package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class ResultDistrictResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("districts")
    val results: ArrayList<AddressDistrictResponse> = ArrayList()
)