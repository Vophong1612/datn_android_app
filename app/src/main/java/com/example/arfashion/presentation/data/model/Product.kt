package com.example.arfashion.presentation.data.model

data class Product(
    val name: String = "",
    val categories: String = "", //TODO: hỏi xem catagories trả về list hay 1 category
    val color: String = "",
    val images: String = "",
    val sizes: String = "",
    val sales: Int = 0,
    val comments: String = "",
    val prices: Int = 0,
    val description: String = "",
    val details: String = "",
    val total: Int = 0
)