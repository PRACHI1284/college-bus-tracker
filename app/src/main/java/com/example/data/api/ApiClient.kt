package com.example.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // Using 10.0.2.2 which is the Android emulator's loopback to the host machine's localhost
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val moshi = Moshi.Builder()
        // Adds reflection to automatically parse Kotlin data classes
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Logs HTTP request and response data to Logcat (useful for debugging)
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * Use this service instance to make network calls in your Repositories.
     * Example: ApiClient.transitService.getLiveBuses()
     */
    val transitService: TransitApiService by lazy {
        retrofit.create(TransitApiService::class.java)
    }
}
