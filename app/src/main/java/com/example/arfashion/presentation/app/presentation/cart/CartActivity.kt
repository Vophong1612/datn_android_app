package com.example.arfashion.presentation.app.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.openProductDetailActivity
import com.example.arfashion.presentation.app.presentation.payment.PaymentActivity
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Cart
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.Utils.Companion.standardFormat
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var cartViewModel: CartViewModel

    private lateinit var cartProductAdapter: CartProductAdapter

    private var isChosen: Boolean = false

    companion object{
        var temp: List<Product> = listOf()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(CartViewModel::class.java)

        initView()

        initData()
    }

    private fun initData() {
        cartViewModel.cart.observe(this, {
            when (it) {
                is ARResult.Success -> {
                    handelData(it.data)
                }
                is ARResult.Error -> {
                    if (it.throwable.message != null) {
                        handleShowMessage(it.throwable.message!!)
                    } else {
                        handleShowMessage("Can not get cart !")
                    }
                }
            }
        })
        cartViewModel.loading.observe(this, {
            refreshLayout.isRefreshing = it
        })
        cartViewModel.totalPrice.observe(this, {
            totalPrices.text = generateTotalPrice(it)
        })
        cartViewModel.updateCart.observe(this, {
            when (it) {
                is ARResult.Success -> {
                    cartViewModel.getCart()
                }
                is ARResult.Error -> {
                }
            }
        })
        cartViewModel.removeProduct.observe(this, {
            when (it) {
                is ARResult.Success -> {
                    cartViewModel.getCart()
                }
                is ARResult.Error -> {
                }
            }
        })
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initView() {
        initHeader()

        cartProductAdapter = CartProductAdapter()
        cartViewModel.getCart()

        with(cartList) {
            adapter = cartProductAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        cartProductAdapter.selectCbClickEvent = { product, isChecked ->
            if (isChecked) {
                cartViewModel.updateTotalPrice(product.priceSale * product.total)
            } else {
                cartViewModel.updateTotalPrice(-product.priceSale * product.total)
            }
            product.isCartCheck = isChecked
        }
        cartProductAdapter.deleteProductClickEvent = { product, position ->
            showDeleteProductAlertDialog(product, position)
        }
        lifecycleScope.launch {
            callbackFlow {
                cartProductAdapter.productCountClickEvent = { product, total ->
                    offer(
                        Product(
                            id = product.id,
                            sizes = product.sizes,
                            total = total,
                            priceSale = product.priceSale,
                            colors = product.colors
                        )
                    )
                }
                awaitClose { cancel() }
            }.debounce(1000)
                .collect {
                    cartViewModel.updateCart(
                        it.id,
                        it.sizes[0].id,
                        it.colors[0],
                        it.priceSale,
                        it.total
                    )
                }
        }
        selectAllCb.setOnCheckedChangeListener { _, isChecked ->
            cartProductAdapter.selectAll(isChecked)
        }

        refreshLayout.setOnRefreshListener {
            cartViewModel.getCart()
        }

        buyNowBtn.setOnClickListener {
           if(isChosen){
               val intent = Intent(this, PaymentActivity::class.java)
               temp = cartProductAdapter.getData()
               startActivity(intent)
           }else Toast.makeText(this, getString(R.string.alert_must_be_choose), Toast.LENGTH_SHORT).show()
        }

        cartProductAdapter.openProductDetailListener = {
            openProductDetailActivity(it)
        }
    }

    private fun generateTotalPrice(price: Int): CharSequence {
        val totalPriceText = applicationContext.getString(
            R.string.total_prices,
            price.standardFormat()
        )

        val priceText = "${price.standardFormat()} VND"
        val messageSpan = SpannableString(totalPriceText)

        val priceStartIndex = messageSpan.indexOf(priceText)
        val priceEndIndex = priceStartIndex + priceText.length
        messageSpan.setSpan(
            ForegroundColorSpan(getColor(R.color.palattes_2)),
            priceStartIndex,
            priceEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        isChosen = price > 0
        return messageSpan
    }

    private fun initHeader() {
        screen_name.text = this.getString(R.string.cart_screen_name)
        back_icon.setOnClickListener {
            finish()
        }
    }

    private fun handelData(data: Cart) {
        if (data.product.isNotEmpty()) {
            selectAllCb.visible()
            buyNowBtn.isEnabled = true
            showCartList()
            cartProductAdapter.setData(data.product)
        } else {
            selectAllCb.gone()
            buyNowBtn.isEnabled = false
            totalPrices.text = generateTotalPrice(0)
            handleShowMessage(applicationContext.getString(R.string.cart_is_empty))
        }
    }

    private fun handleShowMessage(message: String) {
        showCartAlert()
        cartAlert.text = message
    }

    private fun showCartList() {
        cartList.visible()
        cartAlert.gone()
    }

    private fun showCartAlert() {
        cartAlert.visible()
        cartList.gone()
    }

    private fun showDeleteProductAlertDialog(product: Product, position: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.myDialog))
            .setTitle("Delete Product Alert")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton(
                applicationContext.getString(R.string.delete_alert_yes)
            ) { _, _ ->
                cartViewModel.removeProduct(product.id, product.sizes[0].id, product.colors[0])
                cartProductAdapter.deleteProduct(position)
            }
            .setNegativeButton(applicationContext.getString(R.string.delete_alert_cancel), null)
            .setIcon(android.R.drawable.ic_dialog_alert)

        val dialog = builder.create()
        dialog.show()
    }
}