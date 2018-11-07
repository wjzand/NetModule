package com.android.module.common

import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by wjz on 2018/10/24
 *
 */
interface ApiService {

    @GET("v1/mirror/update")
    fun testGet():Observable<MagicMirrorResponse<Any>>


    @FormUrlEncoded
    @POST("v1/mirror")
    fun testFormPost(@Field("field1") field1: String,
               @Field("field2") field2: String):Observable<MagicMirrorResponse<Any>>


    @POST("v1/mirror")
    fun testBodyPost(@Body entity: Entity):Observable<MagicMirrorResponse<Any>>

}