package com.example.beesafe.api

import com.example.beesafe.model.Reports
import com.example.beesafe.model.remote.APIResponse
import retrofit2.Call
import retrofit2.http.*

interface APIEndpoint {

    @POST("/report")
    fun postReports(@Body reports: Reports): Call<Reports>

    @GET("/reports")
    fun getReports(): Call<APIResponse>

    @GET("/reports/location")
    fun getNearbyReports(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<APIResponse>

    @GET("/user/{userId}/reports")
    fun getUserHistoryReports(
            @Path("userId") userId: String
    ): Call<APIResponse>
}