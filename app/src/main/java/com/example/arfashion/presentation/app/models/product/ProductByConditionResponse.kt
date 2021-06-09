package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class ProductByConditionResponse(
    @SerializedName("products") val product: List<ProductByCondition>,
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPage: Int,
    @SerializedName("totalCurrentDocs") val totalCurrentProduct: Int,
    @SerializedName("totalDocs") val total: Int
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