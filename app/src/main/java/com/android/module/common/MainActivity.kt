package com.android.module.common

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.net.RetrofitProvider
import okhttp3.logging.HttpLoggingInterceptor

class MainActivity : AppCompatActivity() {
    private var retrofitProvider: RetrofitProvider?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitProvider = RetrofitProvider.Builder
                .addUrl("111")
                .addLogInterceptor(HttpLoggingInterceptor.Level.BODY)
                .build()

        retrofitProvider!!.createApi(ApiService::class.java).ceshi()

    }
}
