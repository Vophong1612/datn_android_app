package com.example.arfashion.presentation.app.presentation.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.main.MainActivity
import com.example.arfashion.presentation.app.presentation.register.RegisterVerifiyActivity
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_back_header.*

class LoginActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var loginViewModel: LoginViewModel

    private val userService = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_in)
        onNavigateBack()

        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()

        loginViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(userService) as T
            }
        })[LoginViewModel::class.java]

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        loginViewModel.result.observe(this) {
            if (it) {
                val response = loginViewModel.loginResponse.value
                response?.let { userRes ->
                    userManager.currentUser = User(Profile(), Credential(userRes.accessToken))
                    Toast.makeText(this, userRes.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        signInBtn.setOnClickListener {
            val email = usernameEdt.text.toString()
            val password = passwordEdt.text.toString()
            if (Utils.isValidEmail(email) && Utils.isValidPassword(password)) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }


//    private fun setupAgreementText() {
//        val termsText = this.getString(R.string.terms_link)
//        val termsAgreement = this.getString(R.string.terms_of_use_agreement, termsText)
//        val start = termsAgreement.indexOf(termsText)
//        val end = start + termsText.length
//        val termsSpan = SpannableString(termsAgreement)
//        termsSpan.setSpan(
//            URLSpan(this.getString(R.string.terms_url)),
//            start,
//            end,
//            Spanned.SPAN_INCLUSIVE_INCLUSIVE
//        )
//        termsAgreementLabel.text = termsSpan
//        termsAgreementLabel.movementMethod = LinkMovementMethod.getInstance()
//    }
}