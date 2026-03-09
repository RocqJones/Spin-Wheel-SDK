package com.jonesmbindyo.spinwheel_sdk.vm

import java.io.File

data class SpinWheelUiState(
    val isLoadingConfig: Boolean = true,
    val isLoadingAssets: Boolean = true,
    val isSpinning: Boolean = false,
    val rotationDegrees: Float = 0f,
    val backgroundFile: File? = null,
    val wheelFile: File? = null,
    val frameFile: File? = null,
    val spinButtonFile: File? = null,
    val errorMessage: String? = null,
    val lastResultIndex: Int = -1,
    val pendingResult: Boolean = false,
)