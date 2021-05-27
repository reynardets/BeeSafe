package com.example.beesafe.api

import com.example.beesafe.Model.Remote.ReportsResponse
import com.example.beesafe.Model.Reports
import retrofit2.Call
import retrofit2.http.*

interface APIEndpoint {

    @POST("/report")
    fun postReports(@Body reports : Reports) : Call<Reports>

    @GET("/reports")
    fun getReports() : Call<ArrayList<ReportsResponse>>

    @GET("/reports/location?latitude={latitude}&longitude={longitude}")
    fun getNearbyReports(
            @Path("latitude") latitude : String,
            @Path("longitude") longitude : String
    ) : Call<ArrayList<ReportsResponse>>
}