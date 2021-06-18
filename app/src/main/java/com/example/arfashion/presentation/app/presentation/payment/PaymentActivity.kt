package com.example.arfashion.presentation.app.presentation.payment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.example.arfashion.presentation.app.models.address.ResultAddressResponse
import com.example.arfashion.presentation.app.models.payment.PaymentItem
import com.example.arfashion.presentation.app.models.payment.ProductInAPI
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
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_back_header.screen_name
import kotlinx.android.synthetic.main.layout_choose_payment_method.*

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

    private var postAPIProducts: MutableList<ProductInAPI> = mutableListOf()

    private lateinit var productAdapter: PaymentProductsAdapter

    private var totalMoney: Int = 0

    private var methodName: String = ""

    private var methodId: String = ""

    companion object{
        var receiverAddressList: List<AddressResponse> = listOf()
        var paymentName: String = ""
        var paymentEmail: String = ""
        var paymentPhone: String = ""
        var paymentAddress: String = ""
        var paymentId: String = ""
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

    @SuppressLint("SetTextI18n")
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

        getProducts()

        payment_money_order.text = Utils.formatPrice(totalMoney) + "đ"

        payment_money_cost.text = payment_money_order.text

        iv_payment_total_choose.setOnClickListener {
            showChoosePaymentMethodDialog()
        }

        tv_purchase.setOnClickListener {

            if(methodName.isEmpty()){
                Toast.makeText(this, getString(R.string.alert_must_be_choose_method), Toast.LENGTH_SHORT).show()
            } else if(payment_name.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.alert_must_be_choose_address), Toast.LENGTH_SHORT).show()
            } else{
                user.credential.accessToken?.let {
                        it1 -> paymentViewModel.addBill(it1, chosenProducts.size, paymentId,
                methodId, totalMoney, postAPIProducts)
                }
            }
        }
    }

    private fun showChoosePaymentMethodDialog() {

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.layout_choose_payment_method, null)
        var recyclerViewList: RecyclerView? = messageBoxView.findViewById(R.id.rl_payment_method)
        val cancelBtn: View? = messageBoxView.findViewById(R.id.layout_cancel)
        val acceptBtn: View? = messageBoxView.findViewById(R.id.layout_accept)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)

        //show dialog
        val messageBoxInstance = messageBoxBuilder.show()

        //set Listener
        messageBoxView.setOnClickListener {
            //close dialog
            messageBoxInstance.dismiss()
        }

        //setting text values
        val methodAdapter = PaymentMethodsAdapter(this)
        with(recyclerViewList){
            this?.adapter = methodAdapter
            this?.layoutManager = LinearLayoutManager(this!!.context)
        }
        methodAdapter.setMethodList(paymentMethods)
        methodAdapter.selectCbClickEvent = { product, isChecked ->
            product.isChosen = isChecked
        }

        cancelBtn?.setOnClickListener {
            messageBoxInstance.dismiss()
        }

        acceptBtn?.setOnClickListener {
            methodId = methodAdapter.getCheckedItemId()
            methodName = methodAdapter.getCheckedItem()
            if(methodName.isEmpty())
                Toast.makeText(this, getString(R.string.alert_must_be_choose_method), Toast.LENGTH_SHORT).show()
            else{
                messageBoxInstance.dismiss()
                payment_method.text = methodName
                iv_payment_total_choose.visibility = View.GONE
            }
        }

    }

    private fun getProducts() {

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
                    postAPIProducts.add(
                        ProductInAPI(
                            item.id, item.colors[0], item.sizes[0].id, item.priceSale, item.total
                        )
                    )
                    totalMoney += item.priceSale * item.total
                }else{
                    chosenProducts.add(
                        ProductInBill(
                            item.id, item.name, item.images[0], item.colors[0], item.sizes[0].name,
                            item.prices, item.total
                        )
                    )
                    postAPIProducts.add(
                        ProductInAPI(
                            item.id, item.colors[0], item.sizes[0].id, item.prices, item.total
                        )
                    )
                    totalMoney += item.prices * item.total
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

        paymentViewModel.resultAddBill.observe(this) {
            if (it) {
                val response = paymentViewModel.addBillResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        paymentViewModel.resultGetPaymentMethods.observe(this) {
            if (it) {
                val response = paymentViewModel.getPaymentMethodsResponse.value
                if (response != null) {
                    paymentMethods = response.payments
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
                    item ->
                        if (item.isDefault) {
                            setStatus(View.GONE)
                            paymentId = item._id
                            payment_name.text = item.name
                            payment_email.text = item.email
                            payment_phone.text = item.phone
                            payment_address.text =
                                item.home + ", " + item.village.name + ", " + item.district.name + ", " + item.province.name
                            payment_name.visibility = View.VISIBLE
                            payment_email.visibility = View.VISIBLE
                            payment_phone.visibility = View.VISIBLE
                            payment_address.visibility = View.VISIBLE
                            receiver_default.visibility = View.VISIBLE
                        }
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