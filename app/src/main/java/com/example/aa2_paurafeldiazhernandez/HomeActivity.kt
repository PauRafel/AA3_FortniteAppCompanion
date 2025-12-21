package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


 //Actividad principal que contiene la navegación por fragmentos
 //Maneja la barra de navegación inferior y el toolbar superior

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configura el toolbar personalizado como ActionBar de la actividad
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Aplica el tema seleccionado a los componentes de navegación
        applyThemeToBars()

        // Carga el fragmento inicial solo si es la primera creación de la actividad
        if (savedInstanceState == null) {
            loadFragment(NewsActivity())
            supportActionBar?.title = "News"
        }

        // Listener para manejar los clicks en los items de la navegación inferior
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_news -> {
                    supportActionBar?.title = "News"
                    loadFragment(NewsActivity())
                    true
                }
                R.id.navigation_shop -> {
                    supportActionBar?.title = "Shop"
                    loadFragment(ShopActivity())
                    true
                }
                R.id.navigation_profile -> {
                    supportActionBar?.title = "Profile"
                    loadFragment(ProfileActivity())
                    true
                }
                R.id.navigation_settings -> {
                    supportActionBar?.title = "Settings"
                    loadFragment(SettingsActivity())
                    true
                }
                else -> false
            }
        }
    }


     // Carga un fragmento en el contenedor de navegación
     // Reemplaza el fragmento actual por el nuevo

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }


     // Aplica el color del tema actual al toolbar y barra de navegación
     // Obtiene el color desde ThemeManager según las preferencias del usuario

    private fun applyThemeToBars() {
        val primaryColor = ThemeManager.getPrimaryColor(this)

        toolbar.setBackgroundColor(primaryColor)
        bottomNavigationView.setBackgroundColor(primaryColor)
    }
}