package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.bill.AddBillRequest
import com.example.arfashion.presentation.app.models.payment.AddBillResponse
import com.example.arfashion.presentation.app.models.payment.PaymentMethodResponse
import com.example.arfashion.presentation.app.models.payment.ProductInAPI
import retrofit2.Call
import retrofit2.http.*

interface PaymentService {

    @Headers("Content-Type: application/json")
    @POST("/bills/add")
    fun addBill(@Body addBillRequest: AddBillRequest): Call<AddBillResponse>

    @GET("/payments/list")
    fun getPaymentMethods(): Call<PaymentMethodResponse>

    companion object {

        fun create(context: Context): PaymentService {
            val networkProvider = NetworkProvider.newInstance(context)
            return networkProvider.getService(PaymentService::class.java)
        }
    }
}