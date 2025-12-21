package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import kotlin.system.exitProcess

  // Fragmento de settings
  // Permite al usuario cambiar entre tema azul y morado

class SettingsActivity : Fragment() {

    private lateinit var switchTheme: SwitchCompat
    private lateinit var txtCurrentTheme: TextView
    private lateinit var btnCloseApp: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchTheme = view.findViewById(R.id.switch_theme)
        txtCurrentTheme = view.findViewById(R.id.txt_current_theme)
        btnCloseApp = view.findViewById((R.id.btn_close_app))
        // Carga el tema guardado en SharedPreferences
        val isBlueTheme = ThemeManager.isBlueTheme(requireContext())
        switchTheme.isChecked = isBlueTheme
        updateThemeText(isBlueTheme)

        // Listener que se ejecuta cuando el usuario cambia el switch
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            // Guarda la preferencia del tema en SharedPreferences
            ThemeManager.setBlueTheme(requireContext(), isChecked)
            updateThemeText(isChecked)
            // recreate() reinicia la actividad para aplicar el nuevo tema
            // Recarga todos los colores y vistas con el tema actualizado
            requireActivity().recreate()
        }

        btnCloseApp.setOnClickListener {
            closeApplication()
        }

        applyThemeToButton()
    }

     // Actualiza el texto y color que indica el tema actual
    private fun updateThemeText(isBlue: Boolean) {
        if (isBlue) {
            txtCurrentTheme.text = "Blue Theme"
            txtCurrentTheme.setTextColor(requireContext().getColor(R.color.blue))
        } else {
            txtCurrentTheme.text = "Purple Theme"
            txtCurrentTheme.setTextColor(requireContext().getColor(R.color.purple))
        }
    }

    // Funcion para cerrar la app
    private fun closeApplication() {
        requireActivity().finishAffinity()
        exitProcess(0)
    }
    private fun applyThemeToButton() {
        val primaryColor = ThemeManager.getPrimaryColor(requireContext())
        btnCloseApp.setBackgroundColor(primaryColor)
    }
}