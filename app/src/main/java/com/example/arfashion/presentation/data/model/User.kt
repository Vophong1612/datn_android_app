package com.example.arfashion.presentation.data.model

import com.example.arfashion.presentation.data.credential.Credential

data class User(
    var profile: Profile,
    val credential: Credential
) {
    companion object {
        val GUEST = User(Profile(), Credential())
    }
}
