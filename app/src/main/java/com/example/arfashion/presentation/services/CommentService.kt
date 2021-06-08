package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.*
import com.example.arfashion.presentation.data.model.Comment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CommentService {
    companion object {
        fun create(): CommentService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(CommentService::class.java)
        }
    }

    @GET("/products/getComments")
    fun getComments(
        @Query("productId") productId: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 5,
        @Query("star") star: Int? = null,
        @Query("create_at") sortDate: String = "asc"
    ): Call<GetCommentsResponse>
}