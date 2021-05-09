package com.example.arfashion.presentation.data.model

data class Profile (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatar: String? = "",
    val gender: Int = -1,
    val birthday: String = "",
    val status: Int = -1,
)