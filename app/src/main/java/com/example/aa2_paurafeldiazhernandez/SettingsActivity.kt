package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

class SettingsActivity : Fragment() {

    private lateinit var switchTheme: SwitchCompat
    private lateinit var txtCurrentTheme: TextView

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

        val isBlueTheme = ThemeManager.isBlueTheme(requireContext())
        switchTheme.isChecked = isBlueTheme
        updateThemeText(isBlueTheme)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setBlueTheme(requireContext(), isChecked)
            updateThemeText(isChecked)
            requireActivity().recreate()
        }
    }

    private fun updateThemeText(isBlue: Boolean) {
        if (isBlue) {
            txtCurrentTheme.text = "Blue Theme"
            txtCurrentTheme.setTextColor(requireContext().getColor(R.color.blue))
        } else {
            txtCurrentTheme.text = "Purple Theme"
            txtCurrentTheme.setTextColor(requireContext().getColor(R.color.purple))
        }
    }
}