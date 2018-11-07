package com.android.net.download

/**
 * Created by wjz on 2018/11/7
 * 下载管理类
 * 单例
 */
class DownLoadManager private constructor(){

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
}