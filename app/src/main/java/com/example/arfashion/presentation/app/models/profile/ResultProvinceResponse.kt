package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class ResultProvinceResponse(
        @SerializedName("results")
        val results: ArrayList<AddressProvinceResponse> = ArrayList(),
)