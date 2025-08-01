package com.example.retrofit_openfda.retrofit.repository

import com.example.retrofit_openfda.retrofit.OpenFdaApi
import com.example.retrofit_openfda.room.DrugDao
import com.example.retrofit_openfda.room.DrugEntity

class DrugRepository (
    private val api: OpenFdaApi,
    private val dao: DrugDao
) {
    suspend fun getDrugByBrandName(name: String): DrugEntity {
        val cached = dao.getDrugByBrand(name)

        return if (cached != null) {
            cached
        } else {
            try {
                val response = api.getDrugByBrand("openfda.brand_name:$name")
                val result = response.results.firstOrNull()?.openfda
                val brand = result?.brand_name?.firstOrNull() ?: "Unknown"
                val generic = result?.generic_name?.firstOrNull() ?: "Unknown"

                val drug = DrugEntity(brandName = brand, genericName = generic)
                dao.insertDrug(drug)
                return drug
            } catch (e: Exception) {
                return DrugEntity(brandName = "Not found", genericName = "No internet")
            }
        }
    }

}