package com.example.arfashion.presentation.app.presentation.forgot_password

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.forgot_password.UserForgotPasswordResponse
import com.example.arfashion.presentation.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel (context: Context) : ViewModel() {
    private val userService = UserService.create(context)

    private val _resultGenerateCodeForgot = MutableLiveData<Boolean>()
    val resultGenerateCodeForgot: LiveData<Boolean>
        get() = _resultGenerateCodeForgot

    private val _generateCodeForgotResponse = MutableLiveData<UserForgotPasswordResponse>()
    val generateCodeForgotResponse: LiveData<UserForgotPasswordResponse>
        get() = _generateCodeForgotResponse

    private val _resultValidateCodeForgot = MutableLiveData<Boolean>()
    val resultValidateCodeForgot: LiveData<Boolean>
        get() = _resultValidateCodeForgot

    private val _validateCodeForgotResponse = MutableLiveData<UserForgotPasswordResponse>()
    val validateCodeForgotResponse : LiveData<UserForgotPasswordResponse>
        get() = _validateCodeForgotResponse

    private val _resultChangePasswordForgot = MutableLiveData<Boolean>()
    val resultChangePasswordForgot: LiveData<Boolean>
        get() = _resultChangePasswordForgot

    private val _changePasswordForgotResponse = MutableLiveData<UserForgotPasswordResponse>()
    val changePasswordForgotResponse : LiveData<UserForgotPasswordResponse>
        get() = _changePasswordForgotResponse

    fun generateCodeForgot(username: String, type: String){
        userService.generateCodeForgot(username, type).enqueue(object : Callback<UserForgotPasswordResponse> {
            override fun onResponse(call: Call<UserForgotPasswordResponse>, response: Response<UserForgotPasswordResponse>) {
                _generateCodeForgotResponse.value = response.body()
                when (response.code()){
                    200 -> _resultGenerateCodeForgot.value = response.isSuccessful
                    else -> _resultGenerateCodeForgot.value = false
                }
            }

            override fun onFailure(call: Call<UserForgotPasswordResponse>, t: Throwable) {
                _resultGenerateCodeForgot.value = false
            }
        })
    }

    fun validateCodeForgot(username: String, code: String, type: String){
        userService.validateCodeForgot(username, code, type).enqueue(object :
            Callback<UserForgotPasswordResponse> {
            override fun onResponse(call: Call<UserForgotPasswordResponse>,
                                    response: Response<UserForgotPasswordResponse>) {
                _validateCodeForgotResponse.value = response.body()
                when (response.code()){
                    200 -> _resultValidateCodeForgot.value = response.isSuccessful
                    else -> _resultValidateCodeForgot.value = false
                }
            }

            override fun onFailure(call: Call<UserForgotPasswordResponse>, t: Throwable) {
                _resultValidateCodeForgot.value = false
            }
        })
    }

    fun changePasswordForgot(token: String, newPassword: String, confirmPassword: String){
        userService.changePasswordForgot("Bearer $token", newPassword, confirmPassword).enqueue(object :
            Callback<UserForgotPasswordResponse> {
            override fun onResponse(call: Call<UserForgotPasswordResponse>,
                                    response: Response<UserForgotPasswordResponse>) {
                _changePasswordForgotResponse.value = response.body()
                when (response.code()){
                    200 -> _resultChangePasswordForgot.value = response.isSuccessful
                    else -> _resultChangePasswordForgot.value = false
                }
            }

            override fun onFailure(call: Call<UserForgotPasswordResponse>, t: Throwable) {
                _resultChangePasswordForgot.value = false
            }
        })
    }

}