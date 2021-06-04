package com.example.arfashion.presentation.app.presentation.change_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.change_password.UserChangePasswordResponse
import com.example.arfashion.presentation.app.models.register.*
import com.example.arfashion.presentation.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel (
    private val userService: UserService
) : ViewModel() {

    private val _resultSendCode = MutableLiveData<Boolean>()
    val resultSendCode: LiveData<Boolean>
        get() = _resultSendCode

    private val _sendCodeResponse = MutableLiveData<UserResendActiveResponse>()
    val sendCodeResponse : LiveData<UserResendActiveResponse>
        get() = _sendCodeResponse

    private val _resultVerifyCode = MutableLiveData<Boolean>()
    val resultVerifyCode: LiveData<Boolean>
        get() = _resultVerifyCode

    private val _verifyCodeResponse = MutableLiveData<UserVerifyActivatePhoneResponse>()
    val verifyCodeResponse : LiveData<UserVerifyActivatePhoneResponse>
        get() = _verifyCodeResponse

    private val _resultChangePassword = MutableLiveData<Boolean>()
    val resultChangePassword: LiveData<Boolean>
        get() = _resultChangePassword

    private val _changePasswordResponse = MutableLiveData<UserChangePasswordResponse>()
    val changePasswordResponse : LiveData<UserChangePasswordResponse>
        get() = _changePasswordResponse

    fun sendCode(phone: String){
        userService.resendPhone(phone).enqueue(object : Callback<UserResendActiveResponse> {
            override fun onResponse(call: Call<UserResendActiveResponse>, response: Response<UserResendActiveResponse>) {
                _sendCodeResponse.value = response.body()
                when (response.code()){
                    200 -> _resultSendCode.value = response.isSuccessful
                    else -> _resultSendCode.value = false
                }
            }

            override fun onFailure(call: Call<UserResendActiveResponse>, t: Throwable) {
                _resultSendCode.value = false
            }
        })
    }

    fun verifyCode(phone: String, code: String){
        userService.verifyActivatePhone(phone, code).enqueue(object :
            Callback<UserVerifyActivatePhoneResponse> {
            override fun onResponse(call: Call<UserVerifyActivatePhoneResponse>, response: Response<UserVerifyActivatePhoneResponse>) {
                _verifyCodeResponse.value = response.body()
                when (response.code()){
                    200 -> _resultVerifyCode.value = response.isSuccessful
                    else -> _resultVerifyCode.value = false
                }
            }

            override fun onFailure(call: Call<UserVerifyActivatePhoneResponse>, t: Throwable) {
                _resultVerifyCode.value = false
            }
        })
    }

    fun changePassword(token: String, newPassword: String, oldPassword: String){
        userService.changePassword(token, newPassword, oldPassword).enqueue(object :
            Callback<UserChangePasswordResponse> {
            override fun onResponse(call: Call<UserChangePasswordResponse>, response: Response<UserChangePasswordResponse>) {
                _changePasswordResponse.value = response.body()
                when (response.code()){
                    200 -> _resultChangePassword.value = response.isSuccessful
                    else -> _resultChangePassword.value = false
                }
            }

            override fun onFailure(call: Call<UserChangePasswordResponse>, t: Throwable) {
                _resultChangePassword.value = false
            }
        })
    }


}