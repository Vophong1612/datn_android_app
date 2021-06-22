package com.example.arfashion.presentation.app.presentation.register

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.app.presentation.login.LoginViewModel
import com.example.arfashion.presentation.app.presentation.main.MainActivity
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
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
import kotlinx.android.synthetic.main.activity_register_email_or_phone.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.android.synthetic.main.layout_or.*

class RegisterEmailOrPhoneActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var mCallbackManager: CallbackManager

    private val codeSignInGoogle: Int = 100

    private lateinit var mGoogleSignInClient : GoogleSignInClient

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email_or_phone)
        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        init()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        verifyBtn.setOnClickListener {
            if (Utils.isValidEmail(emailSignUpEdt.text.toString())){
                val intent = Intent(this@RegisterEmailOrPhoneActivity, RegisterEmailUserInfoActivity::class.java)
                intent.putExtra("strEmail",emailSignUpEdt.text.toString())
                startActivity(intent)
            } else if (Utils.isValidPhone(emailSignUpEdt.text.toString())){
                registerViewModel.checkPhoneLogin(Utils.formatPhone(emailSignUpEdt.text.toString()))
            } else Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this@RegisterEmailOrPhoneActivity, LoginActivity::class.java)
            startActivity(intent)
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
                Toast.makeText(this@RegisterEmailOrPhoneActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: FacebookException) {
                Toast.makeText(this@RegisterEmailOrPhoneActivity, e.message, Toast.LENGTH_LONG).show()
            }
        })

        googleLoginBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, codeSignInGoogle)
        }
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

    private fun init() {
        screen_name.text = this.getString(R.string.sign_up)
        onNavigateBack()
        mCallbackManager = CallbackManager.Factory.create()

        registerViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(
            RegisterViewModel::class.java)

        loginViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(LoginViewModel::class.java)

        initViewModel()
    }

    private fun initViewModel() {
        registerViewModel.resultCheckLogined.observe(this) {
            if (it) {
                val response = registerViewModel.checkPhoneLoginResponse.value
                if (response != null) {
                    if (response.status_code == 1) {
                        Toast.makeText(this, getString(R.string.existed_phone), Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@RegisterEmailOrPhoneActivity, RegisterVerifyActivity::class.java)
                        intent.putExtra("strPhone",Utils.formatPhone(emailSignUpEdt.text.toString()))
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.resultLoginGoogle.observe(this) {
            if (it) {
                val response = loginViewModel.loginGoogleResponse.value
                response?.let { userRes ->
                    Utils.initData(userRes.user)
                    userManager.currentUser = User(Profile(userRes.user.id, userRes.user.name,
                        userRes.user.email, userRes.user.phone, userRes.user.avatar,
                        userRes.user.gender, userRes.user.birthday), Credential(userRes.accessToken))
                    userStorage.save(userManager.currentUser)
                    userStorage.savePassword("")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.resultLoginFacebook.observe(this) {
            if (it) {
                val response = loginViewModel.loginFacebookResponse.value
                response?.let { userRes ->
                    Utils.initData(userRes.user)
                    userManager.currentUser = User(Profile(userRes.user.id, userRes.user.name,
                        userRes.user.email, userRes.user.phone, userRes.user.avatar,
                        userRes.user.gender, userRes.user.birthday), Credential(userRes.accessToken))
                    userStorage.save(userManager.currentUser)
                    userStorage.savePassword("")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}