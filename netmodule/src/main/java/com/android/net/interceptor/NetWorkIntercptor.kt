package com.android.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by wjz on 2018/10/24
 *  网络拦截器
 */
class NetWorkIntercptor :Interceptor{

    override fun intercept(chain: Interceptor.Chain?): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var request = chain!!.request()
    }
}