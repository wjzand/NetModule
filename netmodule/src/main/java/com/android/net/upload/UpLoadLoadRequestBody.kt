package com.android.net.upload

import com.android.net.NetProgressObserver
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

/**
 * Created by wjz on 2018/11/7
 * 下载body，这里主要是进度添加
 *
 */
class UpLoadLoadRequestBody(requestBody: RequestBody, progressObserver: NetProgressObserver): RequestBody() {
    private var requestBody:RequestBody? = null
    private var progressObserver: NetProgressObserver? = null
    private var bufferedSink:BufferedSink? = null
    private var totalWrite:Long = 0L

    init {
        this.requestBody = requestBody
        this.progressObserver = progressObserver
        totalWrite = 0L
    }

    override fun contentLength(): Long {
        return requestBody!!.contentLength()
    }

    override fun contentType(): MediaType? {
        return requestBody!!.contentType()
    }


    override fun writeTo(sink: BufferedSink) {
        bufferedSink = bufferedSink?:Okio.buffer(dealSink(sink))
        requestBody?.writeTo(bufferedSink!!)
        bufferedSink?.flush()
    }


    private fun dealSink(sink: BufferedSink):Sink{
        return object :ForwardingSink(sink){
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                totalWrite += byteCount
                progressObserver?.onProgress(((totalWrite * 100/contentLength()).toInt()))
                if(totalWrite == contentLength()){
                    progressObserver?.onComplete()
                }
            }
        }
    }

}