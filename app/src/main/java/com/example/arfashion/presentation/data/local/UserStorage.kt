package com.example.arfashion.presentation.data.local

import com.example.arfashion.presentation.data.model.User

interface UserStorage {
    fun load(): User

    fun save(user: User)
}