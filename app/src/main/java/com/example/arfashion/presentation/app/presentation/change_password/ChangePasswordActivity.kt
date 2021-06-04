package com.example.arfashion.presentation.app.presentation.change_password

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.services.UserService
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.layout_back_save_header.*

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.change_password_label)
        onNavigateBack()

        changePasswordViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChangePasswordViewModel(userService) as T
            }
        })[ChangePasswordViewModel::class.java]

        initView()
        initViewModel()

    }

    private fun initViewModel() {

        changePasswordViewModel.resultChangePassword.observe(this) {
            if (it) {
                val response = changePasswordViewModel.changePasswordResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {

        iv_toggle_show_curr_pass.setOnClickListener {
            if(iv_toggle_show_curr_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_show_curr_pass.setImageResource(R.drawable.toggle_password_open)
                currentPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                currentPasswordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_show_curr_pass.setImageResource(R.drawable.toggle_password_close)
                currentPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                currentPasswordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        iv_toggle_show_new_pass.setOnClickListener {
            if(iv_toggle_show_new_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState){
                iv_toggle_show_new_pass.setImageResource(R.drawable.toggle_password_open)
                newPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                newPasswordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_show_new_pass.setImageResource(R.drawable.toggle_password_close)
                newPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                newPasswordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        iv_toggle_show_confirm_pass.setOnClickListener {
            if(iv_toggle_show_confirm_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState) {
                iv_toggle_show_confirm_pass.setImageResource(R.drawable.toggle_password_open)
                confirmPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEdt.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                iv_toggle_show_confirm_pass.setImageResource(R.drawable.toggle_password_close)
                confirmPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        currentPasswordEdt.isEnabled = false

        var newPass: String = newPasswordEdt.text.toString()
        var confPass: String = confirmPasswordEdt.text.toString()

        check_icon.setOnClickListener {
            if(newPass.isEmpty() || confPass.isEmpty())
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            else if(newPass != confPass)
                Toast.makeText(this, getString(R.string.confirm_password_fail), Toast.LENGTH_SHORT).show()
            else changePasswordViewModel.changePassword("", newPass, "")
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}