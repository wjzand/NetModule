package com.android.net.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.util.*

/**
 * Created by wjz on 2018/11/7
 * 下载body，这里主要是进度添加
 *
 */
class DownLoadResponseBody(responseBody: ResponseBody,downLoadObserver: DownLoadObserver): ResponseBody() {
    private var responseBody:ResponseBody? = null
    private var downLoadObserver:DownLoadObserver? = null
    private var byteRead:Long = 0L
    private var totalRead:Long = 0L


    init {
        this.responseBody = responseBody
        this.downLoadObserver = downLoadObserver
    }

    override fun contentLength(): Long {
        return responseBody!!.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody!!.contentType()
    }

    override fun source(): BufferedSource {
        var bufferedSource:BufferedSource? = Okio.buffer(source(responseBody!!.source()))
        return bufferedSource!!
    }

    private fun source(source: Source):Source{
        var forwardingSource:ForwardingSource = object : ForwardingSource(source){
            override fun read(sink: Buffer?, byteCount: Long): Long {
                byteRead =  super.read(sink, byteCount)
                totalRead += if(byteRead == -1L)0L else byteRead
                downLoadObserver!!.onProgress((totalRead/responseBody!!.contentLength()).toString())
                return byteRead
            }
        }
        return forwardingSource
    }
}