package com.example.arfashion.presentation.app.models.bill

import com.example.arfashion.presentation.app.models.product.Sizes
import com.google.gson.annotations.SerializedName

data class GetBillListResponse (
    @SerializedName("bills") val bills : List<BillItemResponse>,
    @SerializedName("totalCurrentDocs") val totalCurrentDocs : Int,
    @SerializedName("totalDocs") val totalDocs : Int,
    @SerializedName("offset") val offset : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("totalPages") val totalPages : Int,
    @SerializedName("currentPage") val currentPage : Int
)

data class BillItemResponse (
    @SerializedName("totalCost") val totalCost : Int,
    @SerializedName("totalProduct") val totalProduct : Int,
    @SerializedName("note") val note : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("products") val products : List<ProductsInBillListResponse>,
    @SerializedName("address") val address : AddressResponse,
    @SerializedName("payment") val payment : PaymentResponse,
    @SerializedName("status") val status : BillStatusResponse,
    @SerializedName("__v") val __v : Int
)

data class AddressResponse (
    @SerializedName("name") val name : String,
    @SerializedName("phone") val phone : Int,
    @SerializedName("email") val email : String,
    @SerializedName("province") val province : ProvinceResponse,
    @SerializedName("district") val district : DistrictResponse,
    @SerializedName("village") val village : VillageResponse,
    @SerializedName("home") val home : String,
    @SerializedName("is_default") val is_default : Boolean,
    @SerializedName("_id") val _id : String,
    @SerializedName("__v") val __v : Int
)

data class ProvinceResponse (
    @SerializedName("code") val code : Int,
    @SerializedName("name") val name : String
)

data class PaymentResponse (
    @SerializedName("name") val name : String,
    @SerializedName("_id") val _id : String
)

data class BillStatusResponse (
    @SerializedName("name") val name : String,
    @SerializedName("_id") val _id : String
)

data class DistrictResponse (
    @SerializedName("code") val code : Int,
    @SerializedName("name") val name : String
)

data class VillageResponse (
    @SerializedName("code") val code : Int,
    @SerializedName("name") val name : String
)

data class ProductsInBillListResponse (
    @SerializedName("color") val color : String,
    @SerializedName("price") val price : Int,
    @SerializedName("size") val size : Sizes,
    @SerializedName("total") val total : Int,
    @SerializedName("name") val name : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("image") val image : String
)