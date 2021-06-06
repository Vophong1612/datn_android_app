package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressWardResponse(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("province")
        val province: String = "",
        @SerializedName("district")
        val district: String = "",
        @SerializedName("name")
        val name: String = "",
)