package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.GetProductAtHomeResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Carousel
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.ProductService
import com.example.arfashion.presentation.services.toCarousel
import com.example.arfashion.presentation.services.toProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (context: Context) : ViewModel() {
    private val productService =  ProductService.create(context)

    private val _carousels = MutableLiveData<ARResult<List<Carousel>>>()
    val carousel: LiveData<ARResult<List<Carousel>>>
        get() = _carousels

    private val _bestSellerProduct = MutableLiveData<ARResult<List<Product>>>()
    val bestSellerProduct: LiveData<ARResult<List<Product>>> = _bestSellerProduct

    private val _newestProduct = MutableLiveData<ARResult<List<Product>>>()
    val newestProduct: LiveData<ARResult<List<Product>>> = _newestProduct

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

    fun getBestSellerProduct(){
        productService.getPopularProduct().enqueue(object : Callback<GetProductAtHomeResponse> {
            override fun onResponse(
                call: Call<GetProductAtHomeResponse>,
                response: Response<GetProductAtHomeResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _bestSellerProduct.value = ARResult.Success(it.products.let { list ->
                                list.map { product -> product.toProduct() }
                            })
                        }
                    }
                    else -> _bestSellerProduct.value = ARResult.Error(Throwable("Can not get best seller product. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetProductAtHomeResponse>, t: Throwable) {
                _bestSellerProduct.value = ARResult.Error(t)
            }

        })
    }

    fun getNewestProduct(){
        productService.getNewestProduct(0, 4).enqueue(object : Callback<GetProductAtHomeResponse> {
            override fun onResponse(
                call: Call<GetProductAtHomeResponse>,
                response: Response<GetProductAtHomeResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _newestProduct.value = ARResult.Success(it.products.let { list ->
                                list.map { product -> product.toProduct() }
                            })
                        }
                    }
                    else -> _newestProduct.value = ARResult.Error(Throwable("Can not get newest product. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetProductAtHomeResponse>, t: Throwable) {
                _newestProduct.value = ARResult.Error(t)
            }

        })
    }
}