package com.example.arfashion.presentation.app.presentation.register

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.register.UserRegisterResponse
import com.example.arfashion.presentation.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
        private val userService: UserService
) : ViewModel() {

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean>
        get() = _result

    private val _registerResponse = MutableLiveData<UserRegisterResponse>()
    val registerResponse : LiveData<UserRegisterResponse>
        get() = _registerResponse

    fun register(email: String, password: String, name: String) {
        userService.register(email, password, name).enqueue(object : Callback<UserRegisterResponse> {
            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {
                _result.value = response.isSuccessful
                if(_result.value == true)
                    _registerResponse.value = response.body()

            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                _result.value = false
            }

        })
    }

}