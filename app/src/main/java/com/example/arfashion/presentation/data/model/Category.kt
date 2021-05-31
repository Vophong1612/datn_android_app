package com.example.arfashion.presentation.data.model

data class Category(
    val name: String = "",
    val id: String = "",
    val imageBanner: String = "",
    val imageCategory: String = "",
    val tag: List<String> = listOf()
)