package com.example.arfashion.presentation.app.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeToCategoriesShareViewModel : ViewModel() {
    private val _searchClickEvent = MutableLiveData<Unit>()
    val searchClickEvent: LiveData<Unit>
        get() = _searchClickEvent

    fun onSearchClick() {
        _searchClickEvent.value = Unit
    }
}