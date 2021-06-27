package com.example.arfashion.presentation.app.presentation.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.favorite.FavoriteResponse
import com.example.arfashion.presentation.app.models.favorite.ListFavoritesResponse
import com.example.arfashion.presentation.services.FavoriteService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel(context: Context)  : ViewModel() {

    private val favoriteService = FavoriteService.create(context)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _resultAddToFavorite = MutableLiveData<Boolean>()
    val resultAddToFavorite: LiveData<Boolean>
        get() = _resultAddToFavorite

    private val _addToFavoriteResponse = MutableLiveData<FavoriteResponse>()
    val addToFavoriteResponse: LiveData<FavoriteResponse>
        get() = _addToFavoriteResponse

    private val _resultDeleteFromFavorite = MutableLiveData<Boolean>()
    val resultDeleteFromFavorite: LiveData<Boolean>
        get() = _resultDeleteFromFavorite

    private val _deleteFromFavoriteResponse = MutableLiveData<FavoriteResponse>()
    val deleteFromFavoriteResponse: LiveData<FavoriteResponse>
        get() = _deleteFromFavoriteResponse

    private val _resultGetFavorites = MutableLiveData<Boolean>()
    val resultGetFavorites: LiveData<Boolean>
        get() = _resultGetFavorites

    private val _getFavoritesResponse = MutableLiveData<ListFavoritesResponse>()
    val getFavoritesResponse: LiveData<ListFavoritesResponse>
        get() = _getFavoritesResponse

    fun addToFavorite(id: String) {
        favoriteService.addToFavorite(id).enqueue(object :
            Callback<FavoriteResponse> {
            override fun onResponse(
                call: Call<FavoriteResponse>,
                response: Response<FavoriteResponse>
            ) {
                _addToFavoriteResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultAddToFavorite.value = response.isSuccessful
                    else -> _resultAddToFavorite.value = false
                }
            }

            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                _resultAddToFavorite.value = false
            }
        })
    }

    fun deleteFromFavorite(id: String) {
        favoriteService.deleteFromFavorite(id).enqueue(object :
            Callback<FavoriteResponse> {
            override fun onResponse(
                call: Call<FavoriteResponse>,
                response: Response<FavoriteResponse>
            ) {
                _deleteFromFavoriteResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultDeleteFromFavorite.value = response.isSuccessful
                    else -> _resultDeleteFromFavorite.value = false
                }
            }

            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                _resultDeleteFromFavorite.value = false
            }
        })
    }

    fun getFavorites() {
        _loading.value = true
        favoriteService.getFavoriteList().enqueue(object :
            Callback<ListFavoritesResponse> {
            override fun onResponse(
                call: Call<ListFavoritesResponse>,
                response: Response<ListFavoritesResponse>
            ) {
                _getFavoritesResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultGetFavorites.value = response.isSuccessful
                    else -> _resultGetFavorites.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<ListFavoritesResponse>, t: Throwable) {
                _resultGetFavorites.value = false
                _loading.value = false
            }
        })
    }

}