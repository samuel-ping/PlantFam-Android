package com.plantfam.plantfam.network.repository

import android.util.Log
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.network.service.ShrimpifyService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShrimpifyRepository {
    val shrimpify = ShrimpifyService.getInstance()

    fun compressImage(imageFile: File) {
        val requestBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            RequestBody.create(MediaType.parse("image/*"), imageFile)
        )

        shrimpify.compressImage(requestBody).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val compressedImage = response.body() // TODO: figure out way to make api call to compress image
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG(), "Compress image request failed: ", t.cause)
                }
            }
        )
    }
}