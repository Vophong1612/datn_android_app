package com.example.arfashion.presentation.app.models.payment

import com.google.gson.annotations.SerializedName

data class ProductInBill (
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("size")
    val size: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("total")
    val total: String = "",
)