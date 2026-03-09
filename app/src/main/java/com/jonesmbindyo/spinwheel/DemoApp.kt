package com.jonesmbindyo.spinwheel

import android.app.Application
import com.jonesmbindyo.spinwheel.di.SpinWheelSdk

// Demo host application — initializes the SpinWheel SDK on startup.
class DemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SpinWheelSdk.initialize(this)
    }
}

