package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressResponse (
    @SerializedName("_id") val _id: String,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("home") val home : String,
    @SerializedName("village") val village : String,
    @SerializedName("district") val district : String,
    @SerializedName("province") val province : String
)