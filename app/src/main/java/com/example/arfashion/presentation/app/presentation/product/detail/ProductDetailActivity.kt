package com.example.arfashion.presentation.app.presentation.product.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.main.ui.home.ProductAdapter
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class ProductDetailActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    private lateinit var productTabAdapter: ProductTabAdapter

    private val productDetailViewModel: ProductDetailViewModel by viewModels()

    private lateinit var thumbnailAdapter: ThumbnailAdapter

    private lateinit var sizeAdapter: ProductSizeAdapter

    private lateinit var relatedProductAdapter: ProductAdapter

    private var product: Product? = null

    private lateinit var takeImageResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var chooseImageResultLauncher: ActivityResultLauncher<Intent>

    private val cameraPermissionGrantedRunnable = Runnable {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takeImageResultLauncher.launch(takePicture)
    }

    private val galleryPermissionGrantedRunnable = Runnable {
        val pickImage = Intent().also {
            it.type = "image/*"
            it.action = Intent.ACTION_GET_CONTENT
        }
        chooseImageResultLauncher.launch(pickImage)
    }

    private val galleryPermissionDeniedRunnable =
        Runnable {
            Toast.makeText(
                this,
                this.getString(R.string.access_storage_denied),
                Toast.LENGTH_SHORT
            ).show()
        }

    private val cameraPermissionDeniedRunnable =
        Runnable {
            Toast.makeText(
                this,
                this.getString(R.string.prepare_stream_error),
                Toast.LENGTH_SHORT
            ).show()
        }

    init {
        lifecycleScope.launchWhenCreated {
            productDetailViewModel.getProductDetail("6059fad101f525c13f092e12") //fixme: this is hardcode, please replace id by id which is in Bundle
            productDetailViewModel.getRelatedProduct("6059fad101f525c13f092e12")
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> runPermissionRunnable(
                grantResults,
                cameraPermissionGrantedRunnable,
                cameraPermissionDeniedRunnable
            )
            STORAGE_PERMISSION_CODE -> runPermissionRunnable(
                grantResults,
                galleryPermissionGrantedRunnable,
                galleryPermissionDeniedRunnable
            )
        }
    }

    private fun runPermissionRunnable(
        grantResults: IntArray,
        runnableGranted: Runnable,
        runnableDenied: Runnable
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runnableGranted.run()
        } else {
            runnableDenied.run()
        }
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
        relatedProductAdapter = ProductAdapter(applicationContext)
        with(relatedProductList) {
            adapter = relatedProductAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RelatedProductItemOffset(applicationContext))
        }

        refreshLayout.setOnRefreshListener {
            productDetailViewModel.getProductDetail("6059fad101f525c13f092e12") //fixme: this is hardcode, please replace id by id which is in Bundle
        }

        productDetailViewModel.product.observe(this) {
            when (it) {
                is ARResult.Success -> {
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

        takeImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { data ->
                        mainImage.setImageBitmap(data.extras?.get("data") as Bitmap)
                    }
                }
            }

        chooseImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { data ->
                        val selectedImage: Uri? = data.data
                        mainImage.setImageURI(selectedImage)
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
        this.product = product

        Glide.with(mainImage)
            .load(product.images[0])
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

        if (product.sizes.isNotEmpty()) {
            sizeAdapter.setData(product.sizes)
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
            selectedImage()
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

    private fun selectedImage() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(applicationContext.getString(R.string.selected_image_dialog_title))
            .setItems(R.array.pick_image) { dialog, item ->
                when (item) {
                    0 -> {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
                    }
                    1 -> {
                        checkPermission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            STORAGE_PERMISSION_CODE
                        )
                    }
                    else -> dialog.dismiss()
                }
            }
        dialog.show()
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> cameraPermissionGrantedRunnable.run()
                STORAGE_PERMISSION_CODE -> galleryPermissionGrantedRunnable.run()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        takeImageResultLauncher.unregister()
    }
}