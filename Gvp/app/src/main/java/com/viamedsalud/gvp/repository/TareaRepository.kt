package com.viamedsalud.gvp.repository


import com.viamedsalud.gvp.database.dao.TareaDao
import com.viamedsalud.gvp.database.entities.TareaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TareaRepository @Inject constructor(
    private val tareaDao: TareaDao
) {

    fun getAllTareas() = tareaDao.getAllTareas()

    suspend fun insertTarea(tarea: TareaEntity){
        tareaDao.insertTarea(tarea)
    }

    suspend fun updateTarea(estado: String,id :Int){
        tareaDao.updateTarea(estado,id)
    }

    suspend fun deleteTarea(tarea: TareaEntity){
        tareaDao.deleteTarea(tarea)
    }


}