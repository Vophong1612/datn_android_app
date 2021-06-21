package com.example.arfashion.presentation.app.presentation.payment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.payment.AddBillResponse
import com.example.arfashion.presentation.app.models.payment.PaymentMethodResponse
import com.example.arfashion.presentation.app.models.payment.ProductInAPI
import com.example.arfashion.presentation.services.PaymentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel(context: Context) : ViewModel() {
    private val paymentService = PaymentService.create(context)

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

    fun getPaymentMethods() {
        paymentService.getPaymentMethods().enqueue(object :
            Callback<PaymentMethodResponse> {
            override fun onResponse(
                call: Call<PaymentMethodResponse>,
                response: Response<PaymentMethodResponse>
            ) {
                _getPaymentMethodsResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultGetPaymentMethods.value = true
                    else -> _resultGetPaymentMethods.value = false
                }
            }

            override fun onFailure(call: Call<PaymentMethodResponse>, t: Throwable) {
                _resultGetPaymentMethods.value = false
            }
        })
    }

    fun addBill(totalProduct: Int, address: String, payment: String, totalCost: Int, products: MutableList<ProductInAPI>) {
        paymentService.addBill(totalProduct, address, payment, totalCost, products).enqueue(object :
            Callback<AddBillResponse> {
            override fun onResponse(
                call: Call<AddBillResponse>,
                response: Response<AddBillResponse>
            ) {
                _addBillResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultAddBill.value = true
                    else -> _resultAddBill.value = false
                }
            }

            override fun onFailure(cll: Call<AddBillResponse>, t: Throwable) {
                _resultAddBill.value = false
            }
        })
    }
}