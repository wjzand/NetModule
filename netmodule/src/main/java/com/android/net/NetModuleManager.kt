package com.android.net

import android.annotation.SuppressLint
import com.android.net.download.DownLoadInterceptor
import com.android.net.upload.UpLoadLoadRequestBody
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream

/**
 * Created by wjz on 2018/11/7
 * 下载管理类
 * 单例
 */
class NetModuleManager private constructor() : NetProgressObserver {
    private val netModuleApiService:NetModuleApiService
    private val downLoadInterceptor: DownLoadInterceptor = DownLoadInterceptor(this)
    private var netDownloadObserver:NetProgressObserver? = null
    private var compositeDisposable = CompositeDisposable()

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
        netModuleApiService = NetRetrofitProvider.Builder
                .addLogInterceptor(HttpLoggingInterceptor.Level.BODY)
                .addInterceptor(downLoadInterceptor)
                .build()
                .createApi(NetModuleApiService::class.java)
    }

    fun startDownLoad(url: String,outFile:File){
        startDownLoad(url,outFile,null)
    }

    fun startDownLoad(url: String,outFile:File,downLoadObserver: NetProgressObserver?){
        netDownloadObserver = downLoadObserver
        val downDisposable = netModuleApiService.downLoadFile(url)
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
        compositeDisposable.add(downDisposable)
    }

    @SuppressLint("CheckResult")
    fun startUpLoad(url: String, filePath:File){

    }

    fun cancelDownLoad(){
        compositeDisposable.dispose()
    }

    override fun onComplete() {
        netDownloadObserver?.onComplete()
    }

    override fun onProgress(progress: Int) {
        netDownloadObserver?.onProgress(progress)
    }


}