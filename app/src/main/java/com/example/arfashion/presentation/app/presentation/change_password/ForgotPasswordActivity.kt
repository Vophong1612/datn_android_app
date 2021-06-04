package com.example.arfashion.presentation.app.presentation.change_password

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
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.verifyBtn
import kotlinx.android.synthetic.main.activity_register_email_or_phone.*

import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_back_save_header.back_icon
import kotlinx.android.synthetic.main.layout_back_save_header.screen_name
import kotlinx.android.synthetic.main.layout_or.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.forgot_password_label)
        onNavigateBack()

        changePasswordViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChangePasswordViewModel(userService) as T
            }
        })[ChangePasswordViewModel::class.java]

        initView()
        initViewModel()

    }

    private fun initViewModel() {
        changePasswordViewModel.resultSendCode.observe(this) {
            if (it) {
                val response = changePasswordViewModel.sendCodeResponse.value
                if (response != null) {
                    Toast.makeText(this, getString(R.string.send_code_successfully), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, VerifyForgotPasswordActivity::class.java)
                    intent.putExtra("strPhone",Utils.formatPhone(phoneInputEdt.text.toString()))
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {

        verifyBtn.setOnClickListener {
            if(phoneInputEdt.text.toString().isEmpty())
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            else if (Utils.isValidPhone(phoneInputEdt.text.toString()))
                changePasswordViewModel.sendCode(Utils.formatPhone(phoneInputEdt.text.toString()))
            else Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
        }

        signInBtn.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}