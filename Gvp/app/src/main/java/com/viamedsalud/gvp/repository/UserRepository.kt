package com.viamedsalud.gvp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viamedsalud.gvp.api.methods.UserApi
import com.viamedsalud.gvp.api.request.UserRequest
import com.viamedsalud.gvp.api.response.UserResponse
import com.viamedsalud.gvp.utils.BaseResponse
import kotlinx.coroutines.delay
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserApi) {

    private val _userResponseLiveData = MutableLiveData<BaseResponse<UserResponse>>()
    val userResponseLiveData: LiveData<BaseResponse<UserResponse>> get() = _userResponseLiveData



    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(BaseResponse.Loading())
        val response =userAPI.loginUser(userRequest)
        delay(2000)
        handleResponse(response)
    }

    suspend fun getUser(idUser: Int) {
        _userResponseLiveData.postValue(BaseResponse.Loading())
        val response =userAPI.getUser(id = idUser)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(BaseResponse.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(BaseResponse.Error(errorObj.getString("message")))
        }
        else{
            _userResponseLiveData.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }
}