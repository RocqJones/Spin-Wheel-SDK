package com.jonesmbindyo.core.dto

import com.jonesmbindyo.core.config.RemoteConfig
import com.jonesmbindyo.core.config.RotationConfig
import com.jonesmbindyo.core.config.WheelAssets

/**
 * Maps a [WidgetResponse] into the internal [RemoteConfig] domain model.
 * Uses the first element of [WidgetResponse.data]; returns defaults if the list is empty.
 */
fun WidgetResponse.toRemoteConfig(): RemoteConfig {
    val widget = data.firstOrNull() ?: return RemoteConfig()

    val attrs = widget.network.attributes
    val host = widget.network.assets.host.trimEnd('/')

    return RemoteConfig(
        refreshInterval = attrs.refreshInterval,
        cacheExpiration = attrs.cacheExpiration,
        rotationConfig = widget.wheel.rotation.toRotationConfig(),
        wheelAssets = widget.wheel.assets.toWheelAssets(host),
    )
}

private fun WheelRotationDto.toRotationConfig() = RotationConfig(
    duration = duration,
    minimumSpins = minimumSpins,
    maximumSpins = maximumSpins,
    spinEasing = spinEasing,
)

private fun WheelAssetsDto.toWheelAssets(host: String) = WheelAssets(
    background = host.joinPath(bg),
    frame = host.joinPath(wheelFrame),
    wheel = host.joinPath(wheel),
    spinButton = host.joinPath(wheelSpin),
)

/** Joins a host and a relative path with exactly one `/` separator. */
private fun String.joinPath(path: String): String {
    if (path.isBlank()) return this
    return "$this/${path.trimStart('/')}"
}

