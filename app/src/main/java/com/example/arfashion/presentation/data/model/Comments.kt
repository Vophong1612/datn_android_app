package com.example.arfashion.presentation.data.model

import java.util.*

data class Comment(
    val content: String = "",
    val stars: Float = 0f,
    val likes: Int = 0,
    val id: String = "",
    val owner: Profile = Profile(),
    val response: List<Comment> = listOf(),
    val createAt: Date = Date()
)
