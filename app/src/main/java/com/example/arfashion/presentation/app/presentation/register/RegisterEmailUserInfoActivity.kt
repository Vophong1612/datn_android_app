package com.example.arfashion.presentation.app.presentation.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_register_email_user_info.*
import kotlinx.android.synthetic.main.layout_back_save_header.*
import java.util.*

class RegisterEmailUserInfoActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email_user_info)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up)
        onNavigateBack()

        registerViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(userService) as T
            }
        })[RegisterViewModel::class.java]

        initView(intent)
        initViewModel()
    }

    private fun initViewModel() {
        registerViewModel.registerResponse.observe(this) {
            if (it.status_code == 1) {
                val intent = Intent(this@RegisterEmailUserInfoActivity, RegisterVerifyActivity::class.java)
                intent.putExtra("strEmail",emailEdt.text.toString())
                startActivity(intent)
            } else Toast.makeText(this,getString(R.string.register_fail) ,Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView(intentEmail: Intent) {
        emailEdt.setText(intentEmail.getStringExtra("strEmail"))

        //set event
        check_icon.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            val confirmPassword = confirmPasswordEdt.text.toString()
            val name = fullNameEdt.text.toString()

            if (password != confirmPassword)
                Toast.makeText(this, getString(R.string.confirm_password_fail), Toast.LENGTH_SHORT).show()
            else if (!Utils.isValidPassword(password))
                Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
            else if (name.isEmpty())
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            else registerViewModel.register(email, password, name)
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}