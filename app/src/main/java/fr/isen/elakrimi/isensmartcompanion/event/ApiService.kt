package fr.isen.elakrimi.isensmartcompanion.event

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("events.json") // 🔗 Endpoint de l'API
    fun getEvents(): Call<List<Event>> // ✅ Correction pour reconnaître Event
}





