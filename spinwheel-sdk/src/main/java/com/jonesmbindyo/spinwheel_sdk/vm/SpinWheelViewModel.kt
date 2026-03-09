package com.jonesmbindyo.spinwheel_sdk.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonesmbindyo.core.config.RotationConfig
import com.jonesmbindyo.data.assets.AssetRepository
import com.jonesmbindyo.data.config.ConfigRepository
import com.jonesmbindyo.data.prefs.SpinWheelPrefs
import com.jonesmbindyo.domain.wheel.SpinEngine
import com.jonesmbindyo.domain.wheel.WheelRotationConfig
import com.jonesmbindyo.spinwheel_sdk.utils.SpinWheelAssetUrls
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
    private val assetUrls: SpinWheelAssetUrls,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpinWheelUiState())
    val uiState: StateFlow<SpinWheelUiState> = _uiState.asStateFlow()

    private var loadedConfigUrl: String = ""

    fun load(configUrl: String) {
        loadedConfigUrl = configUrl
        viewModelScope.launch {
            val config = runCatching { configRepository.getConfig(configUrl) }
                .onFailure { _uiState.update { it.copy(errorMessage = "Failed to load config: ${it.errorMessage}") } }
                .getOrNull() ?: run {
                _uiState.update { it.copy(isLoadingConfig = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoadingConfig = false) }

            val resolvedAssets = config.wheelAssets.copy(
                background = assetUrls.background,
                frame = assetUrls.frame,
                spinButton = assetUrls.spinButton,
                wheel = assetUrls.wheel,
            )

            runCatching { assetRepository.prefetchAssets(resolvedAssets) }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = "Failed to load assets: ${e.message}") } }

            _uiState.update { state ->
                state.copy(
                    isLoadingAssets = false,
                    backgroundFile = runCatching { assetRepository.getAssetFile(resolvedAssets.background) }.getOrNull(),
                    wheelFile = runCatching { assetRepository.getAssetFile(resolvedAssets.wheel) }.getOrNull(),
                    frameFile = runCatching { assetRepository.getAssetFile(resolvedAssets.frame) }.getOrNull(),
                    spinButtonFile = runCatching { assetRepository.getAssetFile(resolvedAssets.spinButton) }.getOrNull(),
                    lastResultIndex = prefs.lastSpinResultIndex,
                )
            }
        }
    }

    fun spin() {
        if (_uiState.value.isSpinning) return

        viewModelScope.launch {
            val config = runCatching { configRepository.getConfig(loadedConfigUrl) }.getOrNull()
            val rotationConfig = config?.rotationConfig?.toDomainConfig() ?: WheelRotationConfig()

            val result = runCatching { spinEngine.spin(DEFAULT_SEGMENTS, rotationConfig) }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .getOrNull() ?: return@launch

            _uiState.update { it.copy(isSpinning = true) }

            val startDegrees = _uiState.value.rotationDegrees
            val startTime = System.currentTimeMillis()

            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                val progress = (elapsed.toFloat() / result.durationMs).coerceIn(0f, 1f)
                _uiState.update {
                    it.copy(
                        rotationDegrees = startDegrees + result.targetRotationDegrees * easeInOutSine(
                            progress
                        )
                    )
                }
                if (progress >= 1f) break
                delay(FRAME_DELAY_MS)
            }

            prefs.lastSpinResultIndex = result.resultIndex
            _uiState.update {
                it.copy(
                    isSpinning = false,
                    lastResultIndex = result.resultIndex,
                    pendingResult = true
                )
            }
        }
    }

    fun clearPendingResult() {
        _uiState.update { it.copy(pendingResult = false) }
    }

    private fun easeInOutSine(t: Float): Float =
        (-(sin(Math.PI * t).toFloat() - 1) / 2).coerceIn(0f, 1f)

    companion object {
        private const val DEFAULT_SEGMENTS = 8
        private const val FRAME_DELAY_MS = 16L
    }
}

private fun RotationConfig.toDomainConfig() = WheelRotationConfig(
    duration = duration,
    minimumSpins = minimumSpins,
    maximumSpins = maximumSpins,
    spinEasing = spinEasing,
)

