package com.example.arfashion.presentation.services

import android.content.Context
import com.example.arfashion.presentation.app.models.address.*
import com.example.arfashion.presentation.app.models.profile.ProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface AddressService {

    @GET("/address/province")
    fun getProvince(): Call<ResultProvinceResponse>

    @GET("/address/district")
    fun getDistrict(@Query("provinceId") province_id: Int): Call<ResultDistrictResponse>

    @GET("/address/village")
    fun getWard(@Query("districtId") district_id: Int): Call<ResultWardResponse>

    @POST("/users/address/add")
    @FormUrlEncoded
    fun addAddress(@Field("name") name: String,
                   @Field("email") email: String, @Field("phone") phone: String,
                   @Field("home") home: String, @Field("village") village: Int,
                   @Field("district") district: Int, @Field("province") province: Int)
    : Call<ResultAddressResponse>

    @POST("/users/address/update")
    @FormUrlEncoded
    fun updateAddress(@Field("_id") _id: String,
                      @Field("name") name: String, @Field("email") email: String,
                      @Field("phone") phone: String, @Field("is_default") is_default: Boolean,
                      @Field("home") home: String, @Field("village") village: Int,
                      @Field("district") district: Int, @Field("province") province: Int): Call<ResultAddressResponse>

    @POST("/users/address/delete")
    @FormUrlEncoded
    fun deleteAddress(@Field("_id") _id: String,): Call<ResultAddressResponse>

    @GET("/users/address/list")
    fun loadAddress(): Call<LoadAddressListResponse>

    companion object {
        fun create(context: Context): AddressService {
            val networkProvider = NetworkProvider.newInstance(context)
            return networkProvider.getService(AddressService::class.java)
        }
    }
}