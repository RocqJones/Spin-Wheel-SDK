package com.jonesmbindyo.domain.wheel

/**
 * Outcome of a single spin.
 *
 * @property resultIndex           Zero-based index of the winning wheel segment.
 * @property targetRotationDegrees Total degrees the wheel must rotate to land on the result.
 * @property durationMs            Animation duration in milliseconds, sourced from [WheelRotationConfig.duration].
 */
data class SpinResult(
    val resultIndex: Int,
    val targetRotationDegrees: Float,
    val durationMs: Int,
)

