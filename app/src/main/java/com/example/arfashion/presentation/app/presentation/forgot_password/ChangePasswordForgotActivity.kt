package com.example.arfashion.presentation.app.presentation.forgot_password

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.activity_change_forgot_password.*
import kotlinx.android.synthetic.main.layout_back_save_header.*

class ChangePasswordForgotActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ForgotPasswordViewModel

    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_forgot_password)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.change_password_label)
        onNavigateBack()
        accessToken = intent.getStringExtra("accessToken").toString()

        changePasswordViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(ForgotPasswordViewModel::class.java)

        initView()
        initViewModel()

    }

    private fun initViewModel() {

        changePasswordViewModel.resultChangePasswordForgot.observe(this) {
            if (it) {
                val response = changePasswordViewModel.changePasswordForgotResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {

        iv_toggle_show_pass_forgot.setOnClickListener {
            if(iv_toggle_show_pass_forgot.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState){
                iv_toggle_show_pass_forgot.setImageResource(R.drawable.toggle_password_open)
                newPasswordForgotEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                newPasswordForgotEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_show_pass_forgot.setImageResource(R.drawable.toggle_password_close)
                newPasswordForgotEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                newPasswordForgotEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        iv_toggle_show_confirm_pass_forgot.setOnClickListener {
            if(iv_toggle_show_confirm_pass_forgot.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_show_confirm_pass_forgot.setImageResource(R.drawable.toggle_password_open)
                confirmPasswordForgotEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordForgotEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_show_confirm_pass_forgot.setImageResource(R.drawable.toggle_password_close)
                confirmPasswordForgotEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordForgotEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        check_icon.setOnClickListener {

            val newPass: String = newPasswordForgotEdt.text.toString()
            val confPass: String = confirmPasswordForgotEdt.text.toString()

            if(newPass.isEmpty() || confPass.isEmpty())
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            else if(newPass != confPass)
                Toast.makeText(this, getString(R.string.confirm_password_fail), Toast.LENGTH_SHORT).show()
            else changePasswordViewModel.changePasswordForgot(accessToken, newPass, confPass)
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}