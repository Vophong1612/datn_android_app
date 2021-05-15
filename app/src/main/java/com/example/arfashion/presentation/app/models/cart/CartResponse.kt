package com.example.arfashion.presentation.app.models.cart

import com.google.gson.annotations.SerializedName

data class CartResponse (
    @SerializedName("cart") val cart : Cart
)

data class Cart(
    @SerializedName("_id") val _id: String,
    @SerializedName("products") val products: List<ProductsCartResponse>,
    @SerializedName("__v") val __v: Int
)

data class Images (
    @SerializedName("desktop") val desktop : String,
    @SerializedName("mobile") val mobile : String
)

data class ProductsCartResponse (
    @SerializedName("total") val total : Int,
    @SerializedName("price") val price : Int,
    @SerializedName("_id") val _id : String,
    @SerializedName("color") val color : String,
    @SerializedName("size") val size : Size,
    @SerializedName("images") val images : Images,
    @SerializedName("name") val name : String,
    @SerializedName("priceSale") val priceSale : Int
)

data class Size (
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String
)