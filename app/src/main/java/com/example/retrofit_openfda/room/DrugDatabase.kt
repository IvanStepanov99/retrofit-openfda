package com.example.retrofit_openfda.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DrugEntity::class], version = 1)
abstract class DrugDatabase : RoomDatabase() {

    abstract fun drugDao(): DrugDao

    companion object {
        @Volatile private var INSTANCE: DrugDatabase? = null

        fun getDatabase(context: Context): DrugDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrugDatabase::class.java,
                    "drug_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
