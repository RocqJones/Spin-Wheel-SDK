package com.jonesmbindyo.data.config

/**
 * Resolves relative wheel asset paths into absolute URLs by joining [NetworkAssets.host]
 * with each path using exactly one `/` separator.
 *
 * @return [ResolvedWheelAssets] with fully-qualified URLs for every wheel asset.
 */
fun RemoteConfig.resolveAssets(): ResolvedWheelAssets {
    val host = network.assets.host.trimEnd('/')
    fun String.toAbsoluteUrl() = "$host/${this.trimStart('/')}"

    return ResolvedWheelAssets(
        backgroundUrl = wheel.assets.background.toAbsoluteUrl(),
        frameUrl = wheel.assets.frame.toAbsoluteUrl(),
        wheelUrl = wheel.assets.wheel.toAbsoluteUrl(),
        spinButtonUrl = wheel.assets.spinButton.toAbsoluteUrl(),
    )
}
