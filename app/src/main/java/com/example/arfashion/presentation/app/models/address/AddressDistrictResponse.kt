package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressDistrictResponse(
        @SerializedName("province_id")
        val province_id: String = "",
        @SerializedName("district_id")
        val district_id: String = "",
        @SerializedName("district_name")
        val district_name: String = "",
        @SerializedName("district_type")
        val district_type: String = "",
        @SerializedName("lat")
        val lat: String = "",
        @SerializedName("lng")
        val lng: String = "",
)