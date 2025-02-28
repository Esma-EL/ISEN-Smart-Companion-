package fr.isen.elakrimi.isensmartcompanion.event

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/") // Base de l'URL
            .addConverterFactory(GsonConverterFactory.create()) // Convertisseur JSON -> Kotlin
            .build()
            .create(ApiService::class.java)
    }
}