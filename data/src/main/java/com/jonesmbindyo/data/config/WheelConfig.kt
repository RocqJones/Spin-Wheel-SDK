package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Appearance and structure configuration for the spin wheel.
 *
 * @property segments Number of equal wheel segments. Defaults to 8.
 * @property assets   Relative image paths for wheel rendering.
 */
@Serializable
data class WheelConfig(
    @SerialName("segments") val segments: Int = 8,
    @SerialName("assets") val assets: WheelAssets = WheelAssets(),
)
