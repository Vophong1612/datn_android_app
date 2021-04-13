package com.example.arfashion.presentation.app.presentation.register

import OtpEditText
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.app.presentation.register.otp.GenericKeyEvent
import kotlinx.android.synthetic.main.activity_register_verify.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_otp.*

class RegisterVerifiyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_verify)
        init()
        verifyBtn.setOnClickListener {
            val intent = Intent(this@RegisterVerifiyActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up)
        onNavigateBack()

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