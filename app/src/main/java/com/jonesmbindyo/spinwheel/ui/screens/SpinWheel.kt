package com.jonesmbindyo.spinwheel.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jonesmbindyo.spinwheel.vm.SpinWheelViewModel
import com.jonesmbindyo.spinwheel.ui.utils.rememberPainterFromFile
import org.koin.androidx.compose.koinViewModel

/**
 * Public SDK entry point for the Spin Wheel.
 * Handles config loading, asset fetching, and spin animation internally.
 *
 * @param configUrl Remote URL to fetch the widget configuration from.
 * @param modifier  Modifier applied to the wheel container.
 * @param onSpinEnd Called with the winning segment index when a spin completes.
 */
@Composable
fun SpinWheel(
    configUrl: String,
    modifier: Modifier = Modifier,
    onSpinEnd: (resultIndex: Int) -> Unit = {},
) {
    val viewModel: SpinWheelViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    // Load config and assets on first composition
    LaunchedEffect(configUrl) {
        viewModel.load(configUrl)
    }

    // Notify caller when a spin animation completes
    LaunchedEffect(state.isSpinning, state.lastResultIndex) {
        if (!state.isSpinning && state.lastResultIndex >= 0) {
            onSpinEnd(state.lastResultIndex)
        }
    }

    when {
        state.isLoadingConfig || state.isLoadingAssets -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage!!)
            }
        }

        else -> {
            val background = rememberPainterFromFile(state.backgroundFile)
            val wheel = rememberPainterFromFile(state.wheelFile)
            val frame = rememberPainterFromFile(state.frameFile)
            val spinButton = rememberPainterFromFile(state.spinButtonFile)

            if (background != null && wheel != null && frame != null && spinButton != null) {
                SpinWheelView(
                    background = background,
                    wheel = wheel,
                    frame = frame,
                    spinButton = spinButton,
                    rotation = state.rotationDegrees,
                    isSpinning = state.isSpinning,
                    onSpinClick = viewModel::spin,
                    modifier = modifier,
                )
            }
        }
    }
}

