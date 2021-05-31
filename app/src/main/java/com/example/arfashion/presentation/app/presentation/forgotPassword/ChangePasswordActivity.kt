package com.example.arfashion.presentation.app.presentation.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_or.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        init()

        check_icon.setOnClickListener {
            val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun init() {
        screen_name.text = this.getString(R.string.forgot_password_label)
        onNavigateBack()
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            val intent = Intent(this@ChangePasswordActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}