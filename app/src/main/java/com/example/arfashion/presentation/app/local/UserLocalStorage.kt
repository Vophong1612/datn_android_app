package com.example.arfashion.presentation.app.local

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.local.UserStorage
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User

private const val KEY_USER_ID = "pref_user_id"
private const val KEY_USER_NAME = "pref_user_name"
private const val KEY_USER_EMAIL = "pref_user_email"
private const val KEY_USER_PHONE = "pref_user_phone"
private const val KEY_USER_AVATAR = "pref_user_avatar"
private const val KEY_USER_GENDER = "pref_user_gender"
private const val KEY_USER_BIRTHDAY = "pref_user_birthday"
private const val KEY_USER_STATUS = "pref_user_status"

private const val KEY_USER_ACCESS_TOKEN = "pref_access_token"

class UserLocalStorage(private val pref: SharedPreferences) : UserStorage {
    override fun load(): User {
        //implements here
        return User(Profile(), Credential(pref.getString(KEY_USER_ACCESS_TOKEN, "").toString()))
    }

    override fun save(user: User) {
        pref.edit().apply {
            putString(KEY_USER_ID, user.profile.id)
            putString(KEY_USER_NAME, user.profile.name)
            putString(KEY_USER_EMAIL, user.profile.email)
            putString(KEY_USER_PHONE, user.profile.phone)
            putString(KEY_USER_AVATAR, user.profile.avatar)
            putInt(KEY_USER_GENDER, user.profile.gender)
            putString(KEY_USER_BIRTHDAY, user.profile.birthday)
            putString(KEY_USER_STATUS, user.profile.account_status)
            putString(KEY_USER_ACCESS_TOKEN, user.credential.accessToken)
        }.apply()
    }

}