package com.robobender.pcremote.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.robobender.pcremote.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    var sharedPreferences:SharedPreferences? = null

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.settings_pref)
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = preferenceManager.sharedPreferences
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

        val editTextPreference: EditTextPreference? = findPreference(getString(R.string.pref_server))
        editTextPreference?.let {
            updateSummary(it)
        }
    }

    override fun onPause() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        key: String
    ) {
        val editTextPreference: EditTextPreference? = findPreference(getString(R.string.pref_server))
        editTextPreference?.let {
            updateSummary(it)
        }
    }

    private fun updateSummary(preference: EditTextPreference) {
        preference.summary = preference.text
    }
}
