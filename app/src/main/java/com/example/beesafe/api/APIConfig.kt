package com.example.beesafe.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {

    companion object {
        const val BASE_URL = "http://34.101.112.118:8000"

        fun getAPIService(): APIEndpoint {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(APIEndpoint::class.java)
        }
    }
}