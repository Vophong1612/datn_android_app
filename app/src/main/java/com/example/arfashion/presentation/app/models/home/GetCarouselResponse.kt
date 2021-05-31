package com.example.arfashion.presentation.app.models.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetCarouselResponse(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("created_at")
    val createAt: String = "",
    @SerializedName("updated_at")
    val updateAt: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("end_date")
    val endDate: String = ""
) : Serializable
