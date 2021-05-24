package com.example.arfashion.presentation.app.presentation.register

import OtpEditText
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
import com.example.arfashion.presentation.app.presentation.register.otp.GenericKeyEvent
import com.example.arfashion.presentation.services.UserService
import kotlinx.android.synthetic.main.activity_register_verify.*
import kotlinx.android.synthetic.main.activity_register_verify.emailEdt
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_otp.*

class RegisterVerifiyActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_verify)
        init(intent)
    }

    private fun init(intentEmail: Intent) {
        screen_name.text = this.getString(R.string.sign_up)
        val email: String = intentEmail.getStringExtra("strEmail")!!
        onNavigateBack()

        registerViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(userService) as T
            }
        })[RegisterViewModel::class.java]

        initViewModel()

        tvResend.setOnClickListener {
            registerViewModel.resendActiveEmail(email)
        }

        verifyBtn.setOnClickListener {
            val activeCode: String = first.text.toString() + second.text.toString() + third.text.toString() + fourth.text.toString() + fifth.text.toString() + sixth.text.toString()
            registerViewModel.active(activeCode, email)
        }
    }

    private fun initViewModel() {
        registerViewModel.resultResendActiveEmail.observe(this) {
            if (it) {
                val response = registerViewModel.resendActiveEmailResponse.value
                if (response != null) {
                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.resultActive.observe(this) {
            if (it) {
                val response = registerViewModel.activeResponse.value
                if (response != null) {
                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this,"Failure!", Toast.LENGTH_SHORT).show()
            }
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