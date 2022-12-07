package com.viamedsalud.gvp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.viamedsalud.gvp.util.TypefaceUtil
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class GvpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Configuración log
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("LogGvp")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        TypefaceUtil.overrideFont(this, "SERIF", "fonts/google_sans.ttf")
    }
}