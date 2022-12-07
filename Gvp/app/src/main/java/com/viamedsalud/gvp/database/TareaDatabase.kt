package com.viamedsalud.gvp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viamedsalud.gvp.database.dao.TareaDao
import com.viamedsalud.gvp.database.entities.TareaEntity

@Database(entities = [TareaEntity::class], version = 1)
abstract class TareaDatabase: RoomDatabase() {

    abstract fun getTareaDao():TareaDao
}