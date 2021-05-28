package com.example.beesafe.Model.Remote

data class ReportsResponse (
        var category : String = "",
        var datetime : String = "",
        var description : String = "",
        var userId : String = "",
        var latitude : String = "",
        var longitude : String = ""
)