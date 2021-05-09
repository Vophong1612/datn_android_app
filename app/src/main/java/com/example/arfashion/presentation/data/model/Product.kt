package com.example.arfashion.presentation.data.model

import java.util.*
import kotlin.math.roundToLong

data class Product(
    val id: String = "",
    val name: String = "",
//    val colors: List<ProductColor> = listOf(),
    val sizes: List<String> = listOf(),
    val comments: List<Comment> = listOf(),
    val images: List<String> = listOf(),
    val thumbnail: List<String> = listOf(),
    val sales: Int = 0,
    val prices: Int = 0,
    val description: String = "",
    val details: String = "",
    val tag: List<String> = listOf(),
    val total: Int = 0,
    val star: Float = 0f,
    val priceSale: Int = 0
) {
    val isSale: Boolean
        get() = (sales > 0)
}

//data class Sales(
//    val discount: Int = 0,
//    val endDate: Date = Date()
//)

//data class ProductColor(
//    val id: String = "",
//    val name: String = "",
//    val image: String = ""
//)