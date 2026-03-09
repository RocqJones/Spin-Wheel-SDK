package com.jonesmbindyo.domain.wheel

import kotlin.random.Random

/**
 * Pure spin calculation engine. Stateless and deterministic when a seeded [Random] is supplied.
 *
 * @param random Random source used for segment and turn selection. Override in tests for determinism.
 */
class SpinEngine(private val random: Random = Random.Default) {

    /**
     * Computes a [SpinResult] for a wheel with [segments] equal sections.
     *
     * @param segments      Number of equal wheel segments. Must be > 0.
     * @param rotationConfig Animation and spin-count parameters from the remote config.
     * @throws IllegalArgumentException if [segments] ≤ 0 or minimumSpins > maximumSpins.
     */
    fun spin(segments: Int, rotationConfig: WheelRotationConfig): SpinResult {
        require(segments > 0) {
            "segments must be > 0, was $segments"
        }
        require(rotationConfig.minimumSpins <= rotationConfig.maximumSpins) {
            "minimumSpins (${rotationConfig.minimumSpins}) must be <= maximumSpins (${rotationConfig.maximumSpins})"
        }

        val segmentAngle = 360f / segments

        // Pick which segment the wheel lands on
        val resultIndex = random.nextInt(0, segments)

        // Pick how many full rotations to add for visual effect
        val turns = random.nextInt(rotationConfig.minimumSpins, rotationConfig.maximumSpins + 1)

        // Rotate past full turns, then to the centre of the result segment
        val targetRotationDegrees = (turns * 360f) +
                (resultIndex * segmentAngle) +
                (segmentAngle / 2f)

        return SpinResult(
            resultIndex           = resultIndex,
            targetRotationDegrees = targetRotationDegrees,
            durationMs            = rotationConfig.duration,
        )
    }
}

