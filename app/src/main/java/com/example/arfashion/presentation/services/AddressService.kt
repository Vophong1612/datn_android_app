package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.address.ResultAddressResponse
import com.example.arfashion.presentation.app.models.address.ResultDistrictResponse
import com.example.arfashion.presentation.app.models.address.ResultProvinceResponse
import com.example.arfashion.presentation.app.models.address.ResultWardResponse
import retrofit2.Call
import retrofit2.http.*

interface AddressService {

    @GET("/address/province")
    fun getProvince(@Header("Authorization") token: String): Call<ResultProvinceResponse>

    @GET("/address/district/{provinceId}")
    fun getDistrict(@Header("Authorization") token: String, @Path("provinceId") province_id: String): Call<ResultDistrictResponse>

    @GET("/address/village/{districtId}")
    fun getWard(@Header("Authorization") token: String, @Path("districtId") district_id: String): Call<ResultWardResponse>

    @POST("/users/address/add")
    fun addAddress(@Header("Authorization") token: String, @Field("name") name: String,
                   @Field("email") email: String, @Field("phone") phone: String,
                   @Field("home") home: String, @Field("village") village: String,
                   @Field("district") district: String, @Field("province") province: String)
    : Call<ResultAddressResponse>

    @POST("/users/address/update")
    fun updateAddress(@Header("Authorization") token: String, @Field("_id") _id: String,
                      @Field("name") name: String, @Field("email") email: String,
                      @Field("phone") phone: String, @Field("is_default") is_default: Boolean,
                      @Field("home") home: String, @Field("village") village: String,
                      @Field("district") district: String, @Field("province") province: String): Call<ResultAddressResponse>

    @POST("/users/address/delete")
    fun deleteAddress(@Header("Authorization") token: String, @Field("_id") _id: String,)
    : Call<ResultAddressResponse>

    companion object {
        fun create(): AddressService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(AddressService::class.java)
        }
    }
}