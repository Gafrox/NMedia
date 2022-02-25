package ru.netology.nmedia.util

object Util {
    fun counter(count: Int): String {
        return when {
            (count >= 1_000_000) -> if (count % 1_000_000 > 99_999) "${"%.1f".format(count / 1_000_000.toDouble())}M" else "${count / 1_000_000}M"
            (count in 1000..9_999) -> if (count % 1_000 > 99) "${"%.1f".format(count / 1_000.toDouble())}K" else "${count / 1_000}K"
            (count in 10_000..999_999) -> "${count / 1_000}K"
            else -> count
        }.toString()
    }
}