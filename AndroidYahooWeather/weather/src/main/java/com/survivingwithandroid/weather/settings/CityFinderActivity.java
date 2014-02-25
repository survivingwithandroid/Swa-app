package com.survivingwithandroid.weather.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.survivingwithandroid.weather.R;
import com.survivingwithandroid.weather.model.CityResult;
import com.survivingwithandroid.weather.provider.YahooClient;

import java.util.ArrayList;
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
public class CityFinderActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cityfinder_layout);
        AutoCompleteTextView edt = (AutoCompleteTextView) this.findViewById(R.id.edtCity);
        CityAdapter adpt = new CityAdapter(this, null);
        edt.setAdapter(adpt);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        edt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityResult result = (CityResult) parent.getItemAtPosition(position);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(CityFinderActivity.this);
                //Log.d("SwA", "WOEID [" + result.getWoeid() + "]");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("woeid", result.getWoeid());
                editor.putString("cityName", result.getCityName());
                editor.putString("country", result.getCountry());
                editor.commit();
                NavUtils.navigateUpFromSameTask(CityFinderActivity.this);
            }
        });

    }

    private class CityAdapter extends ArrayAdapter<CityResult> implements Filterable {

        private Context ctx;
        private List<CityResult> cityList = new ArrayList<CityResult>();

        public CityAdapter(Context ctx, List<CityResult> cityList) {
            super(ctx, R.layout.cityresult_layout, cityList);
            this.cityList = cityList;
            this.ctx = ctx;
        }


        @Override
        public CityResult getItem(int position) {
            if (cityList != null)
                return cityList.get(position);

            return null;
        }

        @Override
        public int getCount() {
            if (cityList != null)
                return cityList.size();

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;

            if (result == null) {
                LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = inf.inflate(R.layout.cityresult_layout, parent, false);

            }

            TextView tv = (TextView) result.findViewById(R.id.txtCityName);
            tv.setText(cityList.get(position).getCityName() + "," + cityList.get(position).getCountry());

            return result;
        }

        @Override
        public long getItemId(int position) {
            if (cityList != null)
                return cityList.get(position).hashCode();

            return 0;
        }

        @Override
        public Filter getFilter() {
            Filter cityFilter = new Filter() {


                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if (constraint == null || constraint.length() < 2)
                        return results;

                    List<CityResult> cityResultList = YahooClient.getCityList(constraint.toString());
                    results.values = cityResultList;
                    results.count = cityResultList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    cityList = (List) results.values;
                    notifyDataSetChanged();
                }
            };

            return cityFilter;
        }
    }


}
