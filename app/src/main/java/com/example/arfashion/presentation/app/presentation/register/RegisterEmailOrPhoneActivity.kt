package com.example.arfashion.presentation.app.presentation.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_register_email_or_phone.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_or.*

class RegisterEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email_or_phone)
        init()
        verifyBtn.setOnClickListener {
            if (Utils.isValidEmail(emailSignUpEdt.text.toString())){
                val intent = Intent(this@RegisterEmailActivity, RegisterEmailUserInfoActivity::class.java)
                intent.putExtra("strEmail",emailSignUpEdt.text.toString())
                startActivity(intent)
            } else if (Utils.isValidPhone(emailSignUpEdt.text.toString())){
                val intent = Intent(this@RegisterEmailActivity, RegisterPhoneUserInfoActivity::class.java)
                intent.putExtra("strPhone",emailSignUpEdt.text.toString())
                Toast.makeText(this, emailSignUpEdt.text.toString(), Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this@RegisterEmailActivity, LoginActivity::class.java)
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
        screen_name.text = this.getString(R.string.sign_up)
        onNavigateBack()

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }


}