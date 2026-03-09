package com.jonesmbindyo.spinwheel.di

import com.jonesmbindyo.data.cache.AssetCache
import com.jonesmbindyo.data.cache.JsonCache
import com.jonesmbindyo.data.cache.SpinWheelCache
import com.jonesmbindyo.data.prefs.SpinWheelPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val cacheModule = module {
    single { SpinWheelCache(filesDir = androidContext().filesDir) }
    single<JsonCache>  { get<SpinWheelCache>().json }
    single<AssetCache> { get<SpinWheelCache>().asset }
    single { SpinWheelPrefs(filesDir = androidContext().filesDir) }
}