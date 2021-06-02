package com.example.beesafe.model.remote

data class ReportsResponse(
    var datetime: String = "",
    var category: String = "",
    var userId: String = "",
    var location: LocationResponse,
    var description: String = ""
)