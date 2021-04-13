package com.example.arfashion.presentation.app.models.register

import com.google.gson.annotations.SerializedName

data class UserActiveResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("status_code")
        val status_code: Int
) {
    val statusCode: StatusCode
        get() =
            when (status_code) {
                0 -> StatusCode.SUCCESS
                else -> StatusCode.EXISTED_ACCOUNT
            }
}

