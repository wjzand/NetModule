package com.android.net

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by wjz on 2018/11/7
 *
 */
interface NetModuleApiService {

    @Streaming
    @GET
    fun downLoadFile(@Url url: String, @Header("RANGE") start:String):Observable<ResponseBody>


    @Streaming
    @GET
    fun downLoadFile(@Url url: String):Observable<ResponseBody>


    @POST
    fun uploadFile(@Url url: String,@Part part: MultipartBody.Part):Observable<ResponseBody>
}