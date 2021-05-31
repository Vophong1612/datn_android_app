package com.example.arfashion.presentation.app.models.profile

import com.google.gson.annotations.SerializedName

data class ResultWardResponse(
        @SerializedName("results")
        val results: ArrayList<AddressWardResponse> = ArrayList(),
)