package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.payment.AddBillResponse
import com.example.arfashion.presentation.app.models.payment.PaymentMethodResponse
import com.example.arfashion.presentation.app.models.payment.ProductInAPI
import retrofit2.Call
import retrofit2.http.*

interface PaymentService {

    @POST("/bills/add")
    @FormUrlEncoded
    fun addBill(@Field("totalProduct") totalProduct: Int,
                @Field("address") address: String,  @Field("payment") payment: String,
                @Field("totalCost") totalCost: Int, @Field("products") products: MutableList<ProductInAPI>): Call<AddBillResponse>

    @GET("/payments/list")
    fun getPaymentMethods(): Call<PaymentMethodResponse>

    companion object {

        fun create(context: Context): PaymentService {
            val networkProvider = NetworkProvider.newInstance(context)
            return networkProvider.getService(PaymentService::class.java)
        }
    }
}