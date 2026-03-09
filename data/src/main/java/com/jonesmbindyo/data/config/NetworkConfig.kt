package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network-level configuration for the SDK.
 *
 * @property assets           Host information used to build absolute asset URLs.
 * @property configTtlSeconds Seconds a cached [RemoteConfig] remains valid before re-fetching. Defaults to 3600.
 */
@Serializable
data class NetworkConfig(
    @SerialName("assets") val assets: NetworkAssets = NetworkAssets(),
    @SerialName("configTtlSeconds") val configTtlSeconds: Int = 3600,
)
