package com.android.net

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

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
}