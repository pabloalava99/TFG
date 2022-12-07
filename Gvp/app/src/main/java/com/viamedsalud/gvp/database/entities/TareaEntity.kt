package com.viamedsalud.gvp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tareas_table")
data class TareaEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "tarea") val tarea: String,
    @ColumnInfo(name = "estado") val estado: String
)
