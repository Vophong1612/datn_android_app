package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.CategoriesResponse
import com.example.arfashion.presentation.app.models.product.ProductByCondition
import com.example.arfashion.presentation.app.models.product.ProductByConditionResponse
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
    fun getCarousels(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<List<GetCarouselResponse>>

    @GET("/products/getProductById")
    fun findProduct(@Query("_id") id: String): Call<ProductResponse>

    @GET("/products/relatedProducts")
    fun getRelatedProduct(@Query("_id") id: String): Call<List<ProductByCondition>>

    @GET("/categories/list")
    fun getCategories(): Call<List<CategoriesResponse>>

    @GET("/products/list")
    fun getProductByCondition(
        @Query("q") keyword: String = "",
        @Query("updated_at") time: String = "asc",
        @Query("category") categoryId: String = "all",
        @Query("priceRange") priceRange: String = "",
        @Query("priceSort") priceSort: String = "asc",
        @Query("size") sizeId: String = "",
        @Query("tags") tags: List<String> = listOf(),
        @Query("limit") limit: Int = 12,
        @Query("offset") offset: Int = 0
    ): Call<ProductByConditionResponse>
}