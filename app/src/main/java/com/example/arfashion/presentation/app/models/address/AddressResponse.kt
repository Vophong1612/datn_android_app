package com.example.arfashion.presentation.app.models.address

import com.google.gson.annotations.SerializedName

data class AddressResponse (
    @SerializedName("_id") val _id: String = "",
    @SerializedName("name") val name : String = "",
    @SerializedName("email") val email : String = "",
    @SerializedName("phone") val phone : String = "",
    @SerializedName("home") val home : String = "",
    @SerializedName("village") val village: AddressWardResponse = AddressWardResponse(),
    @SerializedName("district") val district : AddressDistrictResponse = AddressDistrictResponse(),
    @SerializedName("province") val province : AddressProvinceResponse = AddressProvinceResponse(),
    @SerializedName("is_default") val isDefault: Boolean = false,
    @SerializedName("created_at") val createAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = "",
)