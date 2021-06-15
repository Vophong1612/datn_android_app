package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/tags/list")
    fun getTags(): Call<List<Tags>>

    @GET("/sizes/list")
    fun getSizes(): Call<List<Sizes>>

    @GET("/products/list")
    fun getProductByCondition(
        @Query("q") keyword: String = "",
        @Query("updated_at") time: String = "asc",
        @Query("category") categoryId: String = "all",
        @Query("priceRange") priceRange: String = "",
        @Query("priceSort") priceSort: String = "asc",
        @Query("size") sizeId: String = "",
        @Query("tags") tags: String = "",
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Call<ProductByConditionResponse>

    @Multipart
    @POST("/products/testAR")
    fun testAR(@Part body: MultipartBody.Part, @Part("color") color: RequestBody, @Part("_id") id: RequestBody) : Call<ARTestResponse>
}