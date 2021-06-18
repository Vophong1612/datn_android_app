package com.example.arfashion.presentation.app.models.payment

import com.google.gson.annotations.SerializedName

data class ProductInBill (
    val _id: String = "",
    val name: String = "",
    val image: String = "",
    val color: String = "",
    val size: String = "",
    val price: Int = 0,
    val total: Int = 0,
)