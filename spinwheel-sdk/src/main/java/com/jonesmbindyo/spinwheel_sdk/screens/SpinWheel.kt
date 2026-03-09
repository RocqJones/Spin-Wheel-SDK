package com.jonesmbindyo.spinwheel_sdk.screens

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
import com.jonesmbindyo.spinwheel_sdk.utils.rememberPainterFromFile
import com.jonesmbindyo.spinwheel_sdk.vm.SpinWheelViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Public SDK composable. Initialize [com.jonesmbindyo.spinwheel_sdk.di.SpinWheelSdk] before using this.
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

    LaunchedEffect(configUrl) { viewModel.load(configUrl) }

    LaunchedEffect(state.pendingResult) {
        if (state.pendingResult) {
            onSpinEnd(state.lastResultIndex)
            viewModel.clearPendingResult()
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

