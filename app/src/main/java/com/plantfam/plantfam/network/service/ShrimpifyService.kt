package com.plantfam.plantfam.network.service

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

const val BASE_URL = "https://shrimpify.herokuapp.com"

interface ShrimpifyService {
    @Multipart
    @POST("shrink")
    fun compressImage(@Part("image") image: MultipartBody.Part): Call<ResponseBody>

    companion object {
        var apiService: ShrimpifyService? = null

        private val client = OkHttpClient.Builder().build()

        fun getInstance(): ShrimpifyService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build()
                    .create(ShrimpifyService::class.java)
            }
            return apiService!!
        }
    }
}
