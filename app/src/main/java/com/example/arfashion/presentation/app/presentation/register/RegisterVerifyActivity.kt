package com.example.arfashion.presentation.app.presentation.register

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.app.presentation.login.LoginViewModel
import com.example.arfashion.presentation.app.presentation.main.MainActivity
import com.example.arfashion.presentation.app.presentation.register.otp.GenericKeyEvent
import com.example.arfashion.presentation.app.presentation.register.otp.OtpEditText
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.UserService
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
import kotlinx.android.synthetic.main.activity_register_verify.*
import kotlinx.android.synthetic.main.activity_register_verify.verifyBtn
import kotlinx.android.synthetic.main.layout_back_save_header.*
import kotlinx.android.synthetic.main.layout_or.*
import kotlinx.android.synthetic.main.layout_otp.*

class RegisterVerifyActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var mCallbackManager: CallbackManager

    private val codeSignInGoogle: Int = 100

    private lateinit var mGoogleSignInClient : GoogleSignInClient

    private val userService = UserService.create()

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private var email: String = ""

    private var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_verify)
        init(intent)

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
        val acct = GoogleSignIn.getLastSignedInAccount(application)
        if (acct != null) {

        }
    }

    private fun init(intentRes: Intent) {
        screen_name.text = this.getString(R.string.sign_up)
        email = intentRes.getStringExtra("strEmail").toString()
        phone = intentRes.getStringExtra("strPhone").toString()
        onNavigateBack()
        mCallbackManager = CallbackManager.Factory.create()

        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()

        registerViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(userService) as T
            }
        })[RegisterViewModel::class.java]

        loginViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(userService) as T
            }
        })[LoginViewModel::class.java]

        initOtp()
        initViewModel()

        signUpBtn.setOnClickListener {
            val intent = Intent(this@RegisterVerifyActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        tvResend.setOnClickListener {
            if(phone.isEmpty()){
                registerViewModel.resendActiveEmail(email)
            }else{
                registerViewModel.resendActivePhone(phone)
            }
        }

        verifyBtn.setOnClickListener {
            val activeCode: String = first.text.toString() + second.text.toString() + third.text.toString() + fourth.text.toString() + fifth.text.toString() + sixth.text.toString()
            if(phone.isEmpty()){
                registerViewModel.active(activeCode, email)
            }else{
                registerViewModel.verifyActivePhone(phone, activeCode)
            }
        }

        facebookLoginBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("TAG", "Success Login")
                loginViewModel.loginFacebook(loginResult.accessToken.token.toString(), loginResult.accessToken.userId.toString())
            }

            override fun onCancel() {
                Toast.makeText(this@RegisterVerifyActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: FacebookException) {
                Toast.makeText(this@RegisterVerifyActivity, e.message, Toast.LENGTH_LONG).show()
            }
        })

        googleLoginBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, codeSignInGoogle)
        }
    }

    private fun initViewModel() {
        registerViewModel.resultResendActiveEmail.observe(this) {
            if (it) {
                val response = registerViewModel.resendActiveEmailResponse.value
                if (response != null) {
                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.resultActive.observe(this) {
            if (it) {
                val response = registerViewModel.activeResponse.value
                if (response != null) {
                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this,getString(R.string.incorrect_active_code), Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.resultResendActivePhone.observe(this) {
            if (it) {
                val response = registerViewModel.resendActivePhoneResponse.value
                if (response != null) {
                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.resultVerifyActivatePhone.observe(this) {
            if (it) {
                val response = registerViewModel.verifyActivatePhoneResponse.value
                if (response != null) {
                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, RegisterPhoneUserInfoActivity::class.java)
                    intent.putExtra("strPhone",phone)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this,getString(R.string.incorrect_active_code), Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.resultLoginGoogle.observe(this) {
            if (it) {
                val response = loginViewModel.loginGoogleResponse.value
                response?.let { userRes ->
                    userManager.currentUser = User(Profile(), Credential(userRes.accessToken))
                    val intent = Intent(this, MainActivity::class.java)
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
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
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

    private fun initOtp() {
        first.addTextChangedListener(OtpEditText(first, this))
        second.addTextChangedListener(OtpEditText(second, this))
        third.addTextChangedListener(OtpEditText(third, this))
        fourth.addTextChangedListener(OtpEditText(fourth, this))
        fifth.addTextChangedListener(OtpEditText(fifth, this))
        sixth.addTextChangedListener(OtpEditText(sixth, this))

        first.setOnKeyListener(GenericKeyEvent(first, null))
        second.setOnKeyListener(GenericKeyEvent(second, first))
        third.setOnKeyListener(GenericKeyEvent(third, second))
        fourth.setOnKeyListener(GenericKeyEvent(first, third))
        fifth.setOnKeyListener(GenericKeyEvent(first, fourth))
        sixth.setOnKeyListener(GenericKeyEvent(first, fifth))
    }

}