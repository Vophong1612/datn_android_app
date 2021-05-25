package com.example.arfashion.presentation.app.models.cart

import com.google.gson.annotations.SerializedName

data class UpdateCartResponse(
    @SerializedName("cart") val cart: UpdateCart
)

data class UpdateCart(
    @SerializedName("_id") val _id: String,
    @SerializedName("products") val products: List<ProductsUpdateCartResponse>,
    @SerializedName("__v") val __v: Int
)

data class ProductsUpdateCartResponse (
    @SerializedName("total") val total : Int,
    @SerializedName("price") val price : Int,
    @SerializedName("_id") val _id : String,
    @SerializedName("color") val color : String,
    @SerializedName("size") val size : String
)