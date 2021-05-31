package com.example.arfashion.presentation.app.presentation.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_back_save_header.back_icon
import kotlinx.android.synthetic.main.layout_back_save_header.screen_name
import kotlinx.android.synthetic.main.layout_or.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()

        signInBtn.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        facebookLoginBtn.setOnClickListener {
            Toast.makeText(this, "fb", Toast.LENGTH_SHORT).show()
        }

        googleLoginBtn.setOnClickListener {
            Toast.makeText(this, "gg", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        screen_name.text = this.getString(R.string.forgot_password_label)
        onNavigateBack()
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}