package com.jonesmbindyo.spinwheel

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonesmbindyo.spinwheel.ui.screens.SpinWheelView
import com.jonesmbindyo.spinwheel.ui.theme.SpinWheelTheme
import com.jonesmbindyo.spinwheel.vm.SpinWheelViewModel

private const val CONFIG_URL = "https://your-config-endpoint.com/widget.json"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpinWheelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val vm: SpinWheelViewModel = viewModel()
                    val state by vm.uiState.collectAsState()

                    // Kick off config + asset loading once
                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        vm.load(CONFIG_URL)
                    }

                    when {
                        state.isLoadingConfig || state.isLoadingAssets -> {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                                contentAlignment = Alignment.Center,
                            ) { CircularProgressIndicator() }
                        }

                        state.errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                                contentAlignment = Alignment.Center,
                            ) { Text(state.errorMessage!!) }
                        }

                        else -> {
                            fun filePainter(path: String?) = path
                                ?.let { BitmapFactory.decodeFile(it) }
                                ?.let { BitmapPainter(it.asImageBitmap()) }

                            val bg    = filePainter(state.backgroundFile?.path)
                            val wheel = filePainter(state.wheelFile?.path)
                            val frame = filePainter(state.frameFile?.path)
                            val btn   = filePainter(state.spinButtonFile?.path)

                            if (bg != null && wheel != null && frame != null && btn != null) {
                                SpinWheelView(
                                    background  = bg,
                                    wheel       = wheel,
                                    frame       = frame,
                                    spinButton  = btn,
                                    rotation    = state.rotationDegrees,
                                    isSpinning  = state.isSpinning,
                                    onSpinClick = vm::spin,
                                    modifier    = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}