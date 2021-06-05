package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressWardResponse(
        @SerializedName("district_id")
        val district_id: String = "",
        @SerializedName("ward_id")
        val ward_id: String = "",
        @SerializedName("ward_name")
        val ward_name: String = "",
        @SerializedName("ward_type")
        val ward_type: String = "",
)