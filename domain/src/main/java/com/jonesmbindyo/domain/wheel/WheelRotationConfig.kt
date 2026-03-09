package com.jonesmbindyo.domain.wheel

/**
 * Spin animation parameters sourced from the remote config.
 *
 * @property duration     Animation duration in milliseconds.
 * @property minimumSpins Minimum full rotations before stopping.
 * @property maximumSpins Maximum full rotations before stopping.
 * @property spinEasing   Easing function applied to the animation.
 */
data class WheelRotationConfig(
    val duration: Int = 2000,
    val minimumSpins: Int = 3,
    val maximumSpins: Int = 5,
    val spinEasing: String = "easeInOutCubic",
)
