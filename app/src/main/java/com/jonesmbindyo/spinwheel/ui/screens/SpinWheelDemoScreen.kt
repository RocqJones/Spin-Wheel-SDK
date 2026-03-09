package com.jonesmbindyo.spinwheel.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jonesmbindyo.spinwheel.constants.CONFIG_URL
import com.jonesmbindyo.spinwheel_sdk.screens.SpinWheel

private const val TAG = "SpinWheelDemo"

@Composable
fun SpinWheelDemoScreen(modifier: Modifier = Modifier) {
    var lastResultIndex by remember { mutableIntStateOf(-1) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SpinWheel(
            configUrl = CONFIG_URL,
            modifier  = Modifier.fillMaxWidth(),
            onSpinEnd = { resultIndex ->
                Log.d(TAG, "Spin result: $resultIndex")
                lastResultIndex = resultIndex
            },
        )

        Text(
            text     = if (lastResultIndex >= 0) "Last result: $lastResultIndex" else "Tap the wheel to spin",
            style    = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}

