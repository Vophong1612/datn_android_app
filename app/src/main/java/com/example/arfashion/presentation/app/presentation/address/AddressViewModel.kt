package com.example.arfashion.presentation.app.presentation.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.address.ResultAddressResponse
import com.example.arfashion.presentation.app.models.address.ResultDistrictResponse
import com.example.arfashion.presentation.app.models.address.ResultProvinceResponse
import com.example.arfashion.presentation.app.models.address.ResultWardResponse
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.profile.ProfileResponse
import com.example.arfashion.presentation.services.AddressService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressViewModel(
    private val addressService: AddressService
) : ViewModel() {

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

    private val _loadAddressResponse = MutableLiveData<ResultAddressResponse>()
    val loadAddressResponse: LiveData<ResultAddressResponse>
        get() = _loadAddressResponse

    fun chooseProvince(token: String) {
        addressService.getProvince("Bearer $token").enqueue(object : Callback<ResultProvinceResponse> {
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

    fun chooseDistrict(token: String, id: String) {
        addressService.getDistrict("Bearer $token", id.toInt()).enqueue(object : Callback<ResultDistrictResponse> {
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

    fun chooseVillage(token: String, id: String) {
        addressService.getWard("Bearer $token", id.toInt()).enqueue(object : Callback<ResultWardResponse> {
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

    fun addAddress(token: String, name: String, email: String, phone: String,
                        home: String, village: Int, district: Int, province: Int) {
        addressService.addAddress("Bearer $token", name, email, phone, home, village, district, province)
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
            }

            override fun onFailure(call: Call<ResultAddressResponse>, t: Throwable) {
                _resultAddAddress.value = false
            }
        })
    }

    fun updateAddress(token: String, id: String, name: String, email: String, phone: String,
                      is_default: Boolean, home: String, village: Int, district: Int, province: Int) {
        addressService.updateAddress("Bearer $token", id, name, email, phone, is_default, home, village, district, province)
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
                }

                override fun onFailure(call: Call<ResultAddressResponse>, t: Throwable) {
                    _resultUpdateAddress.value = false
                }
            })
    }

    fun deleteAddress(token: String, id: String) {
        addressService.deleteAddress("Bearer $token", id)
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

    fun loadAddress(token: String) {
        addressService.loadAddress("Bearer $token")
            .enqueue(object : Callback<ResultAddressResponse> {
                override fun onResponse(
                    call: Call<ResultAddressResponse>,
                    response: Response<ResultAddressResponse>
                ) {
                    _loadAddressResponse.value = response.body()
                    when (response.code()) {
                        200 -> _resultLoadAddress.value = response.isSuccessful
                        else -> _resultLoadAddress.value = false
                    }
                }

                override fun onFailure(call: Call<ResultAddressResponse>, t: Throwable) {
                    _resultLoadAddress.value = false
                }
            })
    }
}