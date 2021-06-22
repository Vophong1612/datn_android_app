package com.example.arfashion.presentation.app.presentation.address

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.presentation.payment.PaymentActivity
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.layout_back_header.back_icon
import kotlinx.android.synthetic.main.layout_back_header.screen_name

class AddressListActivity : AppCompatActivity() {

    companion object {
        lateinit var addressViewModel: AddressViewModel
    }

    private lateinit var addressAdapter: AddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        init()
    }

    override fun onResume() {
        super.onResume()
        setStatus(View.VISIBLE)
        addressViewModel.loadAddress()
    }

    private fun setStatus(display: Int) {
        loadListBar.visibility = display
        v_waiting_load_list.visibility = display
    }

    private fun init() {
        screen_name.text = this.getString(R.string.select_address)
        onNavigateBack()

        addressViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(applicationContext)
        ).get(AddressViewModel::class.java)

        initView()
        initViewModel()
    }

    private fun initView() {

        addressAdapter = AddressListAdapter(this)
        with(rvAddressList) {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(context)
        }

        addressAdapter.setAddressList(PaymentActivity.receiverAddressList)
        addressAdapter.notifyDataSetChanged()

        iv_add_address.setOnClickListener {
            val intent = Intent(this, AddNewAddressActivity::class.java)
            intent.putExtra("mode", "add")
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        addressViewModel.resultDeleteAddress.observe(this) {
            if (it) {
                val response = addressViewModel.deleteAddressResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        addressViewModel.resultLoadAddress.observe(this) {
            if (it) {
                val response = addressViewModel.loadAddressResponse.value
                if (response != null) {
                    PaymentActivity.receiverAddressList.toMutableList().clear()
                    addressAdapter.setAddressList(response.results.toMutableList().reversed())
                    setStatus(View.GONE)
                }

            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}