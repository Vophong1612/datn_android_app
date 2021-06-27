package com.example.arfashion.presentation.app.presentation.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.login.UserLoginFacebookResponse
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.loginstatus_code.UserLoginGoogleResponse
import com.example.arfashion.presentation.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(context: Context) : ViewModel() {
    private val userService = UserService.create(context)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean>
        get() = _result

    private val _loginResponse = MutableLiveData<UserLoginResponse>()
    val loginResponse : LiveData<UserLoginResponse>
    get() = _loginResponse

    private val _resultLoginGoogle = MutableLiveData<Boolean>()
    val resultLoginGoogle: LiveData<Boolean>
        get() = _resultLoginGoogle

    private val _loginGoogleResponse = MutableLiveData<UserLoginGoogleResponse>()
    val loginGoogleResponse : LiveData<UserLoginGoogleResponse>
        get() = _loginGoogleResponse

    private val _resultLoginFacebook = MutableLiveData<Boolean>()
    val resultLoginFacebook: LiveData<Boolean>
        get() = _resultLoginFacebook

    private val _loginFacebookResponse = MutableLiveData<UserLoginFacebookResponse>()
    val loginFacebookResponse : LiveData<UserLoginFacebookResponse>
        get() = _loginFacebookResponse

    fun login(email: String, password: String) {
        _loading.value = true
        userService.login(email, password).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                _loginResponse.value = response.body()
                when (response.code()){
                    200 -> _result.value = response.isSuccessful
                    else -> _result.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                _result.value = false
                _loading.value = false
            }
        })
    }

    fun loginPhone(phone: String, password: String) {
        _loading.value = true
        userService.loginPhone(phone, password).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
            ) {
                _loginResponse.value = response.body()
                when (response.code()){
                    200 -> _result.value = response.isSuccessful
                    else -> _result.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                _result.value = false
                _loading.value = false
            }
        })
    }

    fun loginGoogle(idToken: String) {
        _loading.value = true
        userService.loginGoogle(idToken).enqueue(object : Callback<UserLoginGoogleResponse> {
            override fun onResponse(
                    call: Call<UserLoginGoogleResponse>,
                    response: Response<UserLoginGoogleResponse>
            ) {
                _loginGoogleResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultLoginGoogle.value = response.isSuccessful
                    else -> _resultLoginGoogle.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<UserLoginGoogleResponse>, t: Throwable) {
                _resultLoginGoogle.value = false
                _loading.value = false
            }
        })
    }

    fun loginFacebook(accessToken: String, userId: String) {
        _loading.value = true
        userService.loginFacebook(accessToken, userId).enqueue(object : Callback<UserLoginFacebookResponse> {
            override fun onResponse(
                    call: Call<UserLoginFacebookResponse>,
                    response: Response<UserLoginFacebookResponse>
            ) {
                _loginFacebookResponse.value = response.body()
                when (response.code()) {
                    200 -> _resultLoginFacebook.value = response.isSuccessful
                    else -> _resultLoginFacebook.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<UserLoginFacebookResponse>, t: Throwable) {
                _resultLoginFacebook.value = false
                _loading.value = false
            }
        })
    }

}