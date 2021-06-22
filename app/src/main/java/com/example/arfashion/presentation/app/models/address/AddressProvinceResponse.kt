package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressProvinceResponse(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("name")
        val name: String = "",
)

