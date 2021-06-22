package com.example.arfashion.presentation.app.models.bill

import com.example.arfashion.presentation.app.models.payment.ProductInAPI
import com.google.gson.annotations.SerializedName

data class AddBillRequest(
    @SerializedName("totalProduct") val totalProduct: Int,
    @SerializedName("address") val address: String, //addressID
    @SerializedName("payment") val payment: String, //paymentID
    @SerializedName("totalCost") val totalCost: Int,
    @SerializedName("products") val products: List<ProductInAPI>
)