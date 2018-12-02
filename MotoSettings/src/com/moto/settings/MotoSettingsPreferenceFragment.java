/*
 * Copyright (C) 2015-2016 The CyanogenMod Project
 * Copyright (C) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moto.settings;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuItem;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import com.moto.settings.utils.FileUtils;
import com.moto.settings.Constants;

public class MotoSettingsPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.thermal);
        addPreferencesFromResource(R.xml.display);
    }

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);
        // Initialize node preferences
        for (String pref : Constants.sBooleanNodePreferenceMap.keySet()) {
            SwitchPreference b = (SwitchPreference) findPreference(pref);

            if (b == null) continue;

            b.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String node = Constants.sBooleanNodePreferenceMap.get(preference.getKey());
                    if (!TextUtils.isEmpty(node)) {
                        Boolean value = (Boolean) newValue;
                        FileUtils.writeLine(node, value ? "1" : "0");

                        return true;
                    }

                    return false;
                }
            });

            String node = Constants.sBooleanNodePreferenceMap.get(pref);
            if (!node.isEmpty()) {
                if (new File(node).exists()) {
                    String curNodeValue = FileUtils.readOneLine(node);
                    b.setChecked(curNodeValue.equals("1"));
                } else {
                    b.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();

            return true;
        }

        return false;
    }
}
