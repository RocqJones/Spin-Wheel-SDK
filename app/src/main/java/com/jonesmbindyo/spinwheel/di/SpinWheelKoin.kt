package com.jonesmbindyo.spinwheel.di

import android.content.Context
import com.jonesmbindyo.di.domainModule
import com.jonesmbindyo.di.networkModule
import com.jonesmbindyo.di.repositoryModule
import com.jonesmbindyo.spinwheel.vm.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object SpinWheelKoin {
    fun start(context: Context) {
        startKoin {
            androidContext(context)
            modules(
                networkModule,
                cacheModule,
                repositoryModule,
                domainModule,
                viewModelModule,
            )
        }
    }
}