package com.example.arfashion.presentation.app.models.product

import com.google.gson.annotations.SerializedName

data class CategoriesResponse (
    @SerializedName("name") val name : String,
    @SerializedName("tags") val tags : List<CategoryTags>,
    @SerializedName("_id") val _id : String,
    @SerializedName("image") val image : MainCategoryImage,
    @SerializedName("listImage") val listImage : ListImage
)

data class MainCategoryImage (
    @SerializedName("img_banner") val imgBanner : String,
    @SerializedName("img_category") val imaCategory : String
)

data class ImgBanner (
    @SerializedName("mobile") val mobile : String,
    @SerializedName("desktop") val desktop : String
)

data class ImageCategory (
    @SerializedName("mobile") val mobile : String,
    @SerializedName("desktop") val desktop : String
)

data class ListImage (
    @SerializedName("img_banner") val img_banner : ImgBanner,
    @SerializedName("img_category") val img_category : ImageCategory
)

data class CategoryTags (
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String
)