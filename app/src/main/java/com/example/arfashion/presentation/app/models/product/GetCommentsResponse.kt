package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class GetCommentsResponse(
    @SerializedName("comments") val comment: List<Comments>,
    @SerializedName("totalDocs") val totals: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPage: Int,
    @SerializedName("totalCurrentDocs") val totalCurrentProduct: Int,
)

data class Comments(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("star") val star: Float,
    @SerializedName("attach_images") val images : List<String>,
    @SerializedName("_id") val _id: String,
    @SerializedName("created_by") val created_by: CreatedBy,
    @SerializedName("status") val status: String,
    @SerializedName("product") val productId: String,
    @SerializedName("created_at") val createdAt: String
)

data class CreatedBy(
    @SerializedName("_id") val _id: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("name") val name: String
)