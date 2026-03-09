package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration that describes the spin wheel's appearance and structure.
 *
 * Contains both the structural properties of the wheel (e.g. number of segments)
 * and references to the image assets that make up its visual presentation.
 *
 * @property segments Number of equal segments the wheel is divided into. Defaults to 8.
 * @property assets   Relative paths to the image files used to render the wheel.
 */
@Serializable
data class WheelConfig(
    @SerialName("segments") val segments: Int         = 8,
    @SerialName("assets")   val assets: WheelAssets   = WheelAssets(),
)
