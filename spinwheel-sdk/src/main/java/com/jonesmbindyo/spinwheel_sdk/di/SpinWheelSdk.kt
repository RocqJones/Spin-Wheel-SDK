package com.jonesmbindyo.spinwheel_sdk.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.jonesmbindyo.data.cache.AssetCache
import com.jonesmbindyo.data.cache.JsonCache
import com.jonesmbindyo.data.cache.SpinWheelCache
import com.jonesmbindyo.data.prefs.SpinWheelPrefs
import com.jonesmbindyo.di.domainModule
import com.jonesmbindyo.di.networkModule
import com.jonesmbindyo.di.repositoryModule
import com.jonesmbindyo.spinwheel_sdk.utils.SpinWheelAssetUrls
import com.jonesmbindyo.spinwheel_sdk.vm.SpinWheelViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val PREFS_NAME = "spinwheel_prefs"

object SpinWheelSdk {

    fun initialize(context: Context, assetUrls: SpinWheelAssetUrls) {
        startKoin {
            androidContext(context)
            modules(
                networkModule,
                repositoryModule,
                domainModule,
                cacheModule(context),
                sdkModule(assetUrls),
            )
        }
    }
}

private fun cacheModule(context: Context) = module {
    single { SpinWheelCache(filesDir = context.filesDir) }
    single<JsonCache> { get<SpinWheelCache>().json }
    single<AssetCache> { get<SpinWheelCache>().asset }
    single { SpinWheelPrefs(prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)) }
}

private fun sdkModule(assetUrls: SpinWheelAssetUrls) = module {
    single { assetUrls }
    viewModel {
        SpinWheelViewModel(
            configRepository = get(),
            assetRepository = get(),
            spinEngine = get(),
            prefs = get(),
            assetUrls = get(),
        )
    }
}

