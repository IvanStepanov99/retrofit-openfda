package com.example.retrofit_openfda.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DrugDao {
    @Query("SELECT * FROM drugs WHERE LOWER(brandName) = LOWER(:brandName) LIMIT 1")
    suspend fun getDrugByBrand(brandName: String): DrugEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrug(drug: DrugEntity)
}