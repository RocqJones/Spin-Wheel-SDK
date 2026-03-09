package com.jonesmbindyo.spinwheel

import android.app.Application
import com.jonesmbindyo.spinwheel.di.SpinWheelKoin

class SpinWheelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SpinWheelKoin.start(this)
    }
}
