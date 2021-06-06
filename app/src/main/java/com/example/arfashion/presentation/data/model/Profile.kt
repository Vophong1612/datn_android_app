package com.example.arfashion.presentation.data.model

data class Profile (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: String? = "",
    val gender: Int = -1,
    val birthday: String = "",
    val account_status: String = "",
    val refresh_token: RefreshToken = RefreshToken("","","")

)