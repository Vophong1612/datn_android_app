package com.example.arfashion.presentation.app.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.forgot_password.ForgotPasswordActivity
import com.example.arfashion.presentation.app.presentation.main.MainActivity
import com.example.arfashion.presentation.app.presentation.register.RegisterEmailOrPhoneActivity
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_or.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val codeSignInGoogle: Int = 100

    private lateinit var mGoogleSignInClient : GoogleSignInClient

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var loginViewModel: LoginViewModel

    private val userService = UserService.create()

    private lateinit var mCallbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {
        GoogleSignIn.getLastSignedInAccount(application)
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_in)
        onNavigateBack()
        mCallbackManager = CallbackManager.Factory.create()

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

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, getString(R.string.incorrect_data_login), Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.result.observe(this) {
            if (it) {
                val response = loginViewModel.loginResponse.value
                response?.let { userRes ->
                    userManager.currentUser = User(Profile(), Credential(userRes.accessToken))

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, getString(R.string.incorrect_data_login), Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.resultLoginGoogle.observe(this) {
            if (it) {
                val response = loginViewModel.loginGoogleResponse.value
                response?.let { userRes ->
                    userManager.currentUser = User(Profile(), Credential(userRes.accessToken))
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.resultLoginFacebook.observe(this) {
            if (it) {
                val response = loginViewModel.loginFacebookResponse.value
                response?.let { userRes ->
                    userManager.currentUser = User(Profile(), Credential(userRes.accessToken))
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {

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

        signInBtn.setOnClickListener {
            val username = usernameEdt.text.toString()
            val password = passwordEdt.text.toString()
            if (Utils.isValidEmail(username)){
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    loginViewModel.login(username, password)
                } else {
                    Toast.makeText(this, getString(R.string.invalid_data_login), Toast.LENGTH_SHORT).show()
                }
            }else if (Utils.isValidPhone(username)) {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    loginViewModel.loginPhone(Utils.formatPhone(username), password)
                } else {
                    Toast.makeText(this, getString(R.string.invalid_data_login), Toast.LENGTH_SHORT).show()
                }
            } else  Toast.makeText(this, getString(R.string.invalid_data_login), Toast.LENGTH_SHORT).show()
        }

        facebookLoginBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    loginViewModel.loginFacebook(loginResult.accessToken.token.toString(), loginResult.accessToken.userId.toString())
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(e: FacebookException) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            })

        googleLoginBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, codeSignInGoogle)
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterEmailOrPhoneActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == codeSignInGoogle) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    @SuppressLint("LogNotTimber")
    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account: GoogleSignInAccount? = task?.let {
                task.getResult(ApiException::class.java)
            }
            if (account != null) {
                account.idToken?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    loginViewModel.loginGoogle(it)
                }
            }
        } catch (e: ApiException) {
            Log.w("TAG", "si" +
                    "gnInResult:failed code=" + e.statusCode)
        }
    }

}