package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.address.ResultAddressResponse
import com.example.arfashion.presentation.app.models.payment.AddBillResponse
import com.example.arfashion.presentation.app.models.payment.PaymentMethodResponse
import com.example.arfashion.presentation.app.models.payment.ProductInBill
import retrofit2.Call
import retrofit2.http.*

interface PaymentService {

    @POST("/bills/add")
    @FormUrlEncoded
    fun addBill(@Header("Authorization") token: String, @Field("totalProduct") totalProduct: Int,
                @Field("address") address: String,  @Field("payment") payment: String,
                @Field("totalCost") totalCost: Int, @Field("products") products: List<ProductInBill>): Call<AddBillResponse>

    @GET("/payments/list")
    fun getPaymentMethods(@Header("Authorization") token: String): Call<PaymentMethodResponse>

    companion object {

        fun create(): PaymentService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(PaymentService::class.java)
        }
    }
}