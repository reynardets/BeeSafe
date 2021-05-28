package com.example.beesafe.api

import com.example.beesafe.Model.Remote.ReportsResponse
import com.example.beesafe.Model.Reports
import retrofit2.Call
import retrofit2.http.*

interface APIEndpoint {

    @POST("/report")
    fun postReports(@Body reports : Reports) : Call<Reports>

    @GET("/reports")
    fun getReports() : Call<List<ReportsResponse>>

    @GET("/reports/location")
    fun getNearbyReports(
            @Query("latitude") latitude : String,
            @Query("longitude") longitude : String
    ) : Call<List<ReportsResponse>>
}