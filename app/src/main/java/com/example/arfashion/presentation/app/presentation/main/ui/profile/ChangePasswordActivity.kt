package com.example.arfashion.presentation.app.presentation.main.ui.profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.model.User
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.layout_back_save_header.*

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var user: User

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private lateinit var profileViewModel: ProfileViewModel

    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.change_password_label)
        onNavigateBack()

        pref = this.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        user = userStorage.load()
        password = userStorage.loadPassword()

        profileViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(ProfileViewModel::class.java)

        initView()
        initViewModel()

    }

    private fun initViewModel() {
        profileViewModel.resultChangePassword.observe(this) {
            if (it) {
                val response = profileViewModel.changePasswordResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    userStorage.savePassword(newPasswordEdt.text.toString())
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {

        iv_toggle_show_curr_pass.setOnClickListener {
            if(iv_toggle_show_curr_pass.drawable.constantState == getDrawable(R.drawable.toggle_password_close)?.constantState){
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

        check_icon.setOnClickListener {

            val curPass: String = currentPasswordEdt.text.toString()
            val newPass: String = newPasswordEdt.text.toString()
            val confPass: String = confirmPasswordEdt.text.toString()

            if(newPass.isEmpty() || confPass.isEmpty() || curPass.isEmpty())
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            else if(curPass != password)
                Toast.makeText(this, getString(R.string.invalid_old_password), Toast.LENGTH_SHORT).show()
            else if(newPass != confPass)
                Toast.makeText(this, getString(R.string.confirm_password_fail), Toast.LENGTH_SHORT).show()
            else {
                profileViewModel.changePassword( curPass, newPass, confPass )
            }
        }

    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}