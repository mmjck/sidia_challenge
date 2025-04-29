package com.mmjck.movies.utils

import java.text.SimpleDateFormat
import java.util.*


class Functions {
    companion object {
        fun formatRuntime(runtime: Int): String {
            val hours = runtime / 60
            val minutes = runtime % 60
            return "${hours}h ${minutes}m"
        }

        fun getYearFromDate(dateString: String?): String {
            return try {
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val date = dateString?.let {
                    format.parse(it)
                }
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.get(Calendar.YEAR).toString()
            } catch (e: Exception) {
                ""
            }
        }
    }
}