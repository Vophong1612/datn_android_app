package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {
    companion object {
        fun create(): ProductService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(ProductService::class.java)
        }
    }

    @GET("/events/list")
    fun getCarousels(): Call<List<GetCarouselResponse>>
}