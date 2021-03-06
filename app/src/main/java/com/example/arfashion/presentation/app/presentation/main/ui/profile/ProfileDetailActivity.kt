package com.example.arfashion.presentation.app.presentation.main.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.product.test.LoadImageController
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register_email_or_phone.*
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.fragment_profile_tab.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_before_test.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var loadImageController: LoadImageController

    private lateinit var user: User

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private var imgAvatar: File? = null

    private lateinit var profileViewModel: ProfileViewModel

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

        profileViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(ProfileViewModel::class.java)

        initView()
        initViewModel()

    }

    private fun initViewModel() {

        profileViewModel.loading.observe(this) {
            refreshLayoutProfile.isRefreshing = it
        }

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

        refreshLayoutProfile.setOnRefreshListener {
            refreshLayoutProfile.isRefreshing = false
        }

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
        if(password.isEmpty()){
            user_info_change_password_background.visibility = View.INVISIBLE
            changePasswordLabel.visibility = View.INVISIBLE
            relative_layout_change_password.visibility = View.INVISIBLE
        }

        loadImageController = LoadImageController(this, applicationContext, contentResolver)

        loadImageController.takeImageResultListener = { file, bitmap ->
            iv_avatar_detail.setImageBitmap(bitmap)
            imgAvatar = file
            showAvatarUploadingDialog()
        }

        loadImageController.chooseImageResultListener = { file, uri ->
            iv_avatar_detail.setImageURI(uri)
            imgAvatar = file
            showAvatarUploadingDialog()
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
                    profileViewModel.updateProfile(name, email, birthday, gender)
            }
        }

        setDataField()

    }

    private fun showAvatarUploadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.dialog_confirm))
        builder.setMessage(getString(R.string.alert_upload_avt))

        builder.setPositiveButton(
            getString(R.string.dialog_yes)
        ) { dialog, _ -> // Do nothing but close the dialog
            dialog.dismiss()
            imgAvatar?.let {
                val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), it)
                val avatar = MultipartBody.Part.createFormData("avatar", it.name, requestFile)
                profileViewModel.uploadAvatar(avatar)
            }
        }

        builder.setNegativeButton(
            getString(R.string.dialog_no)
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

        phoneNumberEdt.setText(Utils.formatPhoneToNormal(user.profile.phone))
        emailUserEdt.setText(user.profile.email)
        fullNameEdt.setText(user.profile.name)
        birthdayEdt.setText(Utils.formatDateToString(user.profile.birthday.toString()))

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
                loadImageController.checkPermission(Manifest.permission.CAMERA, LoadImageController.CAMERA_PERMISSION_CODE)
                return  true
            }
            R.id.menu_gallery -> {
                loadImageController.checkPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    LoadImageController.STORAGE_PERMISSION_CODE
                )
                return  true
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadImageController.clearRegister()
    }
}


