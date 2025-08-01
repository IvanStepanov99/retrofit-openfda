package com.example.retrofit_openfda.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApi {
    @GET("drug/label.json")
    suspend fun getDrugByBrand(
        @Query("search") search: String
    ): OpenFdaResponse
}