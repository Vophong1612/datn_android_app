package com.example.arfashion.presentation.data.model

data class Cart(
    val id: String = "",
    val product: List<Product> = listOf()
)
//{
//    val totalPrice
//        get() = run {
//            product.sumOf { it.priceSale * it.total }
//        }
//}
