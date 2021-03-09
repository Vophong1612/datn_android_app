package com.example.arfashion.presentation.data

import com.example.arfashion.presentation.data.local.UserStorage
import com.example.arfashion.presentation.data.model.User

class ARFashionUserManager(
    private val userStorage: UserStorage
) {
    private var instance: ARFashionUserManager? = null

    fun getInstance(): ARFashionUserManager{
        if (instance == null) {
            instance = ARFashionUserManager(userStorage)
        }
        return instance as ARFashionUserManager
    }

    private var _currentUser: User = userStorage.load()

    var currentUser: User
        get() = _currentUser
        set(value) {
            _currentUser = value
            userStorage.save(_currentUser)
        }

    fun isUserLoggedIn(): Boolean? = currentUser.credential.accessToken?.isNotEmpty()

    fun clear() {
        currentUser = User.GUEST
    }
}