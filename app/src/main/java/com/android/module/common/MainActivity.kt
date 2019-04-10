package com.android.module.common

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import com.android.net.NetRetrofitProvider
import com.android.net.NetModuleManager
import com.android.net.NetProgressObserver
import com.android.net.interceptor.CommonParamterInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {

    /*private val url = "http://magic-mirror-diancang.oss-cn-shanghai.aliyuncs.com/magic/201807/" +
            "horizontal_ad_default.png?ts=154166823207"*/

    private val url = "https://magicmirror.oss-cn-hangzhou.aliyuncs.com/magicMirror1.7.2_20190408_ce2355e_release.apk"

    private val videoUrl = "https://magicmirror.oss-cn-hangzhou.aliyuncs.com/magicMirror1.7.2_20190408_ce2355e_debug.apk"

    private val tag = javaClass.simpleName

    private var retrofitProvider: NetRetrofitProvider?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downLoad.setOnClickListener(this)
        downLoadPic.setOnClickListener(this)
        pause.setOnClickListener(this)
        pausePic.setOnClickListener(this)

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
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.downLoad -> NetModuleManager.startDownLoad(videoUrl,
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "123.apk"),
                    object :NetProgressObserver{
                        override fun onProgress(progress: Int) {
                            progressText.text = progress.toString()                        }

                        override fun onComplete() {
                            progressText.text = "onComplete"
                        }

                    })
            R.id.pause -> NetModuleManager.cancelCurrentDownLoadByUrl(videoUrl)

            R.id.downLoadPic -> NetModuleManager.startDownLoad(url,
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "456.apk"),
                    object :NetProgressObserver{
                        override fun onProgress(progress: Int) {
                            progressTextPic.text = progress.toString()                        }

                        override fun onComplete() {
                            progressTextPic.text = "onComplete"
                        }

                    })
            R.id.pausePic -> NetModuleManager.cancelCurrentDownLoadByUrl(url)
        }

    }
}
