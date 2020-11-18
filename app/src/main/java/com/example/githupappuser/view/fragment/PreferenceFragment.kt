package com.example.githupappuser.view.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.githupappuser.R
import com.example.githupappuser.receiver.AlarmReceiver

class PreferenceFragment: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String
    private lateinit var CHANGE: String

    private lateinit var reminderPreference: SwitchPreference
    private lateinit var changePreference: Preference

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()

        init()
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        if (key == REMINDER) {
            reminderPreference.summary = sp?.getBoolean(REMINDER, false).toString()

            val isChecked = reminderPreference.summary

            if (isChecked == "true") {
                reminderPreference.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_notifications_active_24, null)
                val title = getString(R.string.daily_reminder)
                val message = getString(R.string.message_alarm)
                alarmReceiver.setRepeatingAlarm(requireContext(), title, message)
            } else {
                reminderPreference.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_notifications_24, null)
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }

    private fun init() {
        REMINDER = resources.getString(R.string.id_reminder_preference)
        CHANGE = resources.getString(R.string.id_changelang_preference)

        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        changePreference = findPreference<Preference>(CHANGE) as Preference
        changePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        reminderPreference.summary = sh.getBoolean(REMINDER, false).toString()
        val isChecked = reminderPreference.summary
        if (isChecked == "true") {
            reminderPreference.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_notifications_active_24, null)
        } else {
            reminderPreference.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_notifications_24, null)
        }
    }
}