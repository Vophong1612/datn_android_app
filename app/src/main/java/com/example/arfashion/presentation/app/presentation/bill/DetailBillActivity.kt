package com.example.arfashion.presentation.app.presentation.bill

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Bill
import com.example.arfashion.presentation.services.Utils.Companion.standardFormat
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_bill.*
import kotlinx.android.synthetic.main.layout_back_header.*

class DetailBillActivity : AppCompatActivity() {

    private lateinit var bill: Bill

    private lateinit var productAdapter: ProductsInBillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bill)
        init()
    }

    private fun init(){
        var jsonMyObject = ""
        val extras = intent.extras
        if (extras != null) {
            jsonMyObject = extras.getString("detailBill").toString()
        }
        bill = Gson().fromJson(jsonMyObject, Bill::class.java)

        screen_name.text = this.getString(R.string.detail_bill_lbl)
        onNavigateBack()

        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        detail_bill_receiver_name.text = bill.address.name
        detail_bill_receiver_email.text = bill.address.email
        detail_bill_receiver_phone.text = bill.address.phone.toString()
        detail_bill_receiver_address.text = bill.address.home + ", " +
                bill.address.village.name + ", " +
                bill.address.district.name + ", " +
                bill.address.province.name
        detail_bill_method.text = bill.payment.name
        detail_bill_money_order.text = detail_bill_money_order.context.getString(R.string.price, bill.totalCost.standardFormat())
        detail_bill_money_cost.text = detail_bill_money_order.context.getString(R.string.price, bill.totalCost.standardFormat())

        productAdapter = ProductsInBillAdapter((this))
        with(detail_bill_products_List){
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context)
        }
        productAdapter.setProductList(bill.products)
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }
}