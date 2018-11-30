package com.android.net.download

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by wjz on 2018/11/7
 *  下载监听
 */
interface DownLoadObserver{


    fun onProgress(progress:Int)


    fun onComplete()
}