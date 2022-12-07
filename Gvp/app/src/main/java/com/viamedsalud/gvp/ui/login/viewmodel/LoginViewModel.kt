package com.viamedsalud.gvp.ui.login.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viamedsalud.gvp.api.request.UserRequest
import com.viamedsalud.gvp.api.response.UserResponse
import com.viamedsalud.gvp.repository.UserRepository
import com.viamedsalud.gvp.utils.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<BaseResponse<UserResponse>>
    get() = userRepository.userResponseLiveData

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun getUser(idUser : Int){
        viewModelScope.launch {
            userRepository.getUser(idUser)
        }
    }

    fun validateCredentials(userName: String, password: String, isLogin: Boolean) : Pair<Boolean, String> {
        var result = Pair(true, "")
        if((!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password)){
            result = Pair(false, "Porfavor rellene los campos")
        }
        return result
    }

}