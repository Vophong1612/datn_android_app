package com.example.arfashion.presentation.app.presentation.product.comment.rating

import android.Manifest
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.product.comment.CommentViewModel
import com.example.arfashion.presentation.app.presentation.product.test.LoadImageController
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

const val KEY_RATING_PRODUCT_ID = "key_rating_product_id"
const val KEY_RATING_PRODUCT_NAME = "key_rating_product_name"
const val KEY_RATING_PRODUCT_IMAGE = "key_rating_product_image"

class RatingActivity : AppCompatActivity() {

    private val chipList = mutableListOf<Chip>()

    private val imageList = mutableListOf<ImageView>()

    private val clearImageList = mutableListOf<ImageView>()

    private lateinit var loadImageController: LoadImageController

    private lateinit var commentViewModel: CommentViewModel

    private var currentImageIndex : Int? = null

    private val imagesRating = mutableListOf<File>()

    private var proId: String?= null

    private var proName: String? = null

    private var proImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        commentViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(
            CommentViewModel::class.java
        )

        loadImageController = LoadImageController(this, applicationContext, contentResolver)

        chipList.addAll(listOf(firstChip, secondChip, thirdChip, fourthChip))

        imageList.addAll(listOf(image1, image2, image3, image4, image5))

        clearImageList.addAll(
            listOf(
                clearImage1,
                clearImage2,
                clearImage3,
                clearImage4,
                clearImage5
            )
        )

        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
        }

        getDataFromIntent()

        initView()

        initViewModel()
    }

    private fun initViewModel() {
        commentViewModel.addComment.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    Toast.makeText(applicationContext, "Success! Please waiting for admin accept your comment", Toast.LENGTH_LONG).show()
                }
                is ARResult.Error -> {
                    Toast.makeText(applicationContext, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        commentViewModel.loading.observe(this) {
            refreshLayout.isRefreshing = it
        }
    }

    private fun getDataFromIntent() {
        proId = intent.extras?.getString(KEY_RATING_PRODUCT_ID)
        proName = intent.extras?.getString(KEY_RATING_PRODUCT_NAME)
        proImage = intent.extras?.getString(KEY_RATING_PRODUCT_IMAGE)
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

    override fun onDestroy() {
        super.onDestroy()
        loadImageController.clearRegister()
    }

    private fun initView() {
        backLayout.setOnClickListener {
            finish()
        }
        screen_name.text = getString(R.string.rating_screen_name)
        check_icon.setOnClickListener {
            proId?.let{ id ->
                val starValue = ratingBar.rating.toInt()
                val contentValue = contentComment.text.toString()
                val titleValue = titleComment.text.toString()

                val photosPart = mutableListOf<MultipartBody.Part>()
                imagesRating.forEach { file ->
                    val photosBody = RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                    )
                    val body = MultipartBody.Part.createFormData("photos", file.name, photosBody)
                    photosPart.add(body)
                }

                val star = RequestBody.create(MediaType.parse("text/plain"), starValue.toString())
                val content = RequestBody.create(MediaType.parse("text/plain"), contentValue)
                val title = RequestBody.create(MediaType.parse("text/plain"), titleValue)
                val productId = RequestBody.create(MediaType.parse("text/plain"), id)
                commentViewModel.addComment(photosPart, star, content, title, productId)
            }
        }

        chipList.forEach {
            it.setOnCheckedChangeListener { _, isChecked ->
                handleQuickComment(it.text.toString(), isChecked)
            }
        }

        imageList.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                currentImageIndex = index
                chooseTypeOfSelectImage()
            }
        }

        clearImageList.forEachIndexed { index, clearImage ->
            clearImage.setOnClickListener {
                imageList[index].setImageResource(R.drawable.ic_add_image)
                clearImage.gone()
                imagesRating.removeAt(index)
            }
        }

        loadImageController.takeImageResultListener = { file, bitmap ->
            currentImageIndex?.let {
                imageList[it].setImageBitmap(bitmap)
                clearImageList[it].visible()
                imagesRating.add(file)
            }
        }

        loadImageController.chooseImageResultListener = { file, uri ->
            currentImageIndex?.let {
                imageList[it].setImageURI(uri)
                clearImageList[it].visible()
                imagesRating.add(file)
            }
        }

        productName.text = proName
        Glide.with(productImage)
            .load(proImage)
            .into(productImage)
    }

    private fun handleQuickComment(text: String, isChecked: Boolean) {
        if (isChecked) {
            addQuickComment(text)
        } else {
            removeQuickComment(text)
        }
    }

    private fun removeQuickComment(text: String) {
        val commentText = contentComment.text.toString()
        val firstIndex = commentText.indexOf(text)
        val notFirstIndex = commentText.indexOf(", $text")

        val textStartIndex = if (notFirstIndex == -1) {
            firstIndex
        } else notFirstIndex
        var textEndIndex = textStartIndex + ", $text".length
        if (textEndIndex > commentText.length) {
            textEndIndex = commentText.length
        }

        val newComment = commentText.removeRange(textStartIndex, textEndIndex)
        contentComment.setText(newComment)
    }

    private fun addQuickComment(text: String) {
        var commentText = contentComment.text.toString()
        commentText += if (commentText.isEmpty()) {
            text
        } else ", $text"
        contentComment.setText(commentText)
    }

    private fun chooseTypeOfSelectImage() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(applicationContext.getString(R.string.selected_image_dialog_title))
            .setItems(R.array.pick_image) { dialog, item ->
                when (item) {
                    0 -> {
                        loadImageController.checkPermission(
                            Manifest.permission.CAMERA,
                            LoadImageController.CAMERA_PERMISSION_CODE
                        )
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
}