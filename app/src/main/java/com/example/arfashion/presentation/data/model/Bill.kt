package com.example.arfashion.presentation.data.model

import com.example.arfashion.presentation.app.models.bill.AddressResponse

data class Bill(
    val totalCost: Int,
    val totalProduct: Int,
    val note: String,
    val id: String,
    val products: List<Product>,
    val address: AddressResponse,
    val payment: Payment,
    val billStatus: BillStatus
)