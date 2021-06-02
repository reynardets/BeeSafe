package com.example.beesafe.model.remote

data class APIResponse(
    var message: String = "",
    var data: List<ReportsResponse>
)