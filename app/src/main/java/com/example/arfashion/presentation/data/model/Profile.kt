package com.example.arfashion.presentation.data.model

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class Profile (
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("avatar")
    val avatar: String? = "",
    @SerializedName("gender")
    val gender: Int = -1,
    @SerializedName("birthday")
    var birthday: String = "",
    @SerializedName("account_status")
    val account_status: String = "",

)