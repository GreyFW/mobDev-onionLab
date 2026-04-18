package com.example.core

object TimeUtils {
    fun formatSecondsToHMS(totalSeconds: Int): String {
        val h = totalSeconds / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60

        // String.format("%02d:%02d:%02d", h, m, s)
        return h.toString().padStart(2, '0') + ":" +
                m.toString().padStart(2, '0') + ":" +
                s.toString().padStart(2, '0')
    }
}