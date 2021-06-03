package com.example.arfashion.presentation.services

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.arfashion.presentation.app.local.UserLocalStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val pref = context.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val userStorage = UserLocalStorage(pref)
        val user = userStorage.load()
        val accessToken = user.credential.accessToken

        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        return chain.proceed(requestBuilder.build())
    }
}