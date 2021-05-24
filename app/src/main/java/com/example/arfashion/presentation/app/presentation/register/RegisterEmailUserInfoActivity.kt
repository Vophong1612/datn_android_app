package com.example.arfashion.presentation.app.presentation.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_register_email_user_info.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import java.util.*


class RegisterUserInfoActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private val userService = UserService.create()

    private val requestCodeCamera = 101

    private val requestCodeMem = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email_user_info)
        init()

    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up_)
        onNavigateBack()

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
                val intent = Intent(this@RegisterUserInfoActivity, RegisterVerifiyActivity::class.java)
                intent.putExtra("strEmail",emailEdt.text.toString())
                startActivity(intent)

            } else {
                Toast.makeText(this,getString(R.string.register_fail)+": "+registerViewModel.registerResponse.value!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        addressEdt.setText(RegisterChooseAddressActivity.addressStr)
    }

    @SuppressLint("SimpleDateFormat", "ShowToast")
    private fun initView(intentEmail: Intent) {
        emailEdt.setText(intentEmail.getStringExtra("strEmail"))
//        registerForContextMenu(iv_avatar)
//        registerForContextMenu(v_avatar)
//        registerForContextMenu(iv_avatar_camera)

        //date picker dialog
//        iv_birthday.setOnClickListener {
//            val calendar: Calendar = Calendar.getInstance()
//            val dd: Int = calendar.get(Calendar.DATE)
//            val mm: Int = calendar.get(Calendar.MONTH)
//            val yy: Int = calendar.get(Calendar.YEAR)
//            val datePickerDialog = DatePickerDialog(this, OnDateSetListener { _, year, month, dayOfMonth ->
//                calendar.set(year, month, dayOfMonth)
//                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
//                birthdayEdt.setText(simpleDateFormat.format(calendar.time))
//            }, yy, mm, dd)
//            datePickerDialog.show()
//        }

        //spinner gender
//        val genderList = resources.getStringArray(R.array.genderList)
//        if (genderSpinner != null) {
//            val adapter = ArrayAdapter(this,
//                    android.R.layout.simple_spinner_dropdown_item, genderList)
//            genderSpinner.adapter = adapter
//
//            genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // write code to perform some action
//                }
//            }
//        }

        //set event
        check_icon.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            val confirmPassword = confirmPasswordEdt.text.toString()
            val name = fullNameEdt.text.toString()

            if(!password.equals(confirmPassword)){
                if (Utils.isValidEmail(email) && Utils.isValidPassword(password) && name.length > 0)
                    registerViewModel.register(email, password, name)
                else
                    Toast.makeText(this,getString(R.string.invalid_data),Toast.LENGTH_SHORT).show()
            }else Toast.makeText(this,getString(R.string.confirm_password_fail),Toast.LENGTH_SHORT).show()

        }



//        addressEdt.setOnClickListener {
//            val intent = Intent(this@RegisterUserInfoActivity, RegisterChooseAddressActivity::class.java)
//            startActivity(intent)
//        }
//
//        iv_avatar.setOnClickListener {
//            this.openContextMenu(it)
//        }
//
//        v_avatar.setOnClickListener {
//            this.openContextMenu(it)
//        }
//
//        iv_avatar_camera.setOnClickListener {
//            this.openContextMenu(it)
//        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

//    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        val inflater = menuInflater
//        inflater.inflate(R.menu.update_avatar_menu, menu)
//    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_camera ->{
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(intent, requestCodeCamera)
//                return true
//            }
//            R.id.menu_gallery ->{
//                val intent1 = Intent(Intent.ACTION_PICK)
//                intent1.type = "image/*"
//                startActivityForResult(intent1, requestCodeMem)
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == requestCodeCamera && resultCode == RESULT_OK && data != null){
//            iv_avatar.setImageBitmap(data.extras?.get("data") as Bitmap)
//        }
//
//        if(requestCode == requestCodeMem && resultCode == RESULT_OK && data != null) {
//
//            iv_avatar.setImageURI(data.data)
//        }
//
//    }
}