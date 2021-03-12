package com.example.arfashion.presentation.presentation.register

import OtpEditText
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.arfashion.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_otp.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up)

        initOtp()

    }

    private fun initOtp() {
        first.addTextChangedListener(OtpEditText(first, this))
        second.addTextChangedListener(OtpEditText(second, this))
        third.addTextChangedListener(OtpEditText(third, this))
        fourth.addTextChangedListener(OtpEditText(fourth, this))
        fifth.addTextChangedListener(OtpEditText(fifth, this))
        sixth.addTextChangedListener(OtpEditText(sixth, this))
    }
}