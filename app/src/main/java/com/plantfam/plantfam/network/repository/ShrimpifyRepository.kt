package com.plantfam.plantfam.network.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.network.service.ShrimpifyService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import kotlin.reflect.KFunction3

class ShrimpifyRepository {
    private val shrimpify = ShrimpifyService.getInstance()

    fun compressImage(
        onSuccess: KFunction3<ObjectId, String, InputStream?, Unit>, // This KFunction2 thing is black magic
        plantId: ObjectId,
        imageFileUri: Uri,
        application: Context
    ) {
        val contentResolver = application.contentResolver
        var requestBody: RequestBody? = null
        val imageName = DocumentFile.fromSingleUri(application, imageFileUri)?.name

        // Was getting following error:
        // android.system.ErrnoException: open failed: ENOENT (No such file or directory)
        // when creating a File object with the Uri for the requestBody,
        // so had to use an InputStream instead. Refer to: https://stackoverflow.com/a/66035337/13026376
        contentResolver.openInputStream(imageFileUri)?.use {
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), it.readBytes())
        }

        val multipartBody = MultipartBody.Part.createFormData("image", imageName, requestBody)

        Log.d(TAG(), "Making call to Shrimpify now.")

        shrimpify.compressImage(multipartBody).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ShrimpifyRepository", "Received compressed image.")
                    val compressedImage = response.body()?.byteStream()

                    imageName?.let { onSuccess(plantId, it, compressedImage) }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("ShrimpifyRepository", "Compress image request failed: ", t.cause)
                }
            }
        )
    }
}