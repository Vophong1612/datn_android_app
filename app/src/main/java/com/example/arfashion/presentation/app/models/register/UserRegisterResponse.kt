package com.example.arfashion.presentation.app.models.register

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("status_code")
        val status_code: Int
) {
    val statusCode: StatusCode
        get() =
            when (status_code) {
                0 -> StatusCode.SUCCESS
                1 -> StatusCode.ERROR_PARAMS
                else -> StatusCode.EXISTED_ACCOUNT
            }
}

enum class StatusCode {
    SUCCESS,
    ERROR_PARAMS,
    EXISTED_ACCOUNT
}
