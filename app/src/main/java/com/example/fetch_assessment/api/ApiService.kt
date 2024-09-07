package com.example.fetch_assessment.api

import com.example.fetch_assessment.models.Item
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
    suspend fun getItems() : List<Item>
}