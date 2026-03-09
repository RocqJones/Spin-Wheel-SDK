package com.jonesmbindyo.spinwheel.vm

import com.jonesmbindyo.data.assets.AssetRepository
import com.jonesmbindyo.data.config.ConfigRepository
import com.jonesmbindyo.data.prefs.SpinWheelPrefs
import com.jonesmbindyo.domain.wheel.SpinEngine
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SpinWheelViewModel(
            configRepository = get<ConfigRepository>(),
            assetRepository = get<AssetRepository>(),
            spinEngine = get<SpinEngine>(),
            prefs = get<SpinWheelPrefs>(),
        )
    }
}