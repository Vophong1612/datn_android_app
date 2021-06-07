package com.example.arfashion.presentation.app.presentation.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.register.otp.GenericKeyEvent
import com.example.arfashion.presentation.app.presentation.register.otp.OtpEditText
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_forgot_password.signInBtn
import kotlinx.android.synthetic.main.activity_forgot_password_verify_code.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_otp.*

class VerifyForgotPasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ForgotPasswordViewModel

    private val userService = UserService.create()

    private var dataInput: String = ""

    private var dataType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_verify_code)
        init(intent)
    }

    private fun init(intentRes: Intent) {
        screen_name.text = this.getString(R.string.forgot_password_label)
        onNavigateBack()
        dataInput = intentRes.getStringExtra("strData").toString()
        dataType = intentRes.getStringExtra("strType").toString()

        changePasswordViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ForgotPasswordViewModel(userService) as T
            }
        })[ForgotPasswordViewModel::class.java]

        initView()
        initOtp()
        initViewModel()
    }

    private fun initViewModel() {

        changePasswordViewModel.resultGenerateCodeForgot.observe(this) {
            if (it) {
                val response = changePasswordViewModel.generateCodeForgotResponse.value
                if (response != null) {
                    Toast.makeText(this, getString(R.string.send_code_successfully), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        changePasswordViewModel.resultValidateCodeForgot.observe(this) {
            if (it) {
                val response = changePasswordViewModel.validateCodeForgotResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ChangePasswordForgotActivity::class.java)
                    intent.putExtra("accessToken",response.accessToken)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {

        tvResend.setOnClickListener {
            when (dataType){
                "email" -> changePasswordViewModel.generateCodeForgot(dataInput, "email")
                else ->  changePasswordViewModel.generateCodeForgot(Utils.formatPhone(dataInput), "phone")
            }
        }

        verifyCodeBtn.setOnClickListener {
            val code: String = first.text.toString() + second.text.toString() + third.text.toString() + fourth.text.toString() + fifth.text.toString() + sixth.text.toString()
            when (dataType){
                "email" -> changePasswordViewModel.validateCodeForgot(dataInput, code, dataType)
                else ->  changePasswordViewModel.validateCodeForgot(Utils.formatPhone(dataInput), code, dataType)
            }
        }

        signInBtn.setOnClickListener {
           finish()
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

    private fun initOtp() {
        first.addTextChangedListener(OtpEditText(first, this))
        second.addTextChangedListener(OtpEditText(second, this))
        third.addTextChangedListener(OtpEditText(third, this))
        fourth.addTextChangedListener(OtpEditText(fourth, this))
        fifth.addTextChangedListener(OtpEditText(fifth, this))
        sixth.addTextChangedListener(OtpEditText(sixth, this))

        first.setOnKeyListener(GenericKeyEvent(first, null))
        second.setOnKeyListener(GenericKeyEvent(second, first))
        third.setOnKeyListener(GenericKeyEvent(third, second))
        fourth.setOnKeyListener(GenericKeyEvent(first, third))
        fifth.setOnKeyListener(GenericKeyEvent(first, fourth))
        sixth.setOnKeyListener(GenericKeyEvent(first, fifth))
    }

}