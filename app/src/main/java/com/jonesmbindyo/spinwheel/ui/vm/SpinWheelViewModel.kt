package com.jonesmbindyo.spinwheel.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonesmbindyo.core.config.RotationConfig
import com.jonesmbindyo.data.assets.AssetRepository
import com.jonesmbindyo.data.config.ConfigRepository
import com.jonesmbindyo.data.prefs.SpinWheelPrefs
import com.jonesmbindyo.domain.wheel.SpinEngine
import com.jonesmbindyo.domain.wheel.WheelRotationConfig
import com.jonesmbindyo.spinwheel.ui.state.SpinWheelUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.sin

class SpinWheelViewModel(
    private val configRepository: ConfigRepository,
    private val assetRepository: AssetRepository,
    private val spinEngine: SpinEngine,
    private val prefs: SpinWheelPrefs,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpinWheelUiState())
    val uiState: StateFlow<SpinWheelUiState> = _uiState.asStateFlow()

    /** Fetches config, prefetches assets, and restores last spin result. */
    fun load(configUrl: String) {
        viewModelScope.launch {
            // Fetch remote config
            val config = runCatching { configRepository.getConfig(configUrl) }
                .onFailure { _uiState.update { it.copy(errorMessage = "Failed to load config: ${it.errorMessage}") } }
                .getOrNull() ?: run {
                _uiState.update { it.copy(isLoadingConfig = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoadingConfig = false) }

            // Prefetch and cache all four assets
            val assets = config.wheelAssets
            runCatching { assetRepository.prefetchAssets(assets) }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = "Failed to load assets: ${e.message}") } }

            _uiState.update { state ->
                state.copy(
                    isLoadingAssets = false,
                    backgroundFile = runCatching { assetRepository.getAssetFile(assets.background) }.getOrNull(),
                    wheelFile = runCatching { assetRepository.getAssetFile(assets.wheel) }.getOrNull(),
                    frameFile = runCatching { assetRepository.getAssetFile(assets.frame) }.getOrNull(),
                    spinButtonFile = runCatching { assetRepository.getAssetFile(assets.spinButton) }.getOrNull(),
                    lastResultIndex = prefs.lastSpinResultIndex,
                )
            }
        }
    }

    /** Computes a spin result and drives the rotation animation via coroutine delay loop. */
    fun spin() {
        if (_uiState.value.isSpinning) return

        viewModelScope.launch {
            val config = runCatching { configRepository.getConfig("") }.getOrNull()
            val rotationConfig = config?.rotationConfig?.toDomainConfig() ?: WheelRotationConfig()
            val segments = DEFAULT_SEGMENTS

            val result = runCatching { spinEngine.spin(segments, rotationConfig) }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .getOrNull() ?: return@launch

            _uiState.update { it.copy(isSpinning = true) }

            val startDegrees = _uiState.value.rotationDegrees
            val totalDelta = result.targetRotationDegrees
            val durationMs = result.durationMs.toLong()
            val startTime = System.currentTimeMillis()

            // Drive rotation with easeInOutSine approximation via coroutine loop
            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                val progress = (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)
                val eased = easeInOutSine(progress)

                _uiState.update { it.copy(rotationDegrees = startDegrees + totalDelta * eased) }

                if (progress >= 1f) break
                delay(FRAME_DELAY_MS)
            }

            prefs.lastSpinResultIndex = result.resultIndex

            _uiState.update {
                it.copy(
                    isSpinning = false,
                    lastResultIndex = result.resultIndex,
                )
            }
        }
    }

    /** Smooth easing: slow start, fast middle, slow end. */
    private fun easeInOutSine(t: Float): Float =
        (-(sin(Math.PI * t).toFloat() - 1) / 2).coerceIn(0f, 1f)

    companion object {
        private const val DEFAULT_SEGMENTS = 8
        private const val FRAME_DELAY_MS = 16L // ~60 fps
    }
}

/** Maps core RotationConfig to the domain [WheelRotationConfig]. */
private fun RotationConfig.toDomainConfig() = WheelRotationConfig(
    duration = duration,
    minimumSpins = minimumSpins,
    maximumSpins = maximumSpins,
    spinEasing = spinEasing,
)

