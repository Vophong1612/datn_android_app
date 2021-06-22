package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.favorite.FavoriteResponse
import com.example.arfashion.presentation.app.models.favorite.ListFavoritesResponse
import retrofit2.Call
import retrofit2.http.*

interface FavoriteService {

    @POST("/favourites/addProduct")
    @FormUrlEncoded
    fun addToFavorite(@Field("productId") id: String): Call<FavoriteResponse>

    @GET("/favourites/getFavouriteDetail")
    fun getFavoriteList(): Call<ListFavoritesResponse>

    @POST("/favourites/removeProduct")
    @FormUrlEncoded
    fun deleteFromFavorite(@Field("productId") id: String): Call<FavoriteResponse>

    companion object {

        fun create(context: Context): FavoriteService {
            val networkProvider = NetworkProvider.newInstance(context)
            return networkProvider.getService(FavoriteService::class.java)
        }
    }
}