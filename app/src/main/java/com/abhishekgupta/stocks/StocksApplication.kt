package com.abhishekgupta.stocks

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.abhishekgupta.stocks.di.appModule
import com.abhishekgupta.stocks.di.dbModule
import com.abhishekgupta.stocks.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class StocksApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@StocksApplication)
            modules(
                listOf(
                    networkModule,
                    dbModule,
                    appModule
                )
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }
}