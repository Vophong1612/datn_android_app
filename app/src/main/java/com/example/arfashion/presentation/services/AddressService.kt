package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.profile.ResultDistrictResponse
import com.example.arfashion.presentation.app.models.profile.ResultProvinceResponse
import com.example.arfashion.presentation.app.models.profile.ResultWardResponse
import retrofit2.Call
import retrofit2.http.*

interface AddressService {

    @GET("/api/province")
    fun getProvince(): Call<ResultProvinceResponse>

    @GET("/api/province/district/{province_id}")
    fun getDistrict(@Path("province_id") province_id: String): Call<ResultDistrictResponse>

    @GET("/api/province/ward/{district_id}")
    fun getWard(@Path("district_id") district_id: String): Call<ResultWardResponse>

    companion object {

        fun create(): AddressService {
            val networkProvider = AddressNetworkProvider.newInstance()
            return networkProvider.getService(AddressService::class.java)
        }
    }
}