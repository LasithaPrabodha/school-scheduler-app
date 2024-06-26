package com.capstoneprojectg8.schoolscheduleapp.ui.settings

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.capstoneprojectg8.schoolscheduleapp.R
import com.capstoneprojectg8.schoolscheduleapp.ui.settings.auth.UserProfileActivity
import com.capstoneprojectg8.schoolscheduleapp.ui.settings.classes.ClassSettingsActivity
import com.capstoneprojectg8.schoolscheduleapp.utils.ThemeHelper.isDarkModeEnabled
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : PreferenceFragmentCompat() {

    private val auth = FirebaseAuth.getInstance()

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val classPreference: Preference? = findPreference("classes")

        classPreference?.setOnPreferenceClickListener {
            val intent = Intent(activity, ClassSettingsActivity::class.java)
            startActivity(intent)

            true
        }

        val userPreference: Preference? = findPreference("user")

        userPreference?.setOnPreferenceClickListener {
            val intent = Intent(activity, UserProfileActivity::class.java)
            startActivity(intent)

            true
        }

        listenToAuthUser()
        updateAssetTint()

        val switchDarkModePreference: SwitchPreferenceCompat? = findPreference("switch_dark_mode")

        switchDarkModePreference?.isChecked = isDarkModeEnabled(requireContext())

        switchDarkModePreference?.setOnPreferenceChangeListener { _, isSelected ->
            if (isSelected as Boolean) {
                enableDarkMode()
            } else {
                disableDarkMode()
            }
            requireActivity().recreate()
            updateAssetTint()
            true
        }
    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        saveDarkModePreference(true)
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        saveDarkModePreference(false)
    }

    private fun saveDarkModePreference(isDarkModeEnabled: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        with(sharedPreferences.edit()) {
            putBoolean("dark_mode", isDarkModeEnabled)
            apply()
        }
    }

    private fun updateAssetTint() {
        val isDarkModeEnabled = isDarkModeEnabled(requireContext())
        val tintResId = if (isDarkModeEnabled) R.color.white else R.color.black

        val switchDarkModePreference: SwitchPreferenceCompat? = findPreference("switch_dark_mode")
        val classesDarkModePreference: Preference? = findPreference("classes")
        val userDarkModePreference: Preference? = findPreference("user")

        switchDarkModePreference?.icon?.setTint(ContextCompat.getColor(requireContext(), tintResId))
        classesDarkModePreference?.icon?.setTint(
            ContextCompat.getColor(
                requireContext(),
                tintResId
            )
        )
        userDarkModePreference?.icon?.setTint(ContextCompat.getColor(requireContext(), tintResId))

        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(tintResId)

        (activity as AppCompatActivity).window.statusBarColor =
            resources.getColor(R.color.transparent)
    }


    private fun listenToAuthUser() {
        auth.addAuthStateListener {
            val userPreference: Preference? = findPreference("user")
            if (it.currentUser != null) {
                userPreference?.title = it.currentUser?.email
            } else {
                userPreference?.title = ""
            }
        }
    }

}