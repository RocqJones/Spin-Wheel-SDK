package com.jonesmbindyo.core.config

/**
 * Animation parameters for the spin wheel rotation.
 *
 * @property duration     Total spin animation duration in milliseconds.
 * @property minimumSpins Minimum number of full rotations before stopping.
 * @property maximumSpins Maximum number of full rotations before stopping.
 * @property spinEasing   Easing function name applied to the spin animation.
 */
data class RotationConfig(
    val duration: Int = 2000,
    val minimumSpins: Int = 3,
    val maximumSpins: Int = 5,
    val spinEasing: String = "easeInOutCubic",
)

