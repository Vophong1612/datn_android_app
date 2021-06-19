package com.example.arfashion.presentation.app.presentation.product.test

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arfashion.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.ref.WeakReference

typealias TakeImageResultListener = (file: File, bitmap: Bitmap) -> Unit
typealias ChooseImageResultListener = (file: File, uri: Uri) -> Unit

class LoadImageController(
    host: AppCompatActivity,
    private val context: Context,
    private val contentResolver: ContentResolver
) {
    companion object {
        const val CAMERA_PERMISSION_CODE = 100
        const val STORAGE_PERMISSION_CODE = 101
    }

    private val hostRef = WeakReference(host)

    private val host: AppCompatActivity?
        get() {
            return hostRef.get()
        }

    var takeImageResultListener: TakeImageResultListener? = null

    var chooseImageResultListener: ChooseImageResultListener? = null

    private lateinit var takeImageResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var chooseImageResultLauncher: ActivityResultLauncher<Intent>

    val cameraPermissionGrantedRunnable = Runnable {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takeImageResultLauncher.launch(takePicture)
    }

    @SuppressLint("QueryPermissionsNeeded")
    val galleryPermissionGrantedRunnable = Runnable {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        ).also { galleryIntent ->
            galleryIntent.resolveActivity(context.packageManager)?.also {
                galleryIntent.type = "image/*"
                chooseImageResultLauncher.launch(galleryIntent)
            }
        }
    }

    val galleryPermissionDeniedRunnable =
        Runnable {
            Toast.makeText(
                context,
                context.getString(R.string.access_storage_denied),
                Toast.LENGTH_SHORT
            ).show()
        }

    val cameraPermissionDeniedRunnable =
        Runnable {
            Toast.makeText(
                context,
                context.getString(R.string.prepare_stream_error),
                Toast.LENGTH_SHORT
            ).show()
        }

    init {
        this.host?.run {
            takeImageResultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        it.data?.let { data ->
                            val bitmap = data.extras?.get("data") as Bitmap

                            val tempUri = getImageUri(bitmap)
                            getPathFromURI(tempUri)?.let { path ->
                                takeImageResultListener?.invoke(File(path), bitmap)
                            }
                        }
                    }
                }

            chooseImageResultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        it.data?.let { data ->
                            val selectedImageUri: Uri? = data.data
                            selectedImageUri?.let {
                                getPathFromURI(selectedImageUri)?.let { path ->
                                    chooseImageResultListener?.invoke(File(path), selectedImageUri)
                                }
                            }
                        }
                    }
                }
        }
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

    fun runPermissionRunnable(
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

    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            this.host?.let {
                ActivityCompat.requestPermissions(it, arrayOf(permission), requestCode)
            }
        } else {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> cameraPermissionGrantedRunnable.run()
                STORAGE_PERMISSION_CODE -> galleryPermissionGrantedRunnable.run()
            }
        }
    }

    fun clearRegister() {
        takeImageResultLauncher.unregister()
        chooseImageResultLauncher.unregister()
    }
}