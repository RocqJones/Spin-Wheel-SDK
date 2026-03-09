package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Root remote configuration fetched from the backend.
 *
 * @property network Network communication settings, including asset host and cache TTL.
 * @property wheel   Appearance and asset configuration for the spin wheel.
 */
@Serializable
data class RemoteConfig(
    @SerialName("network") val network: NetworkConfig = NetworkConfig(),
    @SerialName("wheel") val wheel: WheelConfig = WheelConfig(),
)
