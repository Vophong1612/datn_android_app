package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class ProductByCategoryResponse(
    @SerializedName("name") val name : String,
    @SerializedName("price") val price : Int,
    @SerializedName("tags") val tags : List<Tags>,
    @SerializedName("_id") val id : String,
    @SerializedName("images") val images : List<Images>,
    @SerializedName("priceSale") val priceSale: Int
)