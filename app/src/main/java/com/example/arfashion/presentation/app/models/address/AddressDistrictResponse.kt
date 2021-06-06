package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressDistrictResponse(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("province")
        val province: String = "",
        @SerializedName("name")
        val name: String = "",
)