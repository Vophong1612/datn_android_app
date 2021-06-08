package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class GetCommentsResponse(
    @SerializedName("comments") val comment: List<Comments>,
    @SerializedName("totalDocs") val totals: Int
)

data class Comments(
    @SerializedName("content") val content: String,
    @SerializedName("star") val star: Float,
    @SerializedName("responses") val responses: List<Comments>?,
    @SerializedName("_id") val _id: String,
    @SerializedName("created_by") val created_by: CreatedBy
)

data class CreatedBy(
    @SerializedName("_id") val _id: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("name") val name: String
)