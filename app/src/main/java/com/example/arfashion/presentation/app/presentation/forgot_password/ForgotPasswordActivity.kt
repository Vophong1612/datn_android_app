package com.example.arfashion.presentation.app.presentation.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.verifyBtn

import kotlinx.android.synthetic.main.layout_back_save_header.back_icon
import kotlinx.android.synthetic.main.layout_back_save_header.screen_name

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ForgotPasswordViewModel

    private var dataType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.forgot_password_label)
        onNavigateBack()

        changePasswordViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(ForgotPasswordViewModel::class.java)

        initView()
        initViewModel()

    }

    private fun initViewModel() {
        changePasswordViewModel.resultGenerateCodeForgot.observe(this) {
            if (it) {
                val response = changePasswordViewModel.generateCodeForgotResponse.value
                if (response != null) {
                    Toast.makeText(this, getString(R.string.send_code_successfully), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, VerifyForgotPasswordActivity::class.java)
                    intent.putExtra("strData", dataInputEdt.text.toString())
                    intent.putExtra("strType", dataType)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {

        verifyBtn.setOnClickListener {

            val input: String = dataInputEdt.text.toString()

            if(Utils.isValidEmail(input)){
                dataType = "email"
                changePasswordViewModel.generateCodeForgot(input, "email")
            }else if(Utils.isValidPhone(dataInputEdt.text.toString())){
                dataType = "phone"
                changePasswordViewModel.generateCodeForgot(Utils.formatPhone(input), "phone")
            }else {
                if(dataInputEdt.text.toString().isEmpty())
                    Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, getString(R.string.invalid_data_), Toast.LENGTH_SHORT).show()
            }
        }

        signInBtn.setOnClickListener {
            finish()
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}