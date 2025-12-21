package com.example.aa2_paurafeldiazhernandez

import android.content.Context
import androidx.core.content.ContextCompat

object ThemeManager {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_IS_BLUE_THEME = "is_blue_theme"


    fun setBlueTheme(context: Context, isBlue: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_BLUE_THEME, isBlue).apply()
    }


    fun isBlueTheme(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_BLUE_THEME, false)
    }


    fun getPrimaryColor(context: Context): Int {
        return if (isBlueTheme(context)) {
            ContextCompat.getColor(context, R.color.blue)
        } else {
            ContextCompat.getColor(context, R.color.purple)
        }
    }

    fun getPrimaryDarkColor(context: Context): Int {
        return if (isBlueTheme(context)) {
            ContextCompat.getColor(context, R.color.blue)
        } else {
            ContextCompat.getColor(context, R.color.purple)
        }
    }

    fun getThemeName(context: Context): String {
        return if (isBlueTheme(context)) "Blue" else "Purple"
    }
}