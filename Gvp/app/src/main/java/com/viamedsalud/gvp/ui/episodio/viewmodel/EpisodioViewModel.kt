package com.viamedsalud.gvp.ui.episodio.viewmodel

import androidx.lifecycle.*
import com.viamedsalud.gvp.utils.BaseResponse
import com.viamedsalud.gvp.api.response.EpisodioResponse
import com.viamedsalud.gvp.repository.EpisodioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodioViewModel@Inject constructor(private val episodioRepository: EpisodioRepository) : ViewModel() {

    val episodiosLiveData get() = episodioRepository.episodiosLiveData

    val episodioResponseLiveData: LiveData<BaseResponse<EpisodioResponse>>
        get() = episodioRepository.episodioResponseLiveData

    fun getEpisodios() {
        viewModelScope.launch {
            episodioRepository.getEpisodios()
        }
    }

    fun getEpisodio(episodio: String) {
        viewModelScope.launch {
            episodioRepository.getEpisodio(episodio)
        }
    }
}