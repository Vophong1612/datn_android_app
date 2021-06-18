package com.example.arfashion.presentation.app.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.payment.AddBillResponse
import com.example.arfashion.presentation.app.models.payment.PaymentMethodResponse
import com.example.arfashion.presentation.app.models.payment.ProductInBill
import com.example.arfashion.presentation.services.PaymentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel(
    private val paymentService: PaymentService
) : ViewModel() {

    private val _resultGetPaymentMethods = MutableLiveData<Boolean>()
    val resultGetPaymentMethods: LiveData<Boolean>
        get() = _resultGetPaymentMethods

    private val _getPaymentMethodsResponse = MutableLiveData<PaymentMethodResponse>()
    val getPaymentMethodsResponse: LiveData<PaymentMethodResponse>
        get() = _getPaymentMethodsResponse

    private val _resultAddBill = MutableLiveData<Boolean>()
    val resultAddBill: LiveData<Boolean>
        get() = _resultAddBill

    private val _addBillResponse = MutableLiveData<AddBillResponse>()
    val addBillResponse: LiveData<AddBillResponse>
        get() = _addBillResponse

    fun getPaymentMethods(token: String) {
        paymentService.getPaymentMethods("Bearer $token").enqueue(object :
            Callback<PaymentMethodResponse> {
            override fun onResponse(
                call: Call<PaymentMethodResponse>,
                response: Response<PaymentMethodResponse>
            ) {
                _getPaymentMethodsResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultGetPaymentMethods.value = response.isSuccessful
                    else -> _resultGetPaymentMethods.value = false
                }
            }

            override fun onFailure(call: Call<PaymentMethodResponse>, t: Throwable) {
                _resultGetPaymentMethods.value = false
            }
        })
    }

    fun addBill(token: String, totalProduct: Int, address: String, payment: String, totalCost: Int, products: List<ProductInBill>) {
        paymentService.addBill("Bearer $token", totalProduct, address, payment, totalCost, products).enqueue(object :
            Callback<AddBillResponse> {
            override fun onResponse(
                call: Call<AddBillResponse>,
                response: Response<AddBillResponse>
            ) {
                _addBillResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultAddBill.value = response.isSuccessful
                    else -> _resultAddBill.value = false
                }
            }

            override fun onFailure(call: Call<AddBillResponse>, t: Throwable) {
                _resultAddBill.value = false
            }
        })
    }
}