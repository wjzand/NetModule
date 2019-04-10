package com.android.net

import android.annotation.SuppressLint
import android.util.Log
import com.android.net.download.DownLoadInterceptor
import com.android.net.upload.UpLoadLoadRequestBody
import io.reactivex.FlowableEmitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream

/**
 * Created by wjz on 2018/11/7
 * 下载管理类
 */
object NetModuleManager {
    private val downDisposableMap = mutableMapOf<String,Disposable>()

    @SuppressLint("LogNotTimber")
    private fun createApiService(downLoadObserver: NetProgressObserver?):NetModuleApiService{
        return NetRetrofitProvider
                .Builder
                .addLogInterceptor(HttpLoggingInterceptor.Level.BODY)
                .addInterceptor(DownLoadInterceptor(object :NetProgressObserver{
                    override fun onProgress(progress: Int) {
                        downLoadObserver?.let {
                            Observable.just(it).observeOn(AndroidSchedulers.mainThread())
                                .doOnNext { it.onProgress(progress) }.subscribe() }

                    }

                    override fun onComplete() {
                        downLoadObserver?.let {
                            Observable.just(it).observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext { it.onComplete()}.subscribe() }
                    }
                }))
                .build()
                .createApi(NetModuleApiService::class.java)
    }

    fun startDownLoad(url: String,outFile:File):Disposable{
        return startDownLoad(url,outFile,null)
    }

    fun startDownLoad(url: String,outFile:File,downLoadObserver: NetProgressObserver?):Disposable{
        val downDisposable = createApiService(downLoadObserver)
                .downLoadFile(url)
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
        downDisposableMap[url] = downDisposable
        return downDisposable
    }

    @SuppressLint("CheckResult")
    fun startUpLoad(url: String, filePath:File){

    }

    fun cancelCurrentDownLoadByUrl(url: String){
        downDisposableMap[url]?.dispose()
    }

    fun cancleAllDownLoad(){
        downDisposableMap.keys.forEach { downDisposableMap[it]?.dispose() }
    }
}