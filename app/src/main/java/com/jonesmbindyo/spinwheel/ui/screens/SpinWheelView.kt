package com.jonesmbindyo.spinwheel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

/**
 * Renders the spin wheel using four layered image assets.
 *
 * Layer order (bottom → top):
 *   1. Background  – fills the entire container
 *   2. Wheel       – centered, rotates by [rotation] degrees
 *   3. Frame       – centered overlay drawn above the wheel
 *   4. Spin button – centered, triggers [onSpinClick], disabled while [isSpinning]
 *
 * Animation is intentionally absent here; [rotation] is driven by the ViewModel.
 *
 * @param background  Painter for the background image.
 * @param wheel       Painter for the spinning wheel disc.
 * @param frame       Painter for the decorative frame overlay.
 * @param spinButton  Painter for the spin-trigger button.
 * @param rotation    Current rotation angle in degrees applied to the wheel.
 * @param isSpinning  When `true` the spin button is non-interactive.
 * @param onSpinClick Called when the user taps the spin button.
 */
@Composable
fun SpinWheelView(
    background: Painter,
    wheel: Painter,
    frame: Painter,
    spinButton: Painter,
    rotation: Float,
    isSpinning: Boolean,
    onSpinClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        // Layer 1 – background
        Image(
            painter = background,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        // Layer 2 – rotating wheel disc
        Image(
            painter = wheel,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation),
        )

        // Layer 3 – static frame drawn above the wheel
        Image(
            painter = frame,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )

        // Layer 4 – spin button, disabled while a spin is in progress
        Image(
            painter = spinButton,
            contentDescription = "Spin",
            modifier = Modifier
                .fillMaxSize(0.25f)
                .clickable(
                    enabled = !isSpinning,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onSpinClick,
                ),
        )
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 360, heightDp = 360)
@Composable
private fun SpinWheelViewPreview() {
    SpinWheelView(
        background  = ColorPainter(Color(0xFF1A1A2E)),
        wheel       = ColorPainter(Color(0xFFE94560)),
        frame       = ColorPainter(Color(0x8816213E)),
        spinButton  = ColorPainter(Color(0xFFFFD700)),
        rotation    = 0f,
        isSpinning  = false,
        onSpinClick = {},
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 360, name = "Spinning state")
@Composable
private fun SpinWheelViewSpinningPreview() {
    SpinWheelView(
        background  = ColorPainter(Color(0xFF1A1A2E)),
        wheel       = ColorPainter(Color(0xFFE94560)),
        frame       = ColorPainter(Color(0x8816213E)),
        spinButton  = ColorPainter(Color(0x88FFD700)),
        rotation    = 135f,
        isSpinning  = true,
        onSpinClick = {},
    )
}

