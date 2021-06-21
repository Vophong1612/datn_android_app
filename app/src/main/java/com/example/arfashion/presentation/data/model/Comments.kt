package com.example.arfashion.presentation.data.model

import java.util.*

data class Comment(
    val title: String = "",
    val content: String = "",
    val stars: Float = 0f,
    val images: List<String> = listOf(),
    val id: String = "",
    val owner: Profile = Profile(),
    val createAt: Date = Date(),
    val productId: String = ""
)
