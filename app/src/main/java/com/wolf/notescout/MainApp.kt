package com.wolf.notescout

import android.app.Application
import android.content.Context
import com.wolf.notescout.util.SharedPreferencesUtil

class MainApp: Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        application = Application()
        SharedPreferencesUtil.init(applicationContext)
    }
}