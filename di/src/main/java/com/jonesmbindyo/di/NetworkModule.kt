package com.jonesmbindyo.di

import com.jonesmbindyo.core.network.AssetHttpDataSource
import com.jonesmbindyo.core.network.ConfigHttpDataSource
import com.jonesmbindyo.core.network.HttpClientProvider
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientProvider.client }
    single { ConfigHttpDataSource(client = get()) }
    single { AssetHttpDataSource(client = get()) }
}

