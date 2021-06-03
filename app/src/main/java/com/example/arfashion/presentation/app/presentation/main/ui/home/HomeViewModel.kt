package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Carousel
import com.example.arfashion.presentation.services.ProductService
import com.example.arfashion.presentation.services.toCarousel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (context: Context) : ViewModel() {
    private val productService =  ProductService.create(context)

    private val _carousels = MutableLiveData<ARResult<List<Carousel>>>()
    val carousel: LiveData<ARResult<List<Carousel>>>
        get() = _carousels

    fun getCarouselList() {
        productService.getCarousels(4, 0).enqueue(object : Callback<List<GetCarouselResponse>> {
            override fun onResponse(
                call: Call<List<GetCarouselResponse>>,
                response: Response<List<GetCarouselResponse>>
            ) {
                response.body()?.let { it ->
                    _carousels.value = ARResult.Success(it.map { carousel ->
                        carousel.toCarousel()
                    })
                }
            }

            override fun onFailure(call: Call<List<GetCarouselResponse>>, t: Throwable) {
                _carousels.value = ARResult.Error(t)
            }

        })
    }
}