package com.dev.dailyledger.utils

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Delay heavy initialization
        AppPreferences.initialize(this)


    }

}