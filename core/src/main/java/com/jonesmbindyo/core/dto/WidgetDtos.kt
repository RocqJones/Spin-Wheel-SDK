package com.jonesmbindyo.core.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Root response envelope from the widget API. */
@Serializable
data class WidgetResponse(
    @SerialName("data") val data: List<WidgetData> = emptyList(),
)

/** A single widget entry inside the [WidgetResponse.data] array. */
@Serializable
data class WidgetData(
    @SerialName("id")      val id: String = "",
    @SerialName("name")    val name: String = "",
    @SerialName("type")    val type: String = "",
    @SerialName("network") val network: NetworkDto = NetworkDto(),
    @SerialName("wheel")   val wheel: WheelConfigDto = WheelConfigDto(),
)

/** Network block containing behaviour attributes and asset host. */
@Serializable
data class NetworkDto(
    @SerialName("attributes") val attributes: NetworkAttributes = NetworkAttributes(),
    @SerialName("assets")     val assets: NetworkAssetsDto = NetworkAssetsDto(),
)

/** Operational network parameters. */
@Serializable
data class NetworkAttributes(
    @SerialName("refreshInterval")  val refreshInterval: Int = 300,
    @SerialName("networkTimeout")   val networkTimeout: Int = 30000,
    @SerialName("retryAttempts")    val retryAttempts: Int = 3,
    @SerialName("cacheExpiration")  val cacheExpiration: Int = 3600,
    @SerialName("debugMode")        val debugMode: Boolean = false,
)

/** Base CDN/Drive host for resolving relative asset paths. */
@Serializable
data class NetworkAssetsDto(
    @SerialName("host") val host: String = "",
)

/** Wheel configuration block containing rotation and asset sub-objects. */
@Serializable
data class WheelConfigDto(
    @SerialName("rotation") val rotation: WheelRotationDto = WheelRotationDto(),
    @SerialName("assets")   val assets: WheelAssetsDto = WheelAssetsDto(),
)

/** Spin animation parameters. */
@Serializable
data class WheelRotationDto(
    @SerialName("duration")     val duration: Int = 2000,
    @SerialName("minimumSpins") val minimumSpins: Int = 3,
    @SerialName("maximumSpins") val maximumSpins: Int = 5,
    @SerialName("spinEasing")   val spinEasing: String = "easeInOutCubic",
)

/** Relative asset filenames resolved against [NetworkAssetsDto.host]. */
@Serializable
data class WheelAssetsDto(
    @SerialName("bg")         val bg: String = "",
    @SerialName("wheelFrame") val wheelFrame: String = "",
    @SerialName("wheelSpin")  val wheelSpin: String = "",
    @SerialName("wheel")      val wheel: String = "",
)

