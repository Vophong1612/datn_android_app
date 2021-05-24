package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class ResultDistrictResponse(
        @SerializedName("results")
        val results: ArrayList<AddressDistrictResponse> = ArrayList(),
)