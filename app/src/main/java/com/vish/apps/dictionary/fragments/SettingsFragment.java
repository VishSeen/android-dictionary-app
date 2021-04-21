package com.vish.apps.dictionary.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.vish.apps.dictionary.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }
}