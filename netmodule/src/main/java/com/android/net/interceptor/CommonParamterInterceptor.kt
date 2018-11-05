package com.android.net.interceptor

import android.annotation.SuppressLint
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by wjz on 2018/10/24
 * 公共参数拦截器
 */
class CommonParamterInterceptor(headMap: HashMap<String, String>, paramterMap: HashMap<String, String>) :Interceptor{
    private var commonHeadMap:HashMap<String,String> = HashMap()
    private var commonParamterMap:HashMap<String,String> = HashMap()
    private val METHOD_POST:String = "POST"
    private val METHOD_GET:String = "GET"
    private val METHOD_OPTIONS:String = "OPTIONS"


    init {
        commonHeadMap = headMap;
        commonParamterMap = paramterMap
    }


    override fun intercept(chain: Interceptor.Chain?): Response {
        var oldRequest = chain!!.request()
        when(oldRequest.method()){
            METHOD_GET -> println()
            METHOD_POST -> println()
            METHOD_OPTIONS -> println()
        }
        var newRequestBuilder = oldRequest.newBuilder()
        addHead(newRequestBuilder)
        var newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)

    }

    private fun addHead(requestBuilder: Request.Builder){
        if(!commonHeadMap.isEmpty()) commonHeadMap.forEach{requestBuilder.addHeader(it.key,it.value)}
    }

    object Creater {
        private var headMap:HashMap<String,String> = HashMap()
        private var commonParamterMap:HashMap<String,String> = HashMap()
        private val TAG:String = this.javaClass.simpleName


        fun addHead(key:String,value:String):Creater{
            headMap[key] = value
            return this
        }

        fun addParamter(key:String,value:String):Creater{
            commonParamterMap[key] = value
            return this
        }

        @SuppressLint("LogNotTimber")
        fun create() : CommonParamterInterceptor{
            Log.e(TAG,"head参数："+ headMap.toString())
            Log.e(TAG,"公共参数：" + commonParamterMap.toString())
            return CommonParamterInterceptor(headMap, commonParamterMap)
        }
    }
}