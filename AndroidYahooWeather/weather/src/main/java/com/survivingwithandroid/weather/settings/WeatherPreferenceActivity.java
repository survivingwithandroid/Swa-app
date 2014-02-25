package com.survivingwithandroid.weather.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.survivingwithandroid.weather.R;
import com.survivingwithandroid.weather.model.CityResult;

import java.util.List;
/*
 * Copyright (C) 2014 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
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
public class WeatherPreferenceActivity extends PreferenceActivity  {


    @Override
    public void onCreate(Bundle Bundle) {
        super.onCreate(Bundle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        String action = getIntent().getAction();

        addPreferencesFromResource(R.xml.weather_prefs);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // We set the current values in the description
        Preference prefLocation = getPreferenceScreen().findPreference("swa_loc");
        Preference prefTemp = getPreferenceScreen().findPreference("swa_temp_unit");

        prefLocation.setSummary(getResources().getText(R.string.summary_loc) + " " + prefs.getString("cityName", null) + "," + prefs.getString("country", null));

        String unit =  prefs.getString("swa_temp_unit", null) != null ? "°" + prefs.getString("swa_temp_unit", null).toUpperCase() : "";
        prefTemp.setSummary(getResources().getText(R.string.summary_temp) + " " + unit);


        //getPreferenceScreen().findPreference("swa_temp_unit").setDefaultValue(valTemp);
        //getPreferenceScreen().findPreference("swa_system").setDefaultValue(unitSys);


    }






}
