package com.example.arfashion.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arfashion.R
import com.example.arfashion.presentation.presentation.login.LoginActivity
import com.example.arfashion.presentation.presentation.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}