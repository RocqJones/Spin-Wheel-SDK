package com.jonesmbindyo.spinwheel

import android.app.Application
import com.jonesmbindyo.spinwheel.constants.ASSET_URL_BACKGROUND
import com.jonesmbindyo.spinwheel.constants.ASSET_URL_FRAME
import com.jonesmbindyo.spinwheel.constants.ASSET_URL_SPIN_BUTTON
import com.jonesmbindyo.spinwheel.constants.ASSET_URL_WHEEL
import com.jonesmbindyo.spinwheel_sdk.di.SpinWheelSdk
import com.jonesmbindyo.spinwheel_sdk.utils.SpinWheelAssetUrls

// Demo host application — initializes the SpinWheel SDK on startup.
class DemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SpinWheelSdk.initialize(
            context    = this,
            assetUrls  = SpinWheelAssetUrls(
                background = ASSET_URL_BACKGROUND,
                frame = ASSET_URL_FRAME,
                spinButton = ASSET_URL_SPIN_BUTTON,
                wheel = ASSET_URL_WHEEL,
            ),
        )
    }
}
