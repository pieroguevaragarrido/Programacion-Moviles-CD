package com.guevara.campusgps.util

import java.util.Locale
import kotlin.math.roundToInt

// < 1000 m -> "350 m"; desde 1000 m -> "1.5 km"
fun formatearDistancia(metros: Float): String =
    if (metros < 1000f) {
        "${metros.roundToInt()} m"
    } else {
        String.format(Locale.US, "%.1f km", metros / 1000f)
    }