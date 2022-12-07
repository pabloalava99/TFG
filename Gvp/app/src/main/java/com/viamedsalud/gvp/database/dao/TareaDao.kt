package com.viamedsalud.gvp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.viamedsalud.gvp.database.entities.TareaEntity
import com.viamedsalud.gvp.model.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao{

    @Query("SELECT * FROM tareas_table")
    fun getAllTareas(): Flow<List<TareaEntity>>

    @Insert
    suspend fun insertTarea(tarea: TareaEntity)

    @Query("UPDATE tareas_table SET estado=:estado WHERE id = :id")
    suspend fun updateTarea(estado: String,id :Int)

    @Delete
    suspend fun deleteTarea(tarea: TareaEntity)

}