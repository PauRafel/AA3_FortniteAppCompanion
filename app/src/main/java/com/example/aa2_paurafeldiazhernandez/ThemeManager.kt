package com.example.aa2_paurafeldiazhernandez

import android.content.Context
import androidx.core.content.ContextCompat


 // Object Singleton que gestiona el tema de la aplicación

object ThemeManager {

    // Constantes para acceder a SharedPreferences
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_IS_BLUE_THEME = "is_blue_theme"


     // Guarda la preferencia de tema en SharedPreferences
     // isBlue true para tema azul, false para tema morado

    fun setBlueTheme(context: Context, isBlue: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_BLUE_THEME, isBlue).apply()
    }


     // Recupera la preferencia de tema guardada
    fun isBlueTheme(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_BLUE_THEME, false)
    }


    // Obtiene el color primario según el tema seleccionado
    // Este color se usa en toolbar, botones y fondos

    fun getPrimaryColor(context: Context): Int {
        return if (isBlueTheme(context)) {
            ContextCompat.getColor(context, R.color.blue)
        } else {
            ContextCompat.getColor(context, R.color.purple)
        }
    }
}