package com.example.arfashion.presentation.app.presentation.product.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.openProductDetailActivity
import com.example.arfashion.presentation.app.presentation.cart.CartViewModel
import com.example.arfashion.presentation.app.presentation.main.ui.categories.KEY_PRODUCT_ID
import com.example.arfashion.presentation.app.presentation.product.ProductAdapter
import com.example.arfashion.presentation.app.presentation.product.test.ARTestActivity
import com.example.arfashion.presentation.app.presentation.product.test.KEY_PRODUCT_COLOR
import com.example.arfashion.presentation.app.presentation.product.test.KEY_PRODUCT_IMAGE
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Product
import kotlinx.android.synthetic.main.activity_login.refreshLayout
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productTabAdapter: ProductTabAdapter

    private val productDetailViewModel: ProductDetailViewModel by viewModels()

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var thumbnailAdapter: ThumbnailAdapter

    private lateinit var sizeAdapter: ProductSizeAdapter

    private lateinit var relatedProductAdapter: ProductAdapter

    private var product: Product? = null

    private var productId: String? = ""

    init {
        lifecycleScope.launchWhenCreated {
            productId = intent.extras?.getString(KEY_PRODUCT_ID)
            productId?.let {
                productDetailViewModel.getProductDetail(it)
                productDetailViewModel.getRelatedProduct(it)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        initData()

        initView()

    }

    private fun initData() {
        thumbnailAdapter = ThumbnailAdapter()
        thumbnailAdapter.thumbnailClickListener = {
            product?.images?.get(it)?.let { image ->
                Glide.with(mainImage)
                    .load(image)
                    .into(mainImage)
            }
        }
        with(thumbnailList) {
            adapter = thumbnailAdapter
            layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(ThumbnailItemOffset(applicationContext))
        }
        sizeAdapter = ProductSizeAdapter()
        with(sizeList) {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(ThumbnailItemOffset(applicationContext))
        }
        relatedProductAdapter = ProductAdapter(applicationContext, false)
        with(relatedProductList) {
            adapter = relatedProductAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RelatedProductItemOffset(applicationContext))
        }

        refreshLayout.setOnRefreshListener {
            productId?.let {
                productDetailViewModel.getProductDetail(it)
            }
        }

        productDetailViewModel.product.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    this.product = it.data
                    handleData(it.data)
                }
                is ARResult.Error -> {
                    Toast.makeText(this, "Can not read product !", Toast.LENGTH_SHORT).show()
                }
            }
        }
        productDetailViewModel.loading.observe(this) {
            refreshLayout.isRefreshing = it
        }
        productDetailViewModel.relatedProduct.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    handleRelatedData(it.data)
                }
                is ARResult.Error -> {
                    relatedProductList.gone()
                    noRelatedProductAlert.visible()
                }
            }
        }
        cartViewModel.updateCart.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    Toast.makeText(this, "Added !", Toast.LENGTH_SHORT).show()
                }
                is ARResult.Error -> {
                    Toast.makeText(this, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleRelatedData(data: List<Product>) {
        if (data.isEmpty()) {
            relatedProductList.gone()
            noRelatedProductAlert.visible()
            return
        }
        relatedProductAdapter.setProducts(data)
    }

    private fun handleData(product: Product) {
        Glide.with(applicationContext)
            .load(product.images[0])
            .error(R.drawable.img_default_category)
            .placeholder(R.drawable.img_default_category)
            .into(mainImage)

        if (product.thumbnail.isNotEmpty()) {
            thumbnailAdapter.setData(product.thumbnail)
        }

        category.text = product.tag[0]

        productName.text = product.name

        productRating.rating = product.star
        totalRating.text = applicationContext.getString(
            R.string.total_rating,
            product.comments.size
        )

        productCount.maxValue = product.total

        if (product.isSale) {
            productPrice.text = applicationContext.getString(
                R.string.price,
                product.priceSale
            )
            defaultPrice.visible()
            defaultPrice.text = HtmlCompat.fromHtml(
                applicationContext.getString(
                    R.string.default_price,
                    product.prices
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            productPrice.text = applicationContext.getString(
                R.string.price,
                product.prices
            )
            defaultPrice.gone()
        }

        product.sizes.let {
            if (it.isNotEmpty()) {
                sizeAdapter.setData(it)
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initView() {
        initProductTabPager()

        back_icon.setOnClickListener {
            finish()
        }

        ARTestBtn.setOnClickListener {
            val intent = Intent(applicationContext, ARTestActivity::class.java)
            intent.putExtra(KEY_PRODUCT_ID, productId)
            intent.putExtra(KEY_PRODUCT_COLOR, product?.colors?.get(thumbnailAdapter.selectedIndex))
            intent.putExtra(KEY_PRODUCT_IMAGE, product?.images?.get(thumbnailAdapter.selectedIndex))
            startActivity(intent)
        }

        lifecycleScope.launch {
            callbackFlow {
                favouriteIcon.setOnClickListener {
                    offer(!it.isSelected)
                }
                awaitClose { cancel() }
            }.debounce(300)
                .collect {
                    favouriteIcon.isSelected = it
                    //todo: check isSelected: if false then call remove favourite API else otherwise call add favourite API

                }
        }

        lifecycleScope.launch {
            callbackFlow {
                addToCartBtn.setOnClickListener {
                    offer(Unit)
                }
                awaitClose { cancel() }
            }.debounce(300)
                .collect {
                    when {
                        productCount.progress == 0 -> Toast.makeText(
                            applicationContext,
                            "Please select number of product !",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        sizeAdapter.selectedIndex == -1 -> Toast.makeText(
                            applicationContext,
                            "Please select size !",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> {
                            product?.let {
                                cartViewModel.updateCart(
                                    it.id,
                                    it.sizes[sizeAdapter.selectedIndex].id,
                                    it.colors[thumbnailAdapter.selectedIndex],
                                    it.priceSale,
                                    productCount.progress
                                )
                            }
                        }
                    }
                }
        }
        relatedProductAdapter.productClickLister = {
            openProductDetailActivity(it)
        }
    }

    private fun initProductTabPager() {
        productTabAdapter = ProductTabAdapter(supportFragmentManager, lifecycle, 2)
        productTabPager.adapter = productTabAdapter

        productTabPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                productDetailTab.isSelected = position == 0
                productReviewTab.isSelected = position == 1
            }
        })

        productDetailTab.setOnClickListener { productTabPager.currentItem = 0 }
        productReviewTab.setOnClickListener { productTabPager.currentItem = 1 }
    }
}