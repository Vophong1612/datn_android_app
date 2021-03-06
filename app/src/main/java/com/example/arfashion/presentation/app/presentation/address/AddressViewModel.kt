package com.example.arfashion.presentation.app.presentation.address

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.address.*
import com.example.arfashion.presentation.services.AddressService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressViewModel(context: Context) : ViewModel() {
    private val addressService = AddressService.create(context)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _resultChooseProvince = MutableLiveData<Boolean>()
    val resultChooseProvince: LiveData<Boolean>
        get() = _resultChooseProvince

    private val _chooseProvinceResponse = MutableLiveData<ResultProvinceResponse>()
    val chooseProvinceResponse: LiveData<ResultProvinceResponse>
        get() = _chooseProvinceResponse

    private val _resultChooseDistrict = MutableLiveData<Boolean>()
    val resultChooseDistrict: LiveData<Boolean>
        get() = _resultChooseDistrict

    private val _chooseDistrictResponse = MutableLiveData<ResultDistrictResponse>()
    val chooseDistrictResponse: LiveData<ResultDistrictResponse>
        get() = _chooseDistrictResponse

    private val _resultChooseVillage = MutableLiveData<Boolean>()
    val resultChooseVillage : LiveData<Boolean>
        get() = _resultChooseVillage

    private val _chooseVillageResponse = MutableLiveData<ResultWardResponse>()
    val chooseVillageResponse: LiveData<ResultWardResponse>
        get() = _chooseVillageResponse

    private val _resultAddAddress = MutableLiveData<Boolean>()
    val resultAddAddress : LiveData<Boolean>
        get() = _resultAddAddress

    private val _addAddressResponse = MutableLiveData<ResultAddressResponse>()
    val addAddressResponse: LiveData<ResultAddressResponse>
        get() = _addAddressResponse

    private val _resultUpdateAddress = MutableLiveData<Boolean>()
    val resultUpdateAddress : LiveData<Boolean>
        get() = _resultUpdateAddress

    private val _updateAddressResponse = MutableLiveData<ResultAddressResponse>()
    val updateAddressResponse: LiveData<ResultAddressResponse>
        get() = _updateAddressResponse

    private val _resultDeleteAddress = MutableLiveData<Boolean>()
    val resultDeleteAddress : LiveData<Boolean>
        get() = _resultDeleteAddress

    private val _deleteAddressResponse = MutableLiveData<ResultAddressResponse>()
    val deleteAddressResponse: LiveData<ResultAddressResponse>
        get() = _deleteAddressResponse

    private val _resultLoadAddress = MutableLiveData<Boolean>()
    val resultLoadAddress : LiveData<Boolean>
        get() = _resultLoadAddress

    private val _loadAddressResponse = MutableLiveData<LoadAddressListResponse>()
    val loadAddressResponse: LiveData<LoadAddressListResponse>
        get() = _loadAddressResponse

    fun chooseProvince() {
        addressService.getProvince().enqueue(object : Callback<ResultProvinceResponse> {
            override fun onResponse(
                call: Call<ResultProvinceResponse>,
                response: Response<ResultProvinceResponse>
            ) {
                _chooseProvinceResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultChooseProvince.value = response.isSuccessful
                    else -> _resultChooseProvince.value = false
                }
            }

            override fun onFailure(call: Call<ResultProvinceResponse>, t: Throwable) {
                _resultChooseProvince.value = false
            }
        })
    }

    fun chooseDistrict(id: String) {
        addressService.getDistrict(id.toInt()).enqueue(object : Callback<ResultDistrictResponse> {
            override fun onResponse(
                call: Call<ResultDistrictResponse>,
                response: Response<ResultDistrictResponse>
            ) {
                _chooseDistrictResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultChooseDistrict.value = response.isSuccessful
                    else -> _resultChooseDistrict.value = false
                }
            }

            override fun onFailure(call: Call<ResultDistrictResponse>, t: Throwable) {
                _resultChooseDistrict.value = false
            }
        })
    }

    fun chooseVillage(id: String) {
        addressService.getWard(id.toInt()).enqueue(object : Callback<ResultWardResponse> {
            override fun onResponse(
                call: Call<ResultWardResponse>,
                response: Response<ResultWardResponse>
            ) {
                _chooseVillageResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultChooseVillage.value = response.isSuccessful
                    else -> _resultChooseVillage.value = false
                }
            }

            override fun onFailure(call: Call<ResultWardResponse>, t: Throwable) {
                _resultChooseVillage.value = false
            }
        })
    }

    fun addAddress(name: String, email: String, phone: String,
                        home: String, village: Int, district: Int, province: Int) {
        _loading.value = true
        addressService.addAddress(name, email, phone, home, village, district, province)
            .enqueue(object : Callback<ResultAddressResponse> {
            override fun onResponse(
                call: Call<ResultAddressResponse>,
                response: Response<ResultAddressResponse>
            ) {
                _addAddressResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultAddAddress.value = response.isSuccessful
                    else -> _resultAddAddress.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<ResultAddressResponse>, t: Throwable) {
                _resultAddAddress.value = false
                _loading.value = false
            }
        })
    }

    fun updateAddress(id: String, name: String, email: String, phone: String,
                      is_default: Boolean, home: String, village: Int, district: Int, province: Int) {
        _loading.value = true
        addressService.updateAddress(id, name, email, phone, is_default, home, village, district, province)
            .enqueue(object : Callback<ResultAddressResponse> {
                override fun onResponse(
                    call: Call<ResultAddressResponse>,
                    response: Response<ResultAddressResponse>
                ) {
                    _updateAddressResponse.value = response.body()
                    when (response.code()) {
                        200 -> _resultUpdateAddress.value = response.isSuccessful
                        else -> _resultUpdateAddress.value = false
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<ResultAddressResponse>, t: Throwable) {
                    _resultUpdateAddress.value = false
                    _loading.value = false
                }
            })
    }

    fun deleteAddress(id: String) {
        addressService.deleteAddress(id)
            .enqueue(object : Callback<ResultAddressResponse> {
                override fun onResponse(
                    call: Call<ResultAddressResponse>,
                    response: Response<ResultAddressResponse>
                ) {
                    _deleteAddressResponse.value = response.body()
                    when (response.code()) {
                        200 -> _resultDeleteAddress.value = response.isSuccessful
                        else -> _resultDeleteAddress.value = false
                    }
                }

                override fun onFailure(call: Call<ResultAddressResponse>, t: Throwable) {
                    _resultDeleteAddress.value = false
                }
            })
    }

    fun loadAddress() {
        addressService.loadAddress()
            .enqueue(object : Callback<LoadAddressListResponse> {
                override fun onResponse(
                    call: Call<LoadAddressListResponse>,
                    response: Response<LoadAddressListResponse>
                ) {
                    _loadAddressResponse.value = response.body()
                    when (response.code()) {
                        200 -> _resultLoadAddress.value = response.isSuccessful
                        else -> _resultLoadAddress.value = false
                    }
                }

                override fun onFailure(call: Call<LoadAddressListResponse>, t: Throwable) {
                    _resultLoadAddress.value = false
                }
            })
    }
}