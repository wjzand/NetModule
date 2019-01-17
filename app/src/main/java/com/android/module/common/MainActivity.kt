package com.android.module.common

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.android.net.NetRetrofitProvider
import com.android.net.NetModuleManager
import com.android.net.NetProgressObserver
import com.android.net.interceptor.CommonParamterInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class MainActivity : AppCompatActivity(), NetProgressObserver {
    @SuppressLint("LogNotTimber")
    override fun onProgress(progress: Int) {
        Log.e("MainActivity",progress.toString())
    }

    override fun onComplete() {
        Log.e("MainActivity","onComplete")

    }

    private var retrofitProvider: NetRetrofitProvider?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitProvider = NetRetrofitProvider.Builder
                .addUrl("http://test-magic-console.startdtapi.com")
                .addLogInterceptor(HttpLoggingInterceptor.Level.BODY)
                .addInterceptor(CommonParamterInterceptor.Creater
                        .addHead("mac","D0:F8:8C:B1:02:E8").addHead("versionCode","2")
                        .addParamter("111","111").addParamter("222","222")
                        .create())
                .build()

       /* retrofitProvider!!.createApi(ApiService::class.java)
                .testFormPost("333","444")
                .compose(NetWorkSchedulers.composeIoThread())
                .subscribe(object :Observer<MagicMirrorResponse<Any>>{
                    override fun onComplete() {
                        Log.e("wjz","onComplete:")

                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.e("wjz","onSubscribe:")

                    }

                    override fun onError(e: Throwable) {
                        Log.e("wjz","onError:" + e.toString())

                    }

                    override fun onNext(t: MagicMirrorResponse<Any>) {
                        Log.e("wjz","onNext:" + t.toString())
                    }

                })
*/
        NetModuleManager.getInstance.startDownLoad("http://magic-mirror-diancang.oss-cn-shanghai.aliyuncs.com/magic/201807/horizontal_ad_default.png?ts=1541668232070",
                File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "wjz.png"),this);
    }
}
