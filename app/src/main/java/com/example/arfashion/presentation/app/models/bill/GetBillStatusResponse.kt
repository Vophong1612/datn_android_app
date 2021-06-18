package com.example.arfashion.presentation.app.models.bill

import com.google.gson.annotations.SerializedName

data class GetBillStatusResponse(
    @SerializedName("name") val name: String,
    @SerializedName("_id") val _id: String
)
