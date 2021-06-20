package com.example.arfashion.presentation.app.presentation.bill

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.bill.GetBillListResponse
import com.example.arfashion.presentation.app.models.bill.GetBillStatusResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Bill
import com.example.arfashion.presentation.data.model.BillStatus
import com.example.arfashion.presentation.services.BillService
import com.example.arfashion.presentation.services.toBill
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillViewModel (context: Context) : ViewModel() {
    private val billService = BillService.create(context)

    private val _billStatus = MutableLiveData<ARResult<List<BillStatus>>>()
    val billStatus: LiveData<ARResult<List<BillStatus>>>
        get() = _billStatus

    private val _billList = MutableLiveData<ARResult<List<Bill>>>()
    val billList: LiveData<ARResult<List<Bill>>> = _billList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _offset = MutableLiveData<ARResult<Int>>()
    val offset : LiveData<ARResult<Int>> = _offset

    private val _currentPage = MutableLiveData(0)
    private val _totalPage = MutableLiveData(0)

    fun getBillStatus() {
        billService.getBillStatus().enqueue(object : Callback<List<GetBillStatusResponse>> {
            override fun onResponse(
                call: Call<List<GetBillStatusResponse>>,
                response: Response<List<GetBillStatusResponse>>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _billStatus.value = ARResult.Success(it.map { billStatus ->
                                BillStatus(billStatus._id, billStatus.name)
                            })
                        }
                    }
                    else -> _billStatus.value =
                        ARResult.Error(Throwable("Can not get bill status. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<GetBillStatusResponse>>, t: Throwable) {
                _billStatus.value = ARResult.Error(t)
            }
        })
    }

    fun getBillListByStatus(status: String, offset: Int) {
        if (offset != 0 && _currentPage.value!! >= _totalPage.value!!) {
            return
        }
        if (offset == 0) {
            reset()
        }
        _loading.value = true
        billService.getBillListByStatus(status, offset, 5).enqueue(object : Callback<GetBillListResponse> {
            override fun onResponse(
                call: Call<GetBillListResponse>,
                response: Response<GetBillListResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _billList.value = ARResult.Success(it.bills.map { bill ->
                                bill.toBill()
                            })
                            _offset.value = ARResult.Success(it.offset + it.limit)
                            _currentPage.value = it.currentPage
                            _totalPage.value = it.totalPages
                        }
                    }
                    else -> {
                        _billList.value =
                            ARResult.Error(Throwable("Can not get bill. Code: ${response.code()}"))
                        _offset.value = ARResult.Error(Throwable())
                    }
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<GetBillListResponse>, t: Throwable) {
                _billList.value = ARResult.Error(t)
                _loading.value = false
                _offset.value = ARResult.Error(t)
            }

        })
    }

    private fun reset() {
        _offset.value = ARResult.Success(0)
        _totalPage.value = 0
        _currentPage.value = 0
    }
}