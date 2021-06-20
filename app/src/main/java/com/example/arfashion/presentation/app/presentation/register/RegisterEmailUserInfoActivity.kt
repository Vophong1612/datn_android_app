package com.example.arfashion.presentation.app.presentation.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_email_user_info.*
import kotlinx.android.synthetic.main.activity_register_email_user_info.iv_toggle_pass
import kotlinx.android.synthetic.main.activity_register_email_user_info.passwordEdt
import kotlinx.android.synthetic.main.layout_back_save_header.*
import java.util.*

class RegisterEmailUserInfoActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email_user_info)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up)
        onNavigateBack()

        registerViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(
            RegisterViewModel::class.java)

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView(intentEmail: Intent) {

        emailEdt.setText(intentEmail.getStringExtra("strEmail"))

        iv_toggle_pass.setOnClickListener {
            if(iv_toggle_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_pass.setImageResource(R.drawable.toggle_password_open)
                passwordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_pass.setImageResource(R.drawable.toggle_password_close)
                passwordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        iv_toggle_conf_pass.setOnClickListener {
            if(iv_toggle_conf_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_conf_pass.setImageResource(R.drawable.toggle_password_open)
                confirmPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_conf_pass.setImageResource(R.drawable.toggle_password_close)
                confirmPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

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