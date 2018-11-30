package com.android.net.download

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.android.net.NetModuleApiService
import com.android.net.NetWorkSchedulers
import com.android.net.RetrofitProvider
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Url
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by wjz on 2018/11/7
 * 下载管理类
 * 单例
 */
class DownLoadManager private constructor(){
    private var netModuleApiService:NetModuleApiService? = null
    private var disposable:Disposable? = null
    private var downLoadObserver:DownLoadObserver? = null

    companion object {
        @Volatile
        private var INSTANCE:DownLoadManager? = null

        val getInstance : DownLoadManager
        get() {
            if(INSTANCE == null){
                synchronized(DownLoadManager::class.java){
                    if(INSTANCE == null){
                        INSTANCE = DownLoadManager()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    init {
        netModuleApiService = RetrofitProvider.Builder
                .addLogInterceptor(HttpLoggingInterceptor.Level.BODY)
                .addInterceptor(DownLoadInterceptor(downLoadObserver = downLoadObserver!!))
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

    fun pauseDownLoad(){

    }

    fun resumeDownLoad(){

    }

    fun cancelDownLoad(){
        disposable?.dispose()
    }

}