package com.viamedsalud.gvp.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viamedsalud.gvp.api.methods.EpisodioApi
import com.viamedsalud.gvp.api.response.EpisodioResponse
import com.viamedsalud.gvp.api.response.EpisodiosResponse
import com.viamedsalud.gvp.utils.BaseResponse
import kotlinx.coroutines.delay
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class EpisodioRepository @Inject constructor(private val episodioApi: EpisodioApi) {

    private val _episodioResponseLiveData = MutableLiveData<BaseResponse<EpisodioResponse>>()

    val episodioResponseLiveData: LiveData<BaseResponse<EpisodioResponse>>
        get() = _episodioResponseLiveData

    private val _episodiosResponseLiveData = MutableLiveData<BaseResponse<EpisodiosResponse>>()
    val episodiosLiveData get() = _episodiosResponseLiveData

    suspend fun getEpisodios() {
        _episodiosResponseLiveData.postValue(BaseResponse.Loading())
        val response =episodioApi.getEpisodios()
        delay(2000)
        if (response.isSuccessful && response.body() != null) {
            _episodiosResponseLiveData.postValue(BaseResponse.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _episodiosResponseLiveData.postValue(BaseResponse.Error(errorObj.getString("message")))
        } else {
            _episodiosResponseLiveData.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }

    suspend fun getEpisodio(episodio: String) {
        _episodioResponseLiveData.postValue(BaseResponse.Loading())
        val response =episodioApi.getEpisodio(episodio = episodio)
        delay(2000)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<EpisodioResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _episodioResponseLiveData.postValue(BaseResponse.Success(response.body()))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _episodioResponseLiveData.postValue(BaseResponse.Error(errorObj.getString("message")))
        }
        else{
            _episodioResponseLiveData.postValue(BaseResponse.Error("Algo va mal" + response.message()))
        }
    }
}

