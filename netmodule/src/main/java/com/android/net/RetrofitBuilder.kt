package com.android.net

import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by wjz on 2018/10/19
 * 构建retrofit
 *
 */
final class RetrofitBuilder {
    var retrofit:Retrofit?= null
    var okHttpClient:OkHttpClient?= null

    constructor(url: String,readTime: Long,writeTime: Long){

        okHttpClient = OkHttpClient.Builder()
                .readTimeout(readTime,TimeUnit.SECONDS)
                .writeTimeout(writeTime,TimeUnit.SECONDS)
                .build()


        retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .build()

    }

    object Builder{
        var url:String = ""
        var readTime = 10L
        var writeTime = 10L

        fun changeUrl(url:String): Builder {
            this.url = url
            return this
        }

        fun changeReadTime(readTime:Long): Builder {
            this.readTime = readTime
            return this
        }


        fun changeWriteTime(writeTime:Long): Builder {
            this.writeTime = writeTime
            return this
        }

        fun build():RetrofitBuilder{
            return RetrofitBuilder(this.url,this.readTime,this.writeTime)
        }
    }
}