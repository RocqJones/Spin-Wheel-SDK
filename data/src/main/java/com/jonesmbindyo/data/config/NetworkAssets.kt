package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Host configuration for remotely hosted assets.
 *
 * Provides the base URL that is prepended to every relative asset path
 * defined in [WheelAssets]. The host value may end with or without a
 * trailing slash — [RemoteConfig.resolveAssets] normalises the separator.
 *
 * @property host Base URL of the CDN or file server that hosts SDK assets
 *               (e.g. `"https://example.com/assets/"`).
 */
@Serializable
data class NetworkAssets(
    @SerialName("host") val host: String = "",
)
