package com.jonesmbindyo.data.config

/**
 * Resolves all relative wheel asset paths into absolute URLs.
 *
 * The host from [RemoteConfig.network] is joined with each relative path from
 * [RemoteConfig.wheel] using exactly one `/` as separator, regardless of whether
 * the host has a trailing slash or the path has a leading one.
 *
 * Example:
 * ```
 * val config = RemoteConfig(
 *     network = NetworkConfig(assets = NetworkAssets(host = "https://example.com/assets/")),
 *     wheel   = WheelConfig(assets = WheelAssets(
 *         background = "bg.png",
 *         frame      = "wheel_frame.png",
 *         wheel      = "wheel.png",
 *         spinButton = "wheel_spin.png",
 *     )),
 * )
 * val resolved = config.resolveAssets()
 * // resolved.backgroundUrl  == "https://example.com/assets/bg.png"
 * // resolved.frameUrl       == "https://example.com/assets/wheel_frame.png"
 * // resolved.wheelUrl       == "https://example.com/assets/wheel.png"
 * // resolved.spinButtonUrl  == "https://example.com/assets/wheel_spin.png"
 * ```
 *
 * @return [ResolvedWheelAssets] with fully-qualified URLs for every wheel asset.
 */
fun RemoteConfig.resolveAssets(): ResolvedWheelAssets {
    val host = network.assets.host.trimEnd('/')
    fun String.toAbsoluteUrl() = "$host/${this.trimStart('/')}"

    return ResolvedWheelAssets(
        backgroundUrl = wheel.assets.background.toAbsoluteUrl(),
        frameUrl      = wheel.assets.frame.toAbsoluteUrl(),
        wheelUrl      = wheel.assets.wheel.toAbsoluteUrl(),
        spinButtonUrl = wheel.assets.spinButton.toAbsoluteUrl(),
    )
}
