package com.jonesmbindyo.di

import com.jonesmbindyo.domain.wheel.SpinEngine
import org.koin.dsl.module

val domainModule = module {
    single { SpinEngine() }
}

