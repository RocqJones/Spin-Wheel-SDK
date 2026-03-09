package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Relative asset paths for the visual components of the spin wheel.
 *
 * Every path in this model is a relative filename (or sub-path) that is
 * combined with [NetworkAssets.host] at runtime to produce an absolute URL.
 * Use [RemoteConfig.resolveAssets] to obtain fully-qualified [ResolvedWheelAssets].
 *
 * @property background Relative path of the background image rendered behind the wheel.
 * @property frame      Relative path of the decorative frame placed around the wheel.
 * @property wheel      Relative path of the spinning wheel disc image.
 * @property spinButton Relative path of the button image that triggers a spin.
 */
@Serializable
data class WheelAssets(
    @SerialName("background") val background: String = "",
    @SerialName("frame")      val frame: String      = "",
    @SerialName("wheel")      val wheel: String      = "",
    @SerialName("spinButton") val spinButton: String = "",
)
