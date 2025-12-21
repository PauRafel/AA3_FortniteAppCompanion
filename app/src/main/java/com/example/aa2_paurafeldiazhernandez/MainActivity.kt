package com.example.aa2_paurafeldiazhernandez

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


  // Splash Screen - Primera actividad que se muestra al abrir la app
  // Muestra una pantalla de bienvenida durante 3 segundos antes de ir al login

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private val SPLASH_DURATION = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayout = findViewById(R.id.main)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_DURATION)

        applyThemeToSplash()
    }

    /**
     * Aplica el color del tema seleccionado al fondo del splash screen
     */
    private fun applyThemeToSplash() {
        val primaryColor = ThemeManager.getPrimaryColor(this)
        linearLayout.setBackgroundColor(primaryColor)
    }
}