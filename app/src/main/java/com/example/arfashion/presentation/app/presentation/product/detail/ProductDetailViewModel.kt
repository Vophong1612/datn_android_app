package com.example.arfashion.presentation.app.presentation.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.product.ProductByCondition
import com.example.arfashion.presentation.app.models.product.ProductResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.ProductService
import com.example.arfashion.presentation.services.toProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel : ViewModel() {

    private val productService = ProductService.create()

    private val _product = MutableLiveData<ARResult<Product>>()
    val product: LiveData<ARResult<Product>>
        get() = _product

    private val _relatedProduct = MutableLiveData<ARResult<List<Product>>>()
    val relatedProduct: LiveData<ARResult<List<Product>>>
        get() = _relatedProduct

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getProductDetail(id: String) {
        _loading.value = true
        productService.findProduct(id).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _product.value = ARResult.Success(it.toProduct())
                        }
                    }
                    else ->  _product.value = ARResult.Error(Throwable("Can not get product. Code: ${response.code()}"))
                }

                _loading.value = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _product.value = ARResult.Error(t)
                _loading.value = false
            }
        })
    }

    fun getRelatedProduct(id: String) {
        productService.getRelatedProduct(id).enqueue(object : Callback<List<ProductByCondition>> {
            override fun onResponse(
                call: Call<List<ProductByCondition>>,
                response: Response<List<ProductByCondition>>
            ) {
                val data = response.body()
                if (data != null) {
                    _relatedProduct.value = ARResult.Success(data.map { product ->
                        product.toProduct()
                    })
                } else {
                    _relatedProduct.value = ARResult.Success(listOf())
                }
            }

            override fun onFailure(call: Call<List<ProductByCondition>>, t: Throwable) {
                _relatedProduct.value = ARResult.Error(t)
            }

        })
    }
}