package com.example.arfashion.presentation.data.model

data class Product(
    val id: String = "",
    val name: String = "",
//    val colors: List<ProductColor> = listOf(),
    val sizes: List<String> = listOf(),
    val comments: List<Comment> = listOf(),
    val images: List<String> = listOf(),
    val thumbnail: List<String> = listOf(),
    val prices: Int = 0,
    val description: String = "",
    val details: String = "",
    val tag: List<String> = listOf(),
    val total: Int = 0,
    val star: Float = 0f,
    val priceSale: Int = 0,
    val mask: String = "",
    val category: Category = Category()
) {
    val isSale: Boolean
        get() = (prices - priceSale > 0)
}

//data class ProductColor(
//    val id: String = "",
//    val name: String = "",
//    val image: String = ""
//)