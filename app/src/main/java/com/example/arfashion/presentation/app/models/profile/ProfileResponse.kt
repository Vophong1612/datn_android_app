package com.example.arfashion.presentation.app.models.profile

import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.google.gson.annotations.SerializedName

data class ProfileResponse (
    @SerializedName("_id")
    val _id: String,
    @SerializedName("name")
    val name : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("phone")
    val phone : String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("birthday")
    val birthday : String,
    @SerializedName("gender")
    val gender : Int,
    @SerializedName("account_status")
    val account_status : String,
    @SerializedName("address")
    val address : List<AddressResponse> = listOf()
)