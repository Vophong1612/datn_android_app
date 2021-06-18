package com.example.arfashion.presentation.app.models.payment

import com.google.gson.annotations.SerializedName

data class ProductInAPI (
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("size")
    val size: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
    )