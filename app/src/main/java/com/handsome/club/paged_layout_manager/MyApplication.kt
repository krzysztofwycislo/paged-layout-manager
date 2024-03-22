package com.handsome.club.paged_layout_manager

import android.app.Application
import timber.log.Timber


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}