package com.viamedsalud.gvp.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viamedsalud.gvp.api.methods.HClinicaApi
import com.viamedsalud.gvp.api.request.HClinicaRequest
import com.viamedsalud.gvp.api.response.HCLinicasResponse
import com.viamedsalud.gvp.api.response.HClinicaResponse
import com.viamedsalud.gvp.utils.BaseResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class HClinicaRepository @Inject constructor(private val hClinicaApi: HClinicaApi){


    private val _hclinicaResponseLiveData = MutableLiveData<BaseResponse<HClinicaResponse>>()
    val hclinicaResponseLiveData: LiveData<BaseResponse<HClinicaResponse>> get() = _hclinicaResponseLiveData

    private val _hclinicasUserResponseLiveData = MutableLiveData<BaseResponse<HCLinicasResponse>>()
    val hclinicasLiveDataUser get() = _hclinicasUserResponseLiveData

    private val _hclinicasResponseLiveDataEpisodio = MutableLiveData<BaseResponse<HCLinicasResponse>>()
    val hclinicasLiveDataEpisodio get() = _hclinicasResponseLiveDataEpisodio

    private val _hclinicasResponseLiveDataHoy = MutableLiveData<BaseResponse<HCLinicasResponse>>()
    val hclinicasLiveDataHoy get() = _hclinicasResponseLiveDataHoy

    suspend fun createHClinica(hClinicaRequest: HClinicaRequest) {
        _hclinicaResponseLiveData.postValue(BaseResponse.Loading())
        val response =hClinicaApi.createHClinica(hClinicaRequest)
        handleResponse(response)
    }

    suspend fun getHClinicasUser(idUser: Int) {
        _hclinicasUserResponseLiveData.postValue(BaseResponse.Loading())
        val response =hClinicaApi.getHClinicasUser(idUser)
        if (response.isSuccessful && response.body() != null) {
            _hclinicasUserResponseLiveData.postValue(BaseResponse.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _hclinicasUserResponseLiveData.postValue(BaseResponse.Error(errorObj.getString("message")))
        } else {
            _hclinicasUserResponseLiveData.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }

    suspend fun getHClinicasEpisodio(episodio: String) {
        _hclinicasResponseLiveDataEpisodio.postValue(BaseResponse.Loading())
        val response =hClinicaApi.getHClinicasEpisodio(episodio)
        if (response.isSuccessful && response.body() != null) {
            _hclinicasResponseLiveDataEpisodio.postValue(BaseResponse.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _hclinicasResponseLiveDataEpisodio.postValue(BaseResponse.Error(errorObj.getString("message")))
        } else {
            _hclinicasResponseLiveDataEpisodio.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }

    suspend fun getAllHClinicas() {
        _hclinicasResponseLiveDataHoy.postValue(BaseResponse.Loading())
        val response =hClinicaApi.getAllHClinicas()
        if (response.isSuccessful && response.body() != null) {
            _hclinicasResponseLiveDataHoy.postValue(BaseResponse.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _hclinicasResponseLiveDataHoy.postValue(BaseResponse.Error(errorObj.getString("message")))
        } else {
            _hclinicasResponseLiveDataHoy.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }

    private fun handleResponse(response: Response<HClinicaResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _hclinicaResponseLiveData.postValue(BaseResponse.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _hclinicaResponseLiveData.postValue(BaseResponse.Error(errorObj.getString("message")))
        }
        else{
            _hclinicaResponseLiveData.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }
}