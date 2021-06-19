package com.example.arfashion.presentation.app.presentation.product.test

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.main.ui.categories.KEY_PRODUCT_ID
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import kotlinx.android.synthetic.main.activity_ar_test.*
import kotlinx.android.synthetic.main.layout_after_test.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_before_test.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File

const val KEY_PRODUCT_COLOR = "key_bundle_product_color"
const val KEY_PRODUCT_IMAGE = "key_bundle_product_image"

class ARTestActivity : AppCompatActivity() {

    private val arTestViewModel by viewModels<ARTestViewModel>()

    private var productId: String = ""

    private var productColor: String = ""

    private var productImage: String = ""

    private var imgBody: File? = null

    private lateinit var loadImageController: LoadImageController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_test)

        getDataFromIntent()

        initData()

        initView()
    }

    private fun getDataFromIntent() {
        intent.extras?.run {
            getString(KEY_PRODUCT_ID)?.let {
                productId = it
            }
            getString(KEY_PRODUCT_IMAGE)?.let {
                productImage = it
            }
            getString(KEY_PRODUCT_COLOR)?.let {
                productColor = it
            }
        }
    }

    private fun initView() {
        back_icon.setOnClickListener {
            finish()
        }

        userImgView.setOnClickListener {
            selectedImage()
        }

        testBtn.setOnClickListener {
            test()
        }

        retryBtn.setOnClickListener {
            afterTestLayout.gone()
            beforeTestLayout.visible()
        }

        refreshLayout.setOnRefreshListener {
            test()
        }

        loadImageController = LoadImageController(this, applicationContext, contentResolver)

        loadImageController.takeImageResultListener = { file, bitmap ->
            showUserImage()
            userImg.setImageBitmap(bitmap)
            imgBody = file
        }

        loadImageController.chooseImageResultListener = { file, uri ->
            showUserImage()
            userImg.setImageURI(uri)
            imgBody = file
        }

        Glide.with(productImg)
            .load(productImage)
            .placeholder(R.drawable.img_default_category)
            .error(R.drawable.img_default_category)
            .into(productImg)
    }

    private fun initData() {
        arTestViewModel.result.observe(this, {
            when (it) {
                is ARResult.Success -> {
                    handleDate(it.data)
                }
                is ARResult.Error -> Toast.makeText(this, it.throwable.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })

        arTestViewModel.loading.observe(this, {
            refreshLayout.isRefreshing = it
        })
    }

    private fun handleDate(result: String) {
        beforeTestLayout.gone()
        afterTestLayout.visible()
        Glide.with(resultImg)
            .load(result)
            .placeholder(R.drawable.img_default_category)
            .error(R.drawable.img_default_category)
            .into(resultImg)
    }

    private fun showUserImage() {
        userImg.visible()
        addUserImgIcon.gone()
    }

    private fun test() {
        imgBody?.let {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), it)
            val body = MultipartBody.Part.createFormData("body", it.name, requestFile)
            val color = RequestBody.create(MediaType.parse("text/plain"), productColor)
            val id = RequestBody.create(MediaType.parse("text/plain"), productId)
            arTestViewModel.testAR(body, color, id)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LoadImageController.CAMERA_PERMISSION_CODE -> loadImageController.runPermissionRunnable(
                grantResults,
                loadImageController.cameraPermissionGrantedRunnable,
                loadImageController.cameraPermissionDeniedRunnable
            )
            LoadImageController.STORAGE_PERMISSION_CODE -> loadImageController.runPermissionRunnable(
                grantResults,
                loadImageController.galleryPermissionGrantedRunnable,
                loadImageController.galleryPermissionDeniedRunnable
            )
        }
    }

    private fun selectedImage() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(applicationContext.getString(R.string.selected_image_dialog_title))
            .setItems(R.array.pick_image) { dialog, item ->
                when (item) {
                    0 -> {
                        loadImageController.checkPermission(Manifest.permission.CAMERA, LoadImageController.CAMERA_PERMISSION_CODE)
                    }
                    1 -> {
                        loadImageController.checkPermission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            LoadImageController.STORAGE_PERMISSION_CODE
                        )
                    }
                    else -> dialog.dismiss()
                }
            }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadImageController.clearRegister()
    }
}