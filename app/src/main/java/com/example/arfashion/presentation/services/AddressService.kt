package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.address.ResultAddressResponse
import com.example.arfashion.presentation.app.models.address.ResultDistrictResponse
import com.example.arfashion.presentation.app.models.address.ResultProvinceResponse
import com.example.arfashion.presentation.app.models.address.ResultWardResponse
import com.example.arfashion.presentation.app.models.profile.ProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface AddressService {

    @GET("/address/province")
    fun getProvince(@Header("Authorization") token: String): Call<ResultProvinceResponse>

    @GET("/address/district")
    fun getDistrict(@Header("Authorization") token: String, @Query("provinceId") province_id: Int): Call<ResultDistrictResponse>

    @GET("/address/village")
    fun getWard(@Header("Authorization") token: String, @Query("districtId") district_id: Int): Call<ResultWardResponse>

    @POST("/users/address/add")
    @FormUrlEncoded
    fun addAddress(@Header("Authorization") token: String, @Field("name") name: String,
                   @Field("email") email: String, @Field("phone") phone: String,
                   @Field("home") home: String, @Field("village") village: Int,
                   @Field("district") district: Int, @Field("province") province: Int)
    : Call<ResultAddressResponse>

    @POST("/users/address/update")
    @FormUrlEncoded
    fun updateAddress(@Header("Authorization") token: String, @Field("_id") _id: String,
                      @Field("name") name: String, @Field("email") email: String,
                      @Field("phone") phone: String, @Field("is_default") is_default: Boolean,
                      @Field("home") home: String, @Field("village") village: Int,
                      @Field("district") district: Int, @Field("province") province: Int): Call<ResultAddressResponse>

    @POST("/users/address/delete")
    @FormUrlEncoded
    fun deleteAddress(@Header("Authorization") token: String, @Field("_id") _id: String,): Call<ResultAddressResponse>

    @GET("/users/profile")
    fun loadAddress(@Header("Authorization") token: String): Call<ProfileResponse>

    companion object {
        fun create(): AddressService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(AddressService::class.java)
        }
    }
}