package com.jonesmbindyo.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Root remote configuration fetched from the backend.
 *
 * Top-level object deserialised from the remote JSON payload. It groups all
 * sub-configurations (network, wheel, etc.) into a single immutable value that
 * can be cached and propagated throughout the SDK.
 *
 * @property network Configuration that governs how the SDK communicates with the network,
 *                   including asset hosting and cache TTL.
 * @property wheel   Configuration that defines the appearance and behaviour of the spin wheel.
 */
@Serializable
data class RemoteConfig(
    @SerialName("network") val network: NetworkConfig = NetworkConfig(),
    @SerialName("wheel")   val wheel: WheelConfig     = WheelConfig(),
)
