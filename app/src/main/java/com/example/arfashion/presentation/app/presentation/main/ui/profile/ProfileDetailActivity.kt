package com.example.arfashion.presentation.app.presentation.main.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.forgot_password.ForgotPasswordViewModel
import com.example.arfashion.presentation.app.presentation.forgot_password.VerifyForgotPasswordActivity
import com.example.arfashion.presentation.app.presentation.login.LoginViewModel
import com.example.arfashion.presentation.app.presentation.product.test.ARTestActivity
import com.example.arfashion.presentation.app.presentation.register.RegisterVerifyActivity
import com.example.arfashion.presentation.app.presentation.register.RegisterViewModel
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import com.facebook.CallbackManager
import com.facebook.Profile
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register_email_or_phone.*
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.fragment_profile_tab.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_before_test.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileDetailActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

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

    private lateinit var user: User

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private var imgAvatar: File? = null

    private lateinit var profileViewModel: ProfileViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        init()
    }

    private fun init() {

        screen_name.text = this.getString(R.string.profile_label)
        onNavigateBack()

        pref = this.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        user = userStorage.load()

        profileViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(userService) as T
            }
        })[ProfileViewModel::class.java]


        initView()
        initViewModel()

    }

    private fun initViewModel() {
        profileViewModel.resultUploadAvatar.observe(this) {
            if (it) {
                val response = profileViewModel.uploadAvatarResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel.resultUpdateProfile.observe(this) {
            if (it) {
                val response = profileViewModel.updateProfileResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() {

        registerForContextMenu(iv_avatar_detail)
        registerForContextMenu(v_avatar_detail)
        registerForContextMenu(iv_avatar_camera)

        iv_birthday.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val dd: Int = calendar.get(Calendar.DATE)
            val mm: Int = calendar.get(Calendar.MONTH)
            val yy: Int = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    birthdayEdt.setText(simpleDateFormat.format(calendar.time))
                }, yy, mm, dd)
            datePickerDialog.show()
        }

        val genderList = resources.getStringArray(R.array.genderList)
        if (genderSpinner != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item, genderList)
            genderSpinner.adapter = adapter

            genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        iv_avatar_detail.setOnClickListener {
            this.openContextMenu(it)
        }

        v_avatar_detail.setOnClickListener {
            this.openContextMenu(it)
        }

        iv_avatar_camera.setOnClickListener {
            this.openContextMenu(it)
        }

        phoneNumberEdt.setOnClickListener {
            Toast.makeText(this, R.string.not_change, Toast.LENGTH_SHORT).show()
        }

        user_changePasswordEdt.setOnClickListener{
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        iv_user_transform_change_password.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        val password: String = userStorage.loadPassword()
        if(password.isEmpty())
            relative_layout_change_password.visibility = View.INVISIBLE

        takeImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { data ->
                        val bitmap = data.extras?.get("data") as Bitmap
                        iv_avatar_detail.setImageBitmap(bitmap)
                        val tempUri = getImageUri(bitmap)
                        getPathFromURI(tempUri)?.let { path ->
                            imgAvatar = File(path)
                            showAvatarUploadingDialog()
                        }
                    }
                }
            }

        chooseImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { data ->
                        val selectedImageUri: Uri? = data.data
                        iv_avatar_detail.setImageURI(selectedImageUri)
                        selectedImageUri?.let {
                            getPathFromURI(selectedImageUri)?.let { path ->
                                imgAvatar = File(path)
                                iv_avatar_detail.setImageURI(selectedImageUri)
                            }
                        }
                    }
                }
            }

        check_icon.setOnClickListener {

            val email: String = emailUserEdt.text.toString()
            val name: String = fullNameEdt.text.toString()
            var birthday: String = birthdayEdt.text.toString()
            if(!birthday.contains("T00:00:00.000Z"))
                birthday = Utils.formatDate(birthday)
            val gender: Int = genderSpinner.selectedItemPosition

            if (birthday.isEmpty() || email.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT)
                    .show()
            } else if (!Utils.isValidEmail(email)) {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT)
                    .show()
            } else {
                user.credential.accessToken?.let { it1 ->
                    profileViewModel.updateProfile(it1, name, email, birthday, gender)
                }
            }
        }

        setDataField()

    }

    private fun showAvatarUploadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure to upload this avatar?")

        builder.setPositiveButton(
            "YES"
        ) { dialog, _ -> // Do nothing but close the dialog
            dialog.dismiss()
            imgAvatar?.let {
                val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), it)
                val avatar = MultipartBody.Part.createFormData("avatar", it.name, requestFile)
                user.credential.accessToken?.let { it2 ->
                    profileViewModel.uploadAvatar(
                        it2,
                        avatar
                    )
                }
            }
        }

        builder.setNegativeButton(
            "NO"
        ) { dialog, _ -> // Do nothing
            dialog.dismiss()
            if (!user.profile.avatar.isNullOrEmpty()) {
                Glide.with(iv_avatar_detail)
                    .load(user.profile.avatar)
                    .into(iv_avatar_detail)
            }
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun setDataField() {

        phoneNumberEdt.setText(user.profile.phone)
        emailUserEdt.setText(user.profile.email)
        fullNameEdt.setText(user.profile.name)
        birthdayEdt.setText(Utils.formatDateToString(user.profile.birthday))

        when (user.profile.gender) {
            0 -> genderSpinner.setSelection(0, true)
            1 -> genderSpinner.setSelection(1, true)
            else -> genderSpinner.setSelection(-1)
        }

        if(!user.profile.avatar.isNullOrEmpty()){
            Glide.with(iv_avatar_detail)
                .load(user.profile.avatar)
                .into(iv_avatar_detail)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.update_avatar_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_camera ->{
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
                return  true
            }
            R.id.menu_gallery -> {
                checkPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    STORAGE_PERMISSION_CODE
                )
                return  true
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }
}


