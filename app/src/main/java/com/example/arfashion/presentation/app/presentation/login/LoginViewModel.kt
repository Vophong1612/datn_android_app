package com.example.arfashion.presentation.app.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean>
        get() = _result

    private val _loginResponse = MutableLiveData<UserLoginResponse>()
    val loginResponse : LiveData<UserLoginResponse>
    get() = _loginResponse

    fun login(email: String, password: String) {
        userService.login(email, password).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                _result.value = response.isSuccessful
                if (_result.value == true) {
                    _loginResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                _result.value = false
            }

        })
    }

}