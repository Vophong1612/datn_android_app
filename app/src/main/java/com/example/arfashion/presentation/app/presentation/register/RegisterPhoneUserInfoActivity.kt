package com.example.arfashion.presentation.app.presentation.register

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
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_register_email_or_phone.*
import kotlinx.android.synthetic.main.activity_register_email_user_info.*
import kotlinx.android.synthetic.main.activity_register_email_user_info.confirmPasswordEdt
import kotlinx.android.synthetic.main.activity_register_email_user_info.fullNameEdt
import kotlinx.android.synthetic.main.activity_register_email_user_info.passwordEdt
import kotlinx.android.synthetic.main.activity_register_phone_user_info.*
import kotlinx.android.synthetic.main.layout_back_save_header.*

class RegisterPhoneUserInfoActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_phone_user_info)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up_)
        onNavigateBack()

        registerViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(
            RegisterViewModel::class.java)

        initView(intent)
        initViewModel()
    }

    private fun initViewModel() {

        registerViewModel.loading.observe(this) {
            refreshLayoutRegisterPhone.isRefreshing = it
        }
        
        registerViewModel.resultPhoneRegister.observe(this) {
            if (it) {
                val response = registerViewModel.phoneRegisterResponse.value
                val intent = Intent(this@RegisterPhoneUserInfoActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView(intentEmail: Intent) {

        refreshLayoutRegisterPhone.setOnRefreshListener {
            refreshLayoutRegisterPhone.isRefreshing = false
        }

        phoneEdt.setText(intentEmail.getStringExtra("strPhone"))

        iv_toggle_phone_pass.setOnClickListener {
            if(iv_toggle_phone_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_phone_pass.setImageResource(R.drawable.toggle_password_open)
                passwordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_phone_pass.setImageResource(R.drawable.toggle_password_close)
                passwordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        iv_toggle_phone_conf_pass.setOnClickListener {
            if(iv_toggle_phone_conf_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_phone_conf_pass.setImageResource(R.drawable.toggle_password_open)
                confirmPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_phone_conf_pass.setImageResource(R.drawable.toggle_password_close)
                confirmPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        //set event
        check_icon.setOnClickListener {
            val phone = phoneEdt.text.toString()
            val password = passwordEdt.text.toString()
            val confirmPassword = confirmPasswordEdt.text.toString()
            val name = fullNameEdt.text.toString()

            if(password != confirmPassword)
                Toast.makeText(this,getString(R.string.confirm_password_fail),Toast.LENGTH_SHORT).show()
            else if (!Utils.isValidPassword(password))
                Toast.makeText(this,getString(R.string.invalid_password),Toast.LENGTH_SHORT).show()
            else if (name.isEmpty())
                Toast.makeText(this,getString(R.string.invalid_data),Toast.LENGTH_SHORT).show()
            else registerViewModel.registerPhone(phone, password, name)
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}