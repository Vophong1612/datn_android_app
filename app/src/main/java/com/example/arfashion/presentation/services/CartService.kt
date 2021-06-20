package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.cart.CartResponse
import com.example.arfashion.presentation.app.models.cart.UpdateCartResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface CartService {
    @GET("/carts/getCartDetail")
    fun getCart(): Call<CartResponse>

    @POST("/carts/updateCart")
    @FormUrlEncoded
    fun updateCart(
        @Field("productId") productId: String,
        @Field("sizeId") sizeId: String,
        @Field("color") color: String,
        @Field("price") price: Int,
        @Field("number") number: Int
    ): Call<UpdateCartResponse>

    @POST("/carts/removeProduct")
    @FormUrlEncoded
    fun removeProduct(
        @Field("productId") productId: String,
        @Field("sizeId") sizeId: String,
        @Field("color") color: String
    ) : Call<Boolean>

    companion object {
        fun create(context: Context): CartService {
            val networkProvider = NetworkProvider.newInstance(context)
            return networkProvider.getService(CartService::class.java)
        }
    }
}