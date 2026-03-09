package com.jonesmbindyo.spinwheel.di

import com.jonesmbindyo.data.assets.AssetRepository
import com.jonesmbindyo.data.config.ConfigRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { ConfigRepository(httpSource = get(), jsonCache = get(), prefs = get()) }
    single { AssetRepository(httpSource = get(), assetCache = get()) }
}

