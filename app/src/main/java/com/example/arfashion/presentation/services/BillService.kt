package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.bill.GetBillListResponse
import com.example.arfashion.presentation.app.models.bill.GetBillStatusResponse
import com.example.arfashion.presentation.app.presentation.bill.BillItemOffset
import retrofit2.Call
import retrofit2.http.*

interface BillService {
    companion object {
        fun create(): BillService {
            val networkProvider = NetworkProvider.newInstance()
            return networkProvider.getService(BillService::class.java)
        }
    }

    @GET("/bills/billStatus/list")
    fun getBillStatus(): Call<List<GetBillStatusResponse>>

    @GET("/users/bill/list")
    fun getBillListByStatus(
        @Query("status") status: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int?
    ): Call<GetBillListResponse>
}