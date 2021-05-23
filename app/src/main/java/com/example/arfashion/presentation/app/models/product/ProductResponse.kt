package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("details") val details : Details,
    @SerializedName("name") val name : String,
    @SerializedName("price") val price : Int,
    @SerializedName("sizes") val sizes : List<Sizes>,
    @SerializedName("comments") val comments : List<Comments>,
    @SerializedName("tags") val tags : List<Tags>,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("_id") val id : String,
    @SerializedName("images") val images : List<Images>,
    @SerializedName("description") val description : String,
    @SerializedName("total") val total : Int,
    @SerializedName("star") val star: Float,
    @SerializedName("priceSale") val priceSale: Int,
    @SerializedName("mask") val mask: String
)

data class Image (
    @SerializedName("img_banner") val img_banner : String,
    @SerializedName("img_category") val img_category : String
)

data class Comments (
    @SerializedName("content") val content : String,
    @SerializedName("star") val star : Float,
    @SerializedName("responses") val responses : List<Comments>,
    @SerializedName("like") val like : Int,
    @SerializedName("_id") val _id : String,
    @SerializedName("created_by") val created_by : CreatedBy
)

data class CreatedBy (
    @SerializedName("_id") val _id : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("name") val name : String
)

data class Details (
    @SerializedName("branch") val branch : String,
    @SerializedName("made_from") val madeFrom : String,
    @SerializedName("model") val model : String
) {
    override fun toString(): String {
        return "<p><b>- Brand:</b> $branch </p> <br/> <p><b>- Made from:</b> $madeFrom </p> <br/> <p><b>- Model:</b> $model </p> <br/>"
    }
}

data class Images (
    @SerializedName("url") val url : String,
    @SerializedName("color") val color : String,
    @SerializedName("desktop") val desktop : String,
    @SerializedName("mobile") val mobile : String
)

data class Sizes (
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String
)

data class Tags(
    @SerializedName("_id") val _id: String,
    @SerializedName("name") val name: String
)
