package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.CategoriesResponse
import com.example.arfashion.presentation.app.models.product.ProductByCategoryResponse
import com.example.arfashion.presentation.app.models.product.ProductResponse
import com.example.arfashion.presentation.app.models.product.RelatedProductResponse
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

    @GET("/products/getProductById")
    fun findProduct(@Query("_id") id: String): Call<ProductResponse>

    @GET("/products/relatedProducts")
    fun getRelatedProduct(@Query("_id") id: String): Call<List<RelatedProductResponse>>

    @GET("/categories/list")
    fun getCategories(): Call<List<CategoriesResponse>>

    @GET("/products/listByKeyWord")
    fun searchByKeyWord(@Query("q") keyword: String): Call<List<ProductResponse>>

    @GET("/products/listByCategory")
    fun getProductListByCategory(@Query("_id") id: String): Call<List<ProductByCategoryResponse>>
}