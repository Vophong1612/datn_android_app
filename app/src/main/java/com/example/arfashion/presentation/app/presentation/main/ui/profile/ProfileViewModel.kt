package com.example.arfashion.presentation.app.presentation.main.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.forgot_password.UserForgotPasswordResponse
import com.example.arfashion.presentation.app.models.login.UserLoginResponse
import com.example.arfashion.presentation.app.models.profile.AvatarResponse
import com.example.arfashion.presentation.app.models.profile.ProfileResponse
import com.example.arfashion.presentation.services.UserService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(context: Context) : ViewModel() {
    private val userService = UserService.create(context)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val _resultUpdateProfile = MutableLiveData<Boolean>()
    val resultUpdateProfile: LiveData<Boolean>
        get() = _resultUpdateProfile

    private val _updateProfileResponse = MutableLiveData<UserLoginResponse>()
    val updateProfileResponse : LiveData<UserLoginResponse>
        get() = _updateProfileResponse

    private val _resultUploadAvatar = MutableLiveData<Boolean>()
    val resultUploadAvatar: LiveData<Boolean>
        get() = _resultUploadAvatar

    private val _uploadAvatarResponse = MutableLiveData<AvatarResponse>()
    val uploadAvatarResponse : LiveData<AvatarResponse>
        get() = _uploadAvatarResponse

    private val _resultGetProfile = MutableLiveData<Boolean>()
    val resultGetProfile: LiveData<Boolean>
        get() = _resultGetProfile

    private val _getProfileResponse = MutableLiveData<ProfileResponse>()
    val getProfileResponse : LiveData<ProfileResponse>
        get() = _getProfileResponse

    private val _resultChangePassword = MutableLiveData<Boolean>()
    val resultChangePassword: LiveData<Boolean>
        get() = _resultChangePassword

    private val _changePasswordResponse = MutableLiveData<UserForgotPasswordResponse>()
    val changePasswordResponse : LiveData<UserForgotPasswordResponse>
        get() = _changePasswordResponse

    fun updateProfile(name: String, email: String, birthday: String, gender: Int) {
        _loading.value = true
        userService.updateProfile(name, email, birthday, gender)
            .enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                _updateProfileResponse.value = response.body()
                when (response.code()){
                    200 -> _resultUpdateProfile.value = response.isSuccessful
                    else -> _resultUpdateProfile.value = false
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                _resultUpdateProfile.value = false
                _loading.value = false
            }
        })
    }

    fun uploadAvatar(avt: MultipartBody.Part) {
        _loading.value = true
        userService.uploadAvatar(avt)
            .enqueue(object : Callback<AvatarResponse> {
                override fun onResponse(
                    call: Call<AvatarResponse>,
                    response: Response<AvatarResponse>
                ) {
                    _uploadAvatarResponse.value = response.body()
                    when (response.code()){
                        200 -> _resultUploadAvatar.value = response.isSuccessful
                        else -> _resultUploadAvatar.value = false
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<AvatarResponse>, t: Throwable) {
                    _resultUploadAvatar.value = false
                    _loading.value = false
                }
            })
    }

    fun getProfile() {
        userService.getProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    _getProfileResponse.value = response.body()
                    when (response.code()){
                        200 -> _resultGetProfile.value = response.isSuccessful
                        else -> _resultGetProfile.value = false
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    _resultGetProfile.value = false
                }
            })
    }

    fun changePassword(oldPass: String, newPass: String, confPass: String) {
        userService.changePasswordProfile(oldPass, newPass, confPass)
            .enqueue(object : Callback<UserForgotPasswordResponse> {
                override fun onResponse(
                    call: Call<UserForgotPasswordResponse>,
                    response: Response<UserForgotPasswordResponse>
                ) {
                    _changePasswordResponse.value = response.body()
                    when (response.code()){
                        200 -> _resultChangePassword.value = response.isSuccessful
                        else -> _resultChangePassword.value = false
                    }
                }

                override fun onFailure(call: Call<UserForgotPasswordResponse>, t: Throwable) {
                    _resultChangePassword.value = false
                }
            })
    }
}