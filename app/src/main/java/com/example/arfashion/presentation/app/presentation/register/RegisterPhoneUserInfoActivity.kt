package com.example.arfashion.presentation.app.presentation.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_register_email_user_info.*
import kotlinx.android.synthetic.main.activity_register_email_user_info.confirmPasswordEdt
import kotlinx.android.synthetic.main.activity_register_email_user_info.fullNameEdt
import kotlinx.android.synthetic.main.activity_register_email_user_info.passwordEdt
import kotlinx.android.synthetic.main.activity_register_phone_user_info.*
import kotlinx.android.synthetic.main.layout_back_save_header.*

class RegisterPhoneUserInfoActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_phone_user_info)
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
        registerViewModel.resultPhoneRegister.observe(this) {
            if (it) {
                val response = registerViewModel.phoneRegisterResponse.value
                val intent = Intent(this@RegisterPhoneUserInfoActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView(intentEmail: Intent) {
        phoneEdt.setText(intentEmail.getStringExtra("strPhone"))

        //set event
        check_icon.setOnClickListener {
            val phone = phoneEdt.text.toString()
            val password = passwordEdt.text.toString()
            val confirmPassword = confirmPasswordEdt.text.toString()
            val name = fullNameEdt.text.toString()

            if(password != confirmPassword)
                Toast.makeText(this,getString(R.string.confirm_password_fail),Toast.LENGTH_SHORT).show()
            else if (!Utils.isValidPassword(password))
                Toast.makeText(this,getString(R.string.invalid_password),Toast.LENGTH_SHORT).show()
            else if (name.isEmpty())
                Toast.makeText(this,getString(R.string.invalid_data),Toast.LENGTH_SHORT).show()
            else registerViewModel.registerPhone(phone, password, name)
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}