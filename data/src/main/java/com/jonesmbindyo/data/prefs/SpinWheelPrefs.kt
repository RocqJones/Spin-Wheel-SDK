package com.jonesmbindyo.data.prefs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.jonesmbindyo.core.constants.SpinWheelPrefsConstants

/**
 * Persistent key-value store for the Spin Wheel SDK backed by Android [SharedPreferences].
 * Accepts a [SharedPreferences] instance so the data module stays testable without Android context.
 *
 * Instantiate via: `SpinWheelPrefs(context.getSharedPreferences("spinwheel_prefs", Context.MODE_PRIVATE))`
 */
class SpinWheelPrefs(private val prefs: SharedPreferences) {

    /**
     * Epoch-millisecond timestamp of the last successful remote config fetch.
     * Required to enforce the config TTL and avoid redundant network requests on every launch.
     * Defaults to `0`, guaranteeing a fetch on first run.
     */
    var lastConfigFetchTimeMs: Long
        get() = prefs.getLong(SpinWheelPrefsConstants.KEY_LAST_CONFIG_FETCH_TIME_MS, 0L)
        @SuppressLint("UseKtx")
        set(value) = prefs.edit().putLong(SpinWheelPrefsConstants.KEY_LAST_CONFIG_FETCH_TIME_MS, value).apply()

    /** Index of the wheel segment from the most recent spin. Defaults to `-1` (no spin yet). */
    var lastSpinResultIndex: Int
        get() = prefs.getInt(SpinWheelPrefsConstants.KEY_LAST_SPIN_RESULT_INDEX, -1)
        @SuppressLint("UseKtx")
        set(value) = prefs.edit().putInt(SpinWheelPrefsConstants.KEY_LAST_SPIN_RESULT_INDEX, value).apply()

    /** Wipes all SDK preferences. */
    @SuppressLint("UseKtx")
    fun clear() = prefs.edit().clear().apply()
}
