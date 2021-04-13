package com.example.arfashion.presentation.app.presentation.register

import OtpEditText
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_register_email.*
import kotlinx.android.synthetic.main.layout_back_header.*

class RegisterEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email)
        init()
        verifyBtn.setOnClickListener {
            if (Utils.isValidEmail(emailSignUpEdt.text.toString())){
                val intent = Intent(this@RegisterEmailActivity, RegisterUserInfoActivity::class.java)
                intent.putExtra("strEmail",emailSignUpEdt.text.toString())
                startActivity(intent)
            }else Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
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


}