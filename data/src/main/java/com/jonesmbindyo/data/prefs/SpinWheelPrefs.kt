package com.jonesmbindyo.data.prefs

import com.jonesmbindyo.core.constants.SpinWheelPrefsConstants
import java.io.File
import java.util.Properties

/**
 * Persistent key-value store for the Spin Wheel SDK.
 * Backed by a `spinwheel_prefs.properties` file inside [filesDir]. Every assignment writes through immediately.
 *
 * @param filesDir The app's private files directory (i.e. `Context.filesDir`).
 */
class SpinWheelPrefs(filesDir: File) {

    private val file = filesDir.resolve("spinwheel_prefs.properties")
    private val props = Properties().also { p ->
        if (file.exists()) file.inputStream().use { p.load(it) }
    }

    /**
     * Epoch-millisecond timestamp of the last successful remote config fetch.
     * Required to enforce the config TTL and avoid redundant network requests on every launch.
     * Defaults to `0`, guaranteeing a fetch on first run.
     */
    var lastConfigFetchTimeMs: Long
        get() = props.getProperty(SpinWheelPrefsConstants.KEY_LAST_CONFIG_FETCH_TIME_MS, "0").toLong()
        set(value) = persist(SpinWheelPrefsConstants.KEY_LAST_CONFIG_FETCH_TIME_MS, value.toString())

    /** Index of the wheel segment from the most recent spin. Defaults to `-1` (no spin yet). */
    var lastSpinResultIndex: Int
        get() = props.getProperty(SpinWheelPrefsConstants.KEY_LAST_SPIN_RESULT_INDEX, "-1").toInt()
        set(value) = persist(SpinWheelPrefsConstants.KEY_LAST_SPIN_RESULT_INDEX, value.toString())

    /** Wipes all SDK preferences. */
    fun clear() {
        props.clear()
        file.delete()
    }

    private fun persist(key: String, value: String) {
        props.setProperty(key, value)
        file.outputStream().use { props.store(it, null) }
    }
}
