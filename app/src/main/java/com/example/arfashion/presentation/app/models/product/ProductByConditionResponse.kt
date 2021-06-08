package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class ProductByConditionResponse(
    @SerializedName("products") val product: List<ProductByCondition>
)

data class ProductByCondition(
    @SerializedName("name") val name : String,
    @SerializedName("price") val price : Int,
    @SerializedName("tags") val tags : List<Tags>,
    @SerializedName("_id") val id : String,
    @SerializedName("images") val images : List<Images>,
    @SerializedName("sales") val priceSale: List<Sale>
)

data class Sale (
    @SerializedName("discount") val discount : Int
)