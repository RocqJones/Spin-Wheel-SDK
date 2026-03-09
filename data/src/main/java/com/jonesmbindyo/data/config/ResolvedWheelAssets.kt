package com.jonesmbindyo.data.config

/**
 * Fully-resolved, absolute URLs for every visual asset used by the spin wheel.
 *
 * Instances of this class are produced by [RemoteConfig.resolveAssets] and are
 * ready to be passed directly to an image-loading library (e.g. Coil, Glide)
 * without any further URL manipulation.
 *
 * @property backgroundUrl Absolute URL of the background image.
 * @property frameUrl      Absolute URL of the decorative wheel frame image.
 * @property wheelUrl      Absolute URL of the spinning wheel disc image.
 * @property spinButtonUrl Absolute URL of the spin-trigger button image.
 */
data class ResolvedWheelAssets(
    val backgroundUrl: String,
    val frameUrl: String,
    val wheelUrl: String,
    val spinButtonUrl: String,
)
