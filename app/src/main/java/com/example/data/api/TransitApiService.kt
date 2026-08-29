package com.example.data.api

import com.example.data.model.Bus
import com.example.data.model.BusRoute
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransitApiService {

    /**
     * Fetches the live locations and status of all active buses.
     */
    @GET("api/v1/buses/live")
    suspend fun getLiveBuses(): List<Bus>

    /**
     * Fetches all available campus bus routes.
     */
    @GET("api/v1/routes")
    suspend fun getCampusRoutes(): List<BusRoute>

    /**
     * Example endpoint: Fetches details for a specific bus by ID.
     */
    @GET("api/v1/buses/{id}")
    suspend fun getBusDetails(@Path("id") busId: String): Bus

    /**
     * Example endpoint: Searches for buses on a specific route.
     */
    @GET("api/v1/buses/search")
    suspend fun searchBusesByRoute(@Query("routeId") routeId: String): List<Bus>
}
