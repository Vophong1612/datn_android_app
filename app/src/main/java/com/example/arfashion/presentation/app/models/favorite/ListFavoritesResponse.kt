package com.example.arfashion.presentation.app.models.favorite

import com.example.arfashion.presentation.app.models.product.Details
import com.google.gson.annotations.SerializedName

data class ListFavoritesResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int = 0,
    @SerializedName("favourites")
    val favourites: List<FavoriteItem> = listOf()
)

data class FavoriteItem(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("priceSale")
    val priceSale: Int = 0,
    @SerializedName("sales")
    val sales: Sales,
    @SerializedName("total_comments")
    val total_comments: Int = 0,
    @SerializedName("details")
    val details: Details = Details("", "", ""),
    @SerializedName("star")
    val star: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("images")
    val images: List<ImageItem> = listOf(),
    @SerializedName("total")
    val total: Int = 0,
)

data class ImageItem(
    @SerializedName("url")
    val url: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("desktop")
    val desktop: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
)

data class Sales (
    @SerializedName("listSale") val listSale : List<Sale> = listOf(),
    @SerializedName("total") val total : Int = 0
)

data class Sale (
    @SerializedName("discount") val discount : Int
)

