package com.example.arfashion.presentation.data.local

import com.example.arfashion.presentation.data.model.User

interface UserStorage {
    fun load(): User

    fun loadPassword(): String

    fun save(user: User)

    fun savePassword(password: String)
}