package com.example.arfashion.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.arfashion.R

class SplashActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        loginBtn.setOnClickListener {
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }
//
//        registerBtn.setOnClickListener {
//            val intent = Intent(this@MainActivity, RegisterEmailOrPhoneActivity::class.java)
//            startActivity(intent)
//        }
//
//        homeBtn.setOnClickListener {
//            val intent = Intent(this@MainActivity, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()

    }

    private fun init() {


        //progressAnimation()

        onNavigateBack()
    }


    private fun onNavigateBack() {
        finish()
    }

}