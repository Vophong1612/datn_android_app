package com.example.arfashion.presentation.app.presentation.product.test

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
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

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    private lateinit var takeImageResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var chooseImageResultLauncher: ActivityResultLauncher<Intent>

    private val arTestViewModel by viewModels<ARTestViewModel>()

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

    private var productId: String = ""

    private var productColor: String = ""

    private var productImage: String = ""

    private var imgBody: File? = null

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

        Glide.with(productImg)
            .load(productImage)
            .placeholder(R.drawable.img_default_category)
            .error(R.drawable.img_default_category)
            .into(productImg)
    }

    private fun initData() {
        takeImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { data ->
                        val bitmap = data.extras?.get("data") as Bitmap
                        showUserImage()
                        userImg.setImageBitmap(bitmap)

                        val tempUri = getImageUri(bitmap)
                        getPathFromURI(tempUri)?.let { path ->
                            imgBody = File(path)
                        }
                    }
                }
            }

        chooseImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { data ->
                        val selectedImageUri: Uri? = data.data
                        selectedImageUri?.let {
                            getPathFromURI(selectedImageUri)?.let { path ->
                                imgBody = File(path)

                                showUserImage()
                                userImg.setImageURI(selectedImageUri)
                            }
                        }
                    }
                }
            }

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

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun getPathFromURI(uri: Uri): String? {
        var res: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = it.getString(columnIndex)
            }
        }
        cursor?.close()
        return res
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