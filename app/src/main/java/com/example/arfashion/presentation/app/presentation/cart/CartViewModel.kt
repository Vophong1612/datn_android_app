package com.example.arfashion.presentation.app.presentation.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.cart.CartResponse
import com.example.arfashion.presentation.app.models.cart.UpdateCartResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Cart
import com.example.arfashion.presentation.services.CartService
import com.example.arfashion.presentation.services.toCart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {
    private val cartService = CartService.create()

    private val _cart = MutableLiveData<ARResult<Cart>>()
    val cart: LiveData<ARResult<Cart>>
        get() = _cart

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _totalPrice = MutableLiveData(0)
    val totalPrice: LiveData<Int> = _totalPrice

    private val _updateCart = MutableLiveData<ARResult<Boolean>>()
    val updateCart: LiveData<ARResult<Boolean>> = _updateCart

    private val _removeProduct = MutableLiveData<ARResult<Boolean>>()
    val removeProduct: LiveData<ARResult<Boolean>> = _removeProduct

    fun getCart() {
        _loading.value = true
        cartService.getCart().enqueue(object : Callback<CartResponse> {
            override fun onResponse(
                call: Call<CartResponse>,
                response: Response<CartResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _cart.value = ARResult.Success(it.cart.toCart())
                        }
                    }
                    else -> _cart.value =
                        ARResult.Error(Throwable("Can not get cart. Code: ${response.code()}"))
                }

                _loading.value = false
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                _cart.value = ARResult.Error(t)
                _loading.value = false
            }
        })
    }

    fun updateTotalPrice(prices: Int) {
        _totalPrice.value = _totalPrice.value?.plus(prices)
    }

    fun updateCart(productId: String, sizeId: String, color: String, price: Int, number: Int) {
        cartService.updateCart(productId, sizeId, color, price, number).enqueue(object: Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _updateCart.value = ARResult.Success(true)
                        }
                    } else -> _updateCart.value = ARResult.Error(Throwable("Update cart fail. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                _updateCart.value = ARResult.Error(t)
            }

        })
    }

    fun removeProduct(productId: String, sizeId: String, color: String) {
        cartService.removeProduct(productId, sizeId, color).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _removeProduct.value = ARResult.Success(true)
                        }
                    } else -> _removeProduct.value = ARResult.Error(Throwable("Update cart fail. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                _removeProduct.value = ARResult.Error(t)
            }
        })
    }
}