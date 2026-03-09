package com.jonesmbindyo.spinwheel.di

import com.jonesmbindyo.spinwheel.ui.vm.SpinWheelViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SpinWheelViewModel(
            configRepository = get(),
            assetRepository = get(),
            spinEngine = get(),
            prefs = get(),
        )
    }
}

