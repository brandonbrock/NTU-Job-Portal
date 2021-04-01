package com.example.ntujobportal.Screens;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.example.ntujobportal.R;

public class Preference extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Settings();
    }

    private void Settings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_dark = sharedPreferences.getBoolean("DARK", false);
        if (check_dark) {
            //if check box is checked go dark mode
            getListView().setBackgroundColor(Color.parseColor("#222222"));
        } else {
            //else set the color to default
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }

        CheckBoxPreference check_dark_instance = (CheckBoxPreference) findPreference("DARK");
        check_dark_instance.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
                boolean yes = (boolean) newValue;
                if (yes) {
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                } else {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return true;
            }
        });
        ListPreference listPreference = (ListPreference) findPreference("ORIENTATION");
        String ORIENTATION = sharedPreferences.getString("ORIENTATION", "false");
        //if 1 default, 2 is portrait, 3 is landscape mode
        if ("1".equals(ORIENTATION)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
            listPreference.setSummary(listPreference.getEntry());
        } else if ("2".equals(ORIENTATION)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            listPreference.setSummary(listPreference.getEntry());
        } else if ("3".equals(ORIENTATION)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            listPreference.setSummary(listPreference.getEntry());
        }
        listPreference.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
                String items = (String) newValue;
                if (preference.getKey().equals("ORIENTATION")) {
                    switch (items) {
                        case "1":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
                            break;
                        case "2":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "3":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }
                    ListPreference listPreference1 = (ListPreference) preference;
                    listPreference1.setSummary(listPreference1.getEntries()[listPreference1.findIndexOfValue(items)]);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        Settings();
        super.onResume();
    }
}