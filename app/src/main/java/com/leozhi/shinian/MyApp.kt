package com.leozhi.shinian

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.leozhi.shinian.di.appModule
import org.koin.core.context.startKoin

/**
 * @author leozhi
 * @date 21-2-27
 */
class MyApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TAG = "ShiNian"
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        startKoin {
            modules(appModule)
        }
    }
}