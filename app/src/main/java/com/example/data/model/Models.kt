package com.example.data.model

data class Bus(
    val id: String,
    val driverName: String,
    val driverPhone: String,
    val routeId: String,
    val routeName: String,
    val status: String, // "On Time", "Delayed", "Arriving"
    val delayMinutes: Int,
    val capacity: Int,
    val currentPassengers: Int,
    val latitudePercent: Float, // 0.0 to 1.0 on canvas
    val longitudePercent: Float, // 0.0 to 1.0 on canvas
    val nextStop: String,
    val etaMinutes: Int
)

data class BusRoute(
    val id: String,
    val name: String,
    val description: String,
    val stops: List<String>,
    val schedule: List<String>,
    val pathPoints: List<Pair<Float, Float>> // coordinates for rendering route on Campus Map Canvas
)
