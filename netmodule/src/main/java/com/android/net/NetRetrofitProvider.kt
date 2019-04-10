package com.android.net

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * Created by wjz on 2018/10/19
 * 构建retrofit
 *
 */
class NetRetrofitProvider(url: String, connectTime: Long, readTime: Long, writeTime: Long, interceptorList: ArrayList<Interceptor>) {
    private var retrofit:Retrofit
    private var okHttpClient:OkHttpClient
    private var okHttpClientBuilder:OkHttpClient.Builder = OkHttpClient.Builder()

    init {

        interceptorList.forEach { okHttpClientBuilder.addInterceptor(it)}

        okHttpClient = okHttpClientBuilder
                .readTimeout(readTime,TimeUnit.SECONDS)
                .writeTimeout(writeTime,TimeUnit.SECONDS)
                .connectTimeout(connectTime,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun <T> createApi(service:Class<T>): T{
         return retrofit.create(service)
    }

    object Builder{
        private var url:String = ""
        private var readTime = 10L
        private var writeTime = 10L
        private var connectTime = 10L
        private var interceptorList:ArrayList<Interceptor> = arrayListOf()
        private val TAG:String = this.javaClass.simpleName
        private val loggingInterceptor = HttpLoggingInterceptor()

        @SuppressLint("LogNotTimber")
        fun addUrl(url:String): Builder {
            if(TextUtils.isEmpty(url)) throw Exception("url 不能为空")
            this.url = url
            if(!this.url.endsWith("/")) this.url += "/"
            Log.e(TAG,"请求域名：{" + this.url + "}")
            return this
        }

        fun addConnectTime(connectTime:Long): Builder {
            this.connectTime = connectTime
            return this
        }

        fun addReadTime(readTime:Long): Builder {
            this.readTime = readTime
            return this
        }

        fun addWriteTime(writeTime:Long): Builder {
            this.writeTime = writeTime
            return this
        }

        @SuppressLint("LogNotTimber")
        fun addLogInterceptor(logLevel:HttpLoggingInterceptor.Level): Builder {
            loggingInterceptor.level = logLevel
            if(interceptorList.contains(loggingInterceptor)){
                interceptorList.remove(loggingInterceptor)
            }
            interceptorList.add(loggingInterceptor)
            Log.e(TAG,"加入logInterceptor")
            return this
        }

        @SuppressLint("LogNotTimber")
        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptorList.add(interceptor)
            Log.e(TAG,"加入interceptor")
            return this
        }

        fun build():NetRetrofitProvider{
            return NetRetrofitProvider(this.url,this.connectTime,this.readTime,this.writeTime, interceptorList)
        }
    }

}

