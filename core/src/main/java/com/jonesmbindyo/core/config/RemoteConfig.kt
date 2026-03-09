package com.jonesmbindyo.core.config

/**
 * Internal domain model for the resolved remote configuration.
 *
 * @property refreshInterval How often (seconds) the SDK should re-fetch config.
 * @property cacheExpiration How long (seconds) the cached config remains valid.
 * @property rotationConfig  Animation parameters for the spin wheel.
 * @property wheelAssets     Absolute asset URLs for each wheel image.
 */
data class RemoteConfig(
    val refreshInterval: Int = 300,
    val cacheExpiration: Int = 3600,
    val rotationConfig: RotationConfig = RotationConfig(),
    val wheelAssets: WheelAssets = WheelAssets(),
)
