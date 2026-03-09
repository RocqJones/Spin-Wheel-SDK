package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Relative asset paths for the spin wheel's visual components.
 * Combined with [NetworkAssets.host] via [RemoteConfig.resolveAssets] to produce absolute URLs.
 *
 * @property background Wheel background image.
 * @property frame      Decorative frame around the wheel.
 * @property wheel      Spinning wheel disc image.
 * @property spinButton Spin-trigger button image.
 */
@Serializable
data class WheelAssets(
    @SerialName("background") val background: String = "",
    @SerialName("frame") val frame: String = "",
    @SerialName("wheel") val wheel: String = "",
    @SerialName("spinButton") val spinButton: String = "",
)
