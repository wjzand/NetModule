package com.android.net

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by wjz on 2018/11/5
 * rxJava线程切换省略符
 */
object NetWorkSchedulers {
    fun <T> composeIoThread(): ObservableTransformer<T,T>{
       return composeThread(Schedulers.io(),AndroidSchedulers.mainThread())
    }

    fun <T> composeNewThread(): ObservableTransformer<T,T>{
        return composeThread(Schedulers.newThread(),AndroidSchedulers.mainThread())
    }

    fun <T> composeThread(observableThread: Scheduler,observerThread:Scheduler): ObservableTransformer<T,T>{
        return ObservableTransformer { observable -> observable.subscribeOn(observableThread)
                .observeOn(observerThread) }
    }
}