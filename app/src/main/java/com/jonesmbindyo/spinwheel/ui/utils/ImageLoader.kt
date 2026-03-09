package com.jonesmbindyo.spinwheel.ui.utils

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import java.io.File

/**
 * Loads a cached image [file] into a [Painter] suitable for use in Compose.
 * Returns `null` if [file] is null or cannot be decoded.
 */
@Composable
fun rememberPainterFromFile(file: File?): Painter? {
    return remember(file) {
        file?.let { BitmapFactory.decodeFile(it.absolutePath) }
            ?.let { BitmapPainter(it.asImageBitmap()) }
    }
}

