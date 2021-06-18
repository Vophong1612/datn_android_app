package com.example.arfashion.presentation.app.presentation.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.example.arfashion.presentation.app.models.address.ResultAddressResponse
import com.example.arfashion.presentation.app.models.payment.PaymentItem
import com.example.arfashion.presentation.app.models.payment.ProductInBill
import com.example.arfashion.presentation.app.presentation.address.AddNewAddressActivity
import com.example.arfashion.presentation.app.presentation.address.AddressListActivity
import com.example.arfashion.presentation.app.presentation.address.AddressListAdapter
import com.example.arfashion.presentation.app.presentation.address.AddressViewModel
import com.example.arfashion.presentation.app.presentation.cart.CartActivity
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.AddressService
import com.example.arfashion.presentation.services.PaymentService
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_back_header.screen_name

class PaymentActivity : AppCompatActivity() {

    private lateinit var paymentViewModel: PaymentViewModel

    private lateinit var addressViewModel: AddressViewModel

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private var paymentService = PaymentService.create()

    private val addressService = AddressService.create()

    private lateinit var user: User

    private var paymentMethods: List<PaymentItem> = listOf()

    private var chosenProducts: MutableList<ProductInBill> = mutableListOf()

    private lateinit var productAdapter: PaymentProductsAdapter

    companion object{
        var receiverAddressList: List<AddressResponse> = listOf()
        var paymentName: String = ""
        var paymentEmail: String = ""
        var paymentPhone: String = ""
        var paymentAddress: String = ""
        var paymentDefault: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()
    }

    override fun onResume() {
        super.onResume()
        payment_name.text = paymentName
        payment_email.text =  paymentEmail
        payment_phone.text = paymentPhone
        payment_address.text = paymentAddress
        if(paymentDefault) receiver_default.visibility = View.VISIBLE
        else receiver_default.visibility = View.INVISIBLE
    }

    private fun init() {
        screen_name.text = this.getString(R.string.payment)
        onNavigateBack()

        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        user = userStorage.load()

        paymentViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PaymentViewModel(paymentService) as T
            }
        })[PaymentViewModel::class.java]

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

        user.credential.accessToken?.let { paymentViewModel.getPaymentMethods(it) }

        user.credential.accessToken?.let {
            addressViewModel.loadAddress(it)
        }

        iv_payment_choose.setOnClickListener {
            val intent = Intent(this, AddressListActivity::class.java)
            startActivity(intent)
        }

        setStatus(View.VISIBLE)

        val temp = CartActivity.temp

        productAdapter = PaymentProductsAdapter(this)
        with(payment_products_List) {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context)
        }
        temp.forEach { item ->
            if(item.isCartCheck){
                if (item.priceSale < item.prices) {
                    chosenProducts.add(
                        ProductInBill(
                            item.id, item.name, item.images[0], item.colors[0], item.sizes[0].name,
                            item.priceSale, item.total
                        )
                    )
                }else{
                    chosenProducts.add(
                        ProductInBill(
                            item.id, item.name, item.images[0], item.colors[0], item.sizes[0].name,
                            item.prices, item.total
                        )
                    )
                }
            }
        }
        productAdapter.setProductList(chosenProducts)
        productAdapter.notifyDataSetChanged()
    }

    private fun setStatus(display: Int) {
        loadBar.visibility = display
        v_waiting_load_payment.visibility = display
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {

        paymentViewModel.resultGetPaymentMethods.observe(this) {
            if (it) {
                val response = paymentViewModel.getPaymentMethodsResponse.value
                if (response != null) {
                    paymentMethods = response.payments
                    paymentMethods.forEach {
                        item -> Toast.makeText(this, item.name, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        addressViewModel.resultLoadAddress.observe(this) {
            if (it) {
                val response = addressViewModel.loadAddressResponse.value
                if (response != null) {
                    receiverAddressList = response.results
                }
                response?.results?.forEach {
                    run {
                        if (it.isDefault) {
                            payment_name.text = it.name
                            payment_email.text = it.email
                            payment_phone.text = it.phone
                            payment_address.text =
                                it.home + ", " + it.village.name + ", " + it.district.name + ", " + it.province.name
                            payment_name.visibility = View.VISIBLE
                            payment_email.visibility = View.VISIBLE
                            payment_phone.visibility = View.VISIBLE
                            payment_address.visibility = View.VISIBLE
                            receiver_default.visibility = View.VISIBLE
                        }
                    }
                }
                setStatus(View.GONE)
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