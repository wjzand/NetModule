package com.android.net.interceptor

import android.annotation.SuppressLint
import android.util.Log
import okhttp3.*
import okhttp3.FormBody.Builder

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
        commonHeadMap = headMap
        commonParamterMap = paramterMap
    }


    override fun intercept(chain: Interceptor.Chain?): Response {
        val oldRequest = chain!!.request()
        val newRequestBuilder = oldRequest.newBuilder()
        addHead(newRequestBuilder)
        if(commonParamterMap.isNotEmpty()){
            when(oldRequest.method()){
                METHOD_GET -> addGetParamter(oldRequest,newRequestBuilder)
                METHOD_POST -> addPostParamter(oldRequest,newRequestBuilder)
                METHOD_OPTIONS -> addOptionsParamter(oldRequest)
            }
        }
        val newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)

    }

    private fun addGetParamter(request: Request,requestBuilder: Request.Builder){
        val urlBuilder:HttpUrl.Builder = request.url().newBuilder()
        commonParamterMap.forEach {urlBuilder.addQueryParameter(it.key,it.value)}
        requestBuilder.url(urlBuilder.build())
    }

    private fun addPostParamter(request: Request,requestBuilder: Request.Builder){
        val formBody:FormBody = (request.body() as FormBody?)!!
        val builder: FormBody.Builder = FormBody.Builder()
        for (i in 0..(formBody.size()-1)){
            builder.add(formBody.name(i),formBody.value(i))
        }
        commonParamterMap.forEach { builder.add(it.key,it.value) }
        requestBuilder.post(builder.build())
    }


    private fun addOptionsParamter(request: Request){

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