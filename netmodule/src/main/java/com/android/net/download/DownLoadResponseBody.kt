package com.android.net.download

import com.android.net.NetProgressObserver
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * Created by wjz on 2018/11/7
 * 下载body，这里主要是进度添加
 *
 */
class DownLoadResponseBody(responseBody: ResponseBody,downLoadObserver: NetProgressObserver?): ResponseBody() {
    private var responseBody:ResponseBody? = null
    private var downLoadObserver: NetProgressObserver? = null
    private var totalRead:Long = 0L
    private var bufferedSource:BufferedSource? = null


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
        bufferedSource = bufferedSource?:Okio.buffer(source(responseBody!!.source()))
        return bufferedSource!!
    }

    private fun source(source: Source):Source{
        return object : ForwardingSource(source){
            override fun read(sink: Buffer?, byteCount: Long): Long {
                val byteRead =  super.read(sink, byteCount)
                if(byteRead != -1L){
                    totalRead += byteRead
                    downLoadObserver?.onProgress(((totalRead * 100/ responseBody!!.contentLength()).toInt()))
                    if(totalRead == contentLength()) downLoadObserver?.onComplete()
                }
                return byteRead
            }
        }
    }
}