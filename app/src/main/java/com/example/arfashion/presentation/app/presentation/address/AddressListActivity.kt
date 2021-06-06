package com.example.arfashion.presentation.app.presentation.address

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.example.arfashion.presentation.services.AddressService
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.layout_back_header.back_icon
import kotlinx.android.synthetic.main.layout_back_header.screen_name

class AddressListActivity : AppCompatActivity() {

    private lateinit var addressViewModel: AddressViewModel

    private val addressService = AddressService.create()

    private lateinit var addressAdapter: AddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        init()

    }

    private fun init() {
        screen_name.text = this.getString(R.string.select_address)
        onNavigateBack()

        addressViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AddressViewModel(addressService) as T
            }
        })[AddressViewModel::class.java]

        initView()
        initViewModel()
    }

    private fun initView() {

        addressAdapter = AddressListAdapter(this)
        with(rvAddressList) {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(context)
        }

//        val obj: AddressResponse = AddressResponse("1","Huỳnh Phúc","abc@gmail.com",
//        "0335160976","Phạm thế hiển", "Phường 3", "Quận 8", "TP HCM")
//        val list = listOf<AddressResponse>(obj)
//        addressAdapter.setAddressList(list)

        iv_add_address.setOnClickListener {
            val intent = Intent(this, AddNewAddressActivity::class.java)
            intent.putExtra("mode","add")
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