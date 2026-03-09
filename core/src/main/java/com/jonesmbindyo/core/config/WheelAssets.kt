package com.jonesmbindyo.core.config

/**
 * Resolved absolute URLs for each spin wheel image asset.
 * Produced by the mapper after joining the host with relative paths from the JSON.
 *
 * @property background  Background image URL.
 * @property frame       Wheel frame image URL.
 * @property wheel       Spinning wheel disc image URL.
 * @property spinButton  Spin-trigger button image URL.
 */
data class WheelAssets(
    val background: String = "",
    val frame: String = "",
    val wheel: String = "",
    val spinButton: String = "",
)
