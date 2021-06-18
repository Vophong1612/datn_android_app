package com.example.arfashion.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.bill.BillActivity
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.app.presentation.main.MainActivity
import com.example.arfashion.presentation.app.presentation.register.RegisterEmailOrPhoneActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterEmailOrPhoneActivity::class.java)
            startActivity(intent)
        }

        homeBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }

        billBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, BillActivity::class.java)
            startActivity(intent)
        }
    }

}