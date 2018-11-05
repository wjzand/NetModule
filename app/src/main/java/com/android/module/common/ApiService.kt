package com.android.module.common

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by wjz on 2018/10/24
 *
 */
interface ApiService {

    @GET("v1/mirror/update")
    fun ceshi():Observable<MagicMirrorResponse<Any>>

}