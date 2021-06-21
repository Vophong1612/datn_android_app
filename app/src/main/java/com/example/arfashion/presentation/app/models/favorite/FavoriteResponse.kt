package com.example.arfashion.presentation.app.models.favorite

import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.google.gson.annotations.SerializedName

data class FavoriteResponse (
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int = 0,
    @SerializedName("favorites")
    val favorites: List<String> = listOf()
)
