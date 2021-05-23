package com.example.arfashion.presentation.app.presentation.cart

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.ContextThemeWrapper
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Cart
import com.example.arfashion.presentation.data.model.Product
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

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var cartProductAdapter: CartProductAdapter

    init {
        lifecycleScope.launchWhenCreated {
            cartViewModel.getCart()
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartProductAdapter = CartProductAdapter()

        initView()

        initData()
    }

    private fun initData() {
//        val cart = Cart(
//            id = "609d41147ff9a80015ac06ff",
//            product = listOf(
//                Product(
//                    id = "605a2714928cf217986aad98",
//                    name = "T-Shirt 4 Yeah",
//                    priceSale = 200000,
//                    prices = 300000,
//                    total = 1,
//                    sizes = listOf(Size("6058a53dc84007ba500dc13f", "S")),
//                    images = listOf("https://res.cloudinary.com/ar-fashion/image/upload/v1619184696/ar/products/605a221c928cf217986aad8d/main.jpg")
//                ),
//                Product(
//                    id = "605a2742928cf217986aad99",
//                    name = "T-Shirt 5 Yeah",
//                    priceSale = 200000,
//                    prices = 300000,
//                    total = 100,
//                    sizes = listOf(Size("6058a56dc84007ba500dc140", "XL")),
//                    images = listOf("https://res.cloudinary.com/ar-fashion/image/upload/v1619184837/ar/products/605a2742928cf217986aad99/000074_1_jkakuo.jpg")
//                )
//            )
//        )
////        handelData(cart)
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
                            priceSale = product.priceSale
                        )
                    )
                }
                awaitClose { cancel() }
            }.debounce(1000)
                .collect {
                    cartViewModel.updateCart(
                        it.id,
                        it.sizes[0].id,
                        "Yellow",
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
    }

    private fun generateTotalPrice(price: Int): CharSequence {
        val totalPriceText = applicationContext.getString(
            R.string.total_prices,
            price
        )

        val priceText = "$price VND"
        val messageSpan = SpannableString(totalPriceText)

        val priceStartIndex = messageSpan.indexOf(priceText)
        val priceEndIndex = priceStartIndex + priceText.length
        messageSpan.setSpan(
            ForegroundColorSpan(
                applicationContext.getColor(R.color.palattes_2)
            ),
            priceStartIndex,
            priceEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
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
                cartViewModel.removeProduct(product.id, product.sizes[0].id, "Yellow")
                cartProductAdapter.deleteProduct(position)
            }
            .setNegativeButton(applicationContext.getString(R.string.delete_alert_cancel), null)
            .setIcon(android.R.drawable.ic_dialog_alert)

        val dialog = builder.create()
        dialog.show()
    }
}