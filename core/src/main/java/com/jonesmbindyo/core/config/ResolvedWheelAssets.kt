package com.jonesmbindyo.core.config

/**
 * Absolute URLs for all spin wheel assets, produced by [RemoteConfig.resolveAssets].
 *
 * @property backgroundUrl Absolute URL of the background image.
 * @property frameUrl      Absolute URL of the wheel frame image.
 * @property wheelUrl      Absolute URL of the wheel disc image.
 * @property spinButtonUrl Absolute URL of the spin button image.
 */
data class ResolvedWheelAssets(
    val backgroundUrl: String,
    val frameUrl: String,
    val wheelUrl: String,
    val spinButtonUrl: String,
)
