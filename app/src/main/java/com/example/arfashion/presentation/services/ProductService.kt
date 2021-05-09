package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    companion object {
        fun create(): ProductService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(ProductService::class.java)
        }
    }

    @GET("/events/list")
    fun getCarousels(): Call<List<GetCarouselResponse>>

    @GET("/products/findProduct")
    fun findProduct(@Query("_id") id: String): Call<ProductResponse>

    @GET("/products/relatedProducts")
    fun getRelatedProduct(@Query("_id") id: String): Call<List<ProductResponse>>
}