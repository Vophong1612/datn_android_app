package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.product.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface CommentService {
    companion object {
        fun create(context: Context): CommentService {
            val networkProvider = NetworkProvider.newInstance(context)
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

    @Multipart
    @POST("/comments/add")
    fun addComment(
        @Part photos: List<MultipartBody.Part>, @Part("star") star: RequestBody,
        @Part("content") content: RequestBody, @Part("title") title: RequestBody,
        @Part("productId") productId: RequestBody
    ): Call<Unit>
}