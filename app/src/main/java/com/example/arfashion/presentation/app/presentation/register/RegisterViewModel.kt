package com.example.arfashion.presentation.app.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.register.*
import com.example.arfashion.presentation.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
        private val userService: UserService
) : ViewModel() {

    private val _registerResponse = MutableLiveData<UserRegisterResponse>()
    val registerResponse : LiveData<UserRegisterResponse>
        get() = _registerResponse

    private val _resultActive = MutableLiveData<Boolean>()
    val resultActive: LiveData<Boolean>
        get() = _resultActive

    private val _activeResponse = MutableLiveData<UserActiveResponse>()
    val activeResponse : LiveData<UserActiveResponse>
        get() = _activeResponse

    private val _resultResendActiveEmail = MutableLiveData<Boolean>()
    val resultResendActiveEmail: LiveData<Boolean>
        get() = _resultResendActiveEmail

    private val _resendActiveEmailResponse = MutableLiveData<UserResendActiveResponse>()
    val resendActiveEmailResponse : LiveData<UserResendActiveResponse>
        get() = _resendActiveEmailResponse

    private val _resultPhoneRegister = MutableLiveData<Boolean>()
    val resultPhoneRegister: LiveData<Boolean>
        get() = _resultPhoneRegister

    private val _phoneRegisterResponse = MutableLiveData<UserRegisterResponse>()
    val phoneRegisterResponse : LiveData<UserRegisterResponse>
        get() = _phoneRegisterResponse

    private val _resultCheckLogined = MutableLiveData<Boolean>()
    val resultCheckLogined: LiveData<Boolean>
        get() = _resultCheckLogined

    private val _checkPhoneLoginResponse = MutableLiveData<UserCheckPhoneLoginResponse>()
    val checkPhoneLoginResponse : LiveData<UserCheckPhoneLoginResponse>
        get() = _checkPhoneLoginResponse

    private val _resultResendActivePhone = MutableLiveData<Boolean>()
    val resultResendActivePhone: LiveData<Boolean>
        get() = _resultResendActivePhone

    private val _resendActivePhoneResponse = MutableLiveData<UserResendActiveResponse>()
    val resendActivePhoneResponse : LiveData<UserResendActiveResponse>
        get() = _resendActivePhoneResponse

    private val _resultVerifyActivatePhone = MutableLiveData<Boolean>()
    val resultVerifyActivatePhone: LiveData<Boolean>
        get() = _resultVerifyActivatePhone

    private val _verifyActivatePhoneResponse = MutableLiveData<UserVerifyActivatePhoneResponse>()
    val verifyActivatePhoneResponse : LiveData<UserVerifyActivatePhoneResponse>
        get() = _verifyActivatePhoneResponse

    fun register(email: String, password: String, name: String) {
        userService.register(email, password, name).enqueue(object : Callback<UserRegisterResponse> {
            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {
                _registerResponse.value = response.body()
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun registerPhone(phone: String, password: String, name: String) {
        userService.registerPhone(phone, password, name).enqueue(object : Callback<UserRegisterResponse> {
            override fun onResponse(call: Call<UserRegisterResponse>, response: Response<UserRegisterResponse>) {
                _phoneRegisterResponse.value = response.body()
                when (response.code()){
                    200 -> _resultPhoneRegister.value = response.isSuccessful
                    else -> _resultPhoneRegister.value = false
                }
            }

            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                _resultPhoneRegister.value = false
            }
        })
    }

    fun resendActiveEmail(email: String){
        userService.resendEmail(email).enqueue(object : Callback<UserResendActiveResponse> {
            override fun onResponse(call: Call<UserResendActiveResponse>, response: Response<UserResendActiveResponse>) {
                _resendActiveEmailResponse.value = response.body()
                when (response.code()){
                    200 -> _resultResendActiveEmail.value = response.isSuccessful
                    else -> _resultResendActiveEmail.value = false
                }
            }

            override fun onFailure(call: Call<UserResendActiveResponse>, t: Throwable) {
                _resultResendActiveEmail.value = false
            }
        })
    }

    fun resendActivePhone(phone: String){
        userService.resendPhone(phone).enqueue(object : Callback<UserResendActiveResponse> {
            override fun onResponse(call: Call<UserResendActiveResponse>, response: Response<UserResendActiveResponse>) {
                _resendActivePhoneResponse.value = response.body()
                when (response.code()){
                    200 -> _resultResendActivePhone.value = response.isSuccessful
                    else -> _resultResendActivePhone.value = false
                }
            }

            override fun onFailure(call: Call<UserResendActiveResponse>, t: Throwable) {
                _resultResendActivePhone.value = false
            }
        })
    }

    fun active(code: String, email: String){
        userService.active(code, email).enqueue(object : Callback<UserActiveResponse> {
            override fun onResponse(call: Call<UserActiveResponse>, response: Response<UserActiveResponse>) {
                _activeResponse.value = response.body()
                when (response.code()){
                    200 -> _resultActive.value = response.isSuccessful
                    else -> _resultActive.value = false
                }
            }

            override fun onFailure(call: Call<UserActiveResponse>, t: Throwable) {
                _resultActive.value = false
            }
        })
    }

    fun verifyActivePhone(phone: String, code: String){
        userService.verifyActivatePhone(phone, code).enqueue(object : Callback<UserVerifyActivatePhoneResponse> {
            override fun onResponse(call: Call<UserVerifyActivatePhoneResponse>, response: Response<UserVerifyActivatePhoneResponse>) {
                _verifyActivatePhoneResponse.value = response.body()
                when (response.code()){
                    200 -> _resultVerifyActivatePhone.value = response.isSuccessful
                    else -> _resultVerifyActivatePhone.value = false
                }
            }

            override fun onFailure(call: Call<UserVerifyActivatePhoneResponse>, t: Throwable) {
                _resultVerifyActivatePhone.value = false
            }
        })
    }

    fun checkPhoneLogin(phone: String){
        userService.checkPhoneLogin(phone).enqueue(object : Callback<UserCheckPhoneLoginResponse> {
            override fun onResponse(call: Call<UserCheckPhoneLoginResponse>, response: Response<UserCheckPhoneLoginResponse>) {
                _checkPhoneLoginResponse.value = response.body()
                when (response.code()){
                    200 -> _resultCheckLogined.value = response.isSuccessful
                    else -> _resultCheckLogined.value = false
                }
            }

            override fun onFailure(call: Call<UserCheckPhoneLoginResponse>, t: Throwable) {
                _resultCheckLogined.value = false
            }
        })
    }

}