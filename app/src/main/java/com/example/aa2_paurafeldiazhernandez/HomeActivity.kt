package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        applyThemeToBars()

        if (savedInstanceState == null) {
            loadFragment(NewsActivity())
            supportActionBar?.title = "News"
        }

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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun applyThemeToBars() {
        val primaryColor = ThemeManager.getPrimaryColor(this)

        toolbar.setBackgroundColor(primaryColor)
        bottomNavigationView.setBackgroundColor(primaryColor)

    }
}