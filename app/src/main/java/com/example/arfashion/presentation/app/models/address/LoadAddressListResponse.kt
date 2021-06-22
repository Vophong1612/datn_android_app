package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class LoadAddressListResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int = 0,
    @SerializedName("address")
    val results: List<AddressResponse> = listOf()
)