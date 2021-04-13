package com.example.arfashion.presentation.app.presentation.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.login.LoginViewModel
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_user_info.*
import kotlinx.android.synthetic.main.activity_register_user_info.passwordEdt
import kotlinx.android.synthetic.main.layout_back_save_header.*
import okhttp3.internal.Util
import java.text.SimpleDateFormat
import java.util.*


class RegisterUserInfoActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var registerViewModel: RegisterViewModel

    private val userService = UserService.create()

    private val request_code_camera = 101

    private val request_code_mem = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user_info)
        init()

    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up)
        onNavigateBack()

        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()

        registerViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(userService) as T
            }
        })[RegisterViewModel::class.java]

        initView(intent)
        initViewModel()
    }

    private fun initViewModel() {
        registerViewModel.result.observe(this) {

            if (it) {
                val response = registerViewModel.registerResponse.value
                Toast.makeText(this, response!!.message,Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterUserInfoActivity, RegisterVerifiyActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this,getString(R.string.register_fail)+": "+registerViewModel.registerResponse.value!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "ShowToast")
    private fun initView(intent: Intent) {
        emailEdt.setText(intent.getStringExtra("strEmail"))
        registerForContextMenu(iv_avatar)
        registerForContextMenu(v_avatar)
        registerForContextMenu(iv_avatar_camera)

        //date picker dialog
        iv_birthday.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val dd: Int = calendar.get(Calendar.DATE)
            val mm: Int = calendar.get(Calendar.MONTH)
            val yy: Int = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(this, OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                birthdayEdt.setText(simpleDateFormat.format(calendar.time))
            }, yy, mm, dd)
            datePickerDialog.show()
        }

        //spinner gender
        val genderList = resources.getStringArray(R.array.genderList)
        if (genderSpinner != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, genderList)
            genderSpinner.adapter = adapter

            genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        //set event
        check_icon.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            val name = fullNameEdt.text.toString()

            if (Utils.isValidEmail(email) && Utils.isValidPassword(password) && Utils.isValidName(name))
                registerViewModel.register(email, password, name)
            else
                Toast.makeText(this,getString(R.string.invalid_data),Toast.LENGTH_SHORT).show()
        }

        this.relative_layout_address_register.setOnClickListener {
            val intent = Intent(this@RegisterUserInfoActivity, RegisterChooseAddressActivity::class.java)
            startActivity(intent)
        }

        iv_avatar.setOnClickListener {
            this.openContextMenu(it)
        }

        v_avatar.setOnClickListener {
            this.openContextMenu(it)
        }

        iv_avatar_camera.setOnClickListener {
            this.openContextMenu(it)
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
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
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, request_code_camera)
                return true
            }
            R.id.menu_gallery ->{
                val intent1 = Intent(Intent.ACTION_PICK)
                intent1.type = "image/*"
                startActivityForResult(intent1, request_code_mem)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == request_code_camera && resultCode == RESULT_OK && data != null){
//            iv_avatar.setImageBitmap(data.get("data"))
        }

        if(requestCode == request_code_mem && resultCode == RESULT_OK && data != null) {
//            val uri: Uri = data.data
//            var inputStream: InputStream? = null
//            try {
//                inputStream = contentResolver.openInputStream(uri)
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            }
//            val bit = BitmapFactory.decodeStream(inputStream)
//            iv_avatar.setImageBitmap(bit)
        }

    }
}