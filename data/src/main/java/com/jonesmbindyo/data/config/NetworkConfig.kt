package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network-level configuration for the SDK.
 *
 * Controls how the SDK reaches remote resources, including the base URL
 * used for asset resolution and how long a fetched config may be reused
 * before a fresh fetch is required.
 *
 * @property assets           Host information used to build absolute asset URLs.
 * @property configTtlSeconds Number of seconds a cached [RemoteConfig] remains valid
 *                            before the SDK must re-fetch it. Defaults to 3600 (1 hour).
 */
@Serializable
data class NetworkConfig(
    @SerialName("assets")           val assets: NetworkAssets = NetworkAssets(),
    @SerialName("configTtlSeconds") val configTtlSeconds: Int = 3600,
)
