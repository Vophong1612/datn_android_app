package com.example.arfashion.presentation.data.model

data class Profile (
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val avatar: String = "",
    val gender: Int = -1,
    val birthday: String = "",
    val account_status: Int = -1,
    val refresh_token: RefreshToken = RefreshToken("","","")

)