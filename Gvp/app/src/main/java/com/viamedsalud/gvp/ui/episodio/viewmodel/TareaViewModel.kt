package com.viamedsalud.gvp.ui.episodio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.viamedsalud.gvp.database.entities.TareaEntity
import com.viamedsalud.gvp.model.Tarea
import com.viamedsalud.gvp.repository.TareaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareaViewModel @Inject constructor(
    private val tareaRepository: TareaRepository
) : ViewModel() {

    val tareaModel = MutableLiveData<Tarea>()

    val allTareas = tareaRepository.getAllTareas().asLiveData()


    fun insertTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            tareaRepository.insertTarea(tarea)
        }
    }

    fun updateTarea(estado: String,id :Int) {
        viewModelScope.launch {
            tareaRepository.updateTarea(estado,id)
        }
    }

    fun deleteTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            tareaRepository.deleteTarea(tarea)
        }
    }



}