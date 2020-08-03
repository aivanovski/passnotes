package com.ivanovsky.passnotes

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.ivanovsky.passnotes.injection.SharedModule
import com.ivanovsky.passnotes.injection.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class App @JvmOverloads constructor(
    private val appConfig: AppConfig = DEFAULT_APP_CONFIG
) : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        sharedModule = SharedModule(this)

        if (appConfig.isStethoEnabled) {
            Stetho.initializeWithDefaults(this)
        }

        if (appConfig.isKoinEnabled) {
            startKoin {
                androidLogger()
                androidContext(this@App)
                modules(KoinModule.appModule)
            }
        }
    }

    companion object {

        @JvmStatic
        lateinit var appInstance: App

        @JvmStatic
        lateinit var sharedModule: SharedModule

        private val DEFAULT_APP_CONFIG = AppConfig(
            isKoinEnabled = true,
            isStethoEnabled = true
        )
    }

    data class AppConfig(
        val isKoinEnabled: Boolean,
        val isStethoEnabled: Boolean
    )
}