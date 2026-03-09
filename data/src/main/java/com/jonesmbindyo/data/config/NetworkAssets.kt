package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Host configuration for remotely hosted assets.
 *
 * @property host Base CDN URL prepended to every relative asset path (e.g. `"https://example.com/assets/"`).
 */
@Serializable
data class NetworkAssets(
    @SerialName("host") val host: String = "",
)
