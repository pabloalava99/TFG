package com.viamedsalud.gvp.ui.episodio.viewmodel

import androidx.lifecycle.*
import com.viamedsalud.gvp.api.request.HClinicaRequest
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.api.response.HClinicaResponse
import com.viamedsalud.gvp.repository.HClinicaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HClinicaViewModel @Inject constructor(private val hClinicaRepository: HClinicaRepository): ViewModel() {

    val hclinicasLiveDataEpisodio get() = hClinicaRepository.hclinicasLiveDataEpisodio
    val hclinicasLiveDataHoy get() = hClinicaRepository.hclinicasLiveDataHoy
    val hclinicasLiveDataUser get() = hClinicaRepository.hclinicasLiveDataUser


    //Respuesta de crear HClinica
    val hclinicaResponseLiveData: LiveData<BaseResponse<HClinicaResponse>>
        get() = hClinicaRepository.hclinicaResponseLiveData

    fun createHClinica(hClinicaRequest: HClinicaRequest){
        viewModelScope.launch {
            hClinicaRepository.createHClinica(hClinicaRequest)
        }
    }

    fun getHClinicasUser(idUser: Int) {
        viewModelScope.launch {
            hClinicaRepository.getHClinicasUser(idUser)
        }
    }

    fun getHClinicasEpisodio(episodio: String) {
        viewModelScope.launch {
            hClinicaRepository.getHClinicasEpisodio(episodio)
        }
    }

    fun getAllHClinicas() {
        viewModelScope.launch {
            hClinicaRepository.getAllHClinicas()
        }
    }


}