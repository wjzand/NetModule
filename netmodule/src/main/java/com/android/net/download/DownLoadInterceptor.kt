package com.android.net.download

import okhttp3.*

/**
 * Created by wjz on 2018/11/7
 *  下载  拦截器
 */
class DownLoadInterceptor(downLoadObserver: DownLoadObserver):Interceptor {
    private var downLoadObserver:DownLoadObserver?= null

    init {
      this.downLoadObserver = downLoadObserver
    }

    override fun intercept(chain: Interceptor.Chain?): Response {
        val response:Response = chain!!.proceed(chain.request())
        return response.newBuilder()
                .body(DownLoadResponseBody(response.body()!!, downLoadObserver!!))
                .build()
    }
}