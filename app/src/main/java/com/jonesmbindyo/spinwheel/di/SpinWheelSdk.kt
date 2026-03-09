package com.jonesmbindyo.spinwheel.di

import android.content.Context

object SpinWheelSdk {
    fun initialize(context: Context) {
        SpinWheelKoin.start(context)
    }
}