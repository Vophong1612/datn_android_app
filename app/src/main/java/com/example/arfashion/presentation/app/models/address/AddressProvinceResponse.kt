package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressProvinceResponse(
        @SerializedName("province_id")
        val province_id: String = "",
        @SerializedName("province_name")
        val province_name: String = "",
        @SerializedName("province_type")
        val province_type: String = "",
)

