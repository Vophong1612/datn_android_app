package com.example.arfashion.presentation.app.presentation.forgotpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.register.RegisterUserInfoActivity
import com.example.arfashion.presentation.app.presentation.register.RegisterVerifiyActivity
import kotlinx.android.synthetic.main.layout_back_save_header.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        check_icon.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, RegisterUserInfoActivity::class.java)
            startActivity(intent)
        }
    }
}