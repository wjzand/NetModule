package com.android.net

import com.android.net.download.DownLoadInterceptor
import com.android.net.upload.UpLoadLoadRequestBody
import io.reactivex.disposables.Disposable
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream

/**
 * Created by wjz on 2018/11/7
 * 下载管理类
 * 单例
 */
class NetModuleManager private constructor() : NetProgressObserver {
    private var netModuleApiService:NetModuleApiService? = null
    private var disposable:Disposable? = null
    private var downLoadInterceptor: DownLoadInterceptor? = null

    companion object {
        @Volatile
        private var INSTANCE: NetModuleManager? = null

        val getInstance : NetModuleManager
        get() {
            if(INSTANCE == null){
                synchronized(NetModuleManager::class.java){
                    if(INSTANCE == null){
                        INSTANCE = NetModuleManager()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    init {
        downLoadInterceptor = DownLoadInterceptor(this)
        netModuleApiService = NetRetrofitProvider.Builder
                .addLogInterceptor(HttpLoggingInterceptor.Level.BODY)
                .addInterceptor(downLoadInterceptor!!)
                .build()
                .createApi(NetModuleApiService::class.java)
    }

    fun startDownLoad(url: String,outFile:File){
        disposable = netModuleApiService!!.downLoadFile(url)
                .compose(NetWorkSchedulers.composeIoThread())
                .map { t -> t.byteStream() }
                .subscribe { t ->
                    val fileOutputStream = FileOutputStream(outFile)
                    val byte = ByteArray(1024)
                    var length = 0
                    while (t?.read(byte).apply { length = this!! } != -1) {
                        fileOutputStream.write(byte,0,length)
                    }
                    fileOutputStream.flush()
                    t?.close()
                    fileOutputStream.close()
                }
    }

    fun startUpLoad(url: String,filePath:File){

    }


    fun cancelDownLoad(){
        disposable?.dispose()
    }

    override fun onComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProgress(progress: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}