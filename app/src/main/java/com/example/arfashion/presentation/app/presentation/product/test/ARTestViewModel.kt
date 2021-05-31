package com.example.arfashion.presentation.app.presentation.product.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.product.ARTestResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.services.ProductService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class ARTestViewModel : ViewModel() {

    private val productService = ProductService.create()

    private val _result = MutableLiveData<ARResult<String>>()
    val result : LiveData<ARResult<String>> = _result

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun testAR(img: MultipartBody.Part /*RequestBody*/, color: RequestBody, id: RequestBody) {
        _loading.value = true
        productService.testAR(img, color, id).enqueue(object: retrofit2.Callback<ARTestResponse> {
            override fun onResponse(
                call: Call<ARTestResponse>,
                response: Response<ARTestResponse>
            ) {
                when(response.code()) {
                    200 -> {
                        response.body()?.let {
                            _result.value = ARResult.Success(it.imgResult)
                        }
                    } else -> _result.value = ARResult.Error(Throwable("Can not test now !. Code: ${response.code()}"))
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<ARTestResponse>, t: Throwable) {
                _result.value = ARResult.Error(t)
                _loading.value = false
            }
        })
    }
}