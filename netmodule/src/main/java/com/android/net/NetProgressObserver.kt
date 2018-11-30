package com.android.net

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by wjz on 2018/11/7
 *  进度监听
 */
interface NetProgressObserver{

    fun onProgress(progress:Int)

    fun onComplete()
}