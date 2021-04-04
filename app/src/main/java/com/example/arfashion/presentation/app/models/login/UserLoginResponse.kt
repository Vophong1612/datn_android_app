package com.example.arfashion.presentation.app.models.login

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("accesstoken")
    val accessToken: String = "",
    @SerializedName("refreshtoken")
    val refreshToken: String = ""
) {
    val statusCode: StatusCode
        get() =
            when (status_code) {
                0 -> StatusCode.SUCCESS
                1 -> StatusCode.WRONG_INFO
                else -> StatusCode.ACCOUNT_BLOCK
            }
}

enum class StatusCode {
    SUCCESS,
    WRONG_INFO,
    ACCOUNT_BLOCK
}
