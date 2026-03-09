package com.jonesmbindyo.spinwheel_sdk.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
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
        Image(
            painter = background,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = wheel,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation)
        )
        Image(painter = frame, contentDescription = null, modifier = Modifier.fillMaxSize())
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

@Preview(showBackground = true, widthDp = 360, heightDp = 360)
@Composable
private fun SpinWheelViewPreview() {
    SpinWheelView(
        background = ColorPainter(Color(0xFF1A1A2E)),
        wheel = ColorPainter(Color(0xFFE94560)),
        frame = ColorPainter(Color(0x8816213E)),
        spinButton = ColorPainter(Color(0xFFFFD700)),
        rotation = 0f,
        isSpinning = false,
        onSpinClick = {},
    )
}

