package com.survivingwithandroid.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.survivingwithandroid.weather.billing.util.IabHelper;
import com.survivingwithandroid.weather.billing.util.IabResult;
import com.survivingwithandroid.weather.billing.util.SwABillingUtil;
import com.survivingwithandroid.weather.model.Weather;
import com.survivingwithandroid.weather.provider.IWeatherImageProvider;
import com.survivingwithandroid.weather.provider.WeatherImageProvider;
import com.survivingwithandroid.weather.provider.YahooClient;
import com.survivingwithandroid.weather.settings.WeatherPreferenceActivity;

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
public class MainActivity extends Activity {

    private RequestQueue requestQueue;
    private SharedPreferences prefs;
    private MenuItem refreshItem;
    TextView tvDescrWeather;
    TextView tvLocation;
    TextView tvTemperature;
    TextView tvTempUnit;
    TextView tvTempMin;
    TextView tvTempMax;
    TextView tvHum;
    TextView tvWindSpeed;
    TextView tvWindDeg;
    TextView tvPress;
    TextView tvPressStatus;
    TextView tvVisibility;
    ImageView weatherImage;
    TextView tvSunrise;
    TextView tvSunset;

    private IabHelper mHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SwA", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        tvDescrWeather = (TextView) findViewById(R.id.descrWeather);
        tvLocation = (TextView) findViewById(R.id.location);
        tvTemperature = (TextView) findViewById(R.id.temp);
        tvTempUnit = (TextView) findViewById(R.id.tempUnit);
        tvTempMin = (TextView) findViewById(R.id.tempMin);
        tvTempMax = (TextView) findViewById(R.id.tempMax);
        tvHum = (TextView) findViewById(R.id.humidity);
        tvWindSpeed = (TextView) findViewById(R.id.windSpeed);
        tvWindDeg = (TextView) findViewById(R.id.windDeg);
        tvPress = (TextView) findViewById(R.id.pressure);
        tvPressStatus = (TextView) findViewById(R.id.pressureStat);
        tvVisibility = (TextView) findViewById(R.id.visibility);
        weatherImage = (ImageView) findViewById(R.id.imgWeather);
        tvSunrise = (TextView) findViewById(R.id.sunrise);
        tvSunset = (TextView) findViewById(R.id.sunset);

        String base64EncodedPublicKey= SwABillingUtil.KEY;
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                //Log.d("SwA", "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.

                    Toast.makeText(MainActivity.this, "Billing error ["+result.getMessage()+"]", Toast.LENGTH_LONG).show();
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
               // Log.d("SwA", "Setup successful.");
            }
        });

        refreshData();

    }



    private void refreshData() {

        if (prefs == null)
            return ;


        String woeid = prefs.getString("woeid", null);
        //Log.d("SwA", "WOEID ["+woeid+"]");
        if (woeid != null) {
            String loc = prefs.getString("cityName", null) + "," + prefs.getString("country", null);
            String unit = prefs.getString("swa_temp_unit", null);
            handleProgressBar(true);

            YahooClient.getWeather(woeid, unit, requestQueue, new YahooClient.WeatherClientListener() {
                @Override
                public void onWeatherResponse(Weather weather) {
                    //Log.d("SwA", "Weather ["+weather+"] - Cond ["+weather.condition+"] - Temp ["+weather.condition.temp+"]");
                    int code = weather.condition.code;

                    tvDescrWeather.setText(weather.condition.description);
                    tvLocation.setText(weather.location.name + "," + weather.location.region + "," + weather.location.country);
                    // Temperature data
                    tvTemperature.setText("" + weather.condition.temp);

                    int resId = getResource(weather.units.temperature, weather.condition.temp);
                    ( (TextView) findViewById(R.id.lineTxt)).setBackgroundResource(resId);

                    tvTempUnit.setText(weather.units.temperature);
                    tvTempMin.setText("" + weather.forecast.tempMin + " " + weather.units.temperature);
                    tvTempMax.setText("" + weather.forecast.tempMax + " " + weather.units.temperature);

                    // Humidity data
                    tvHum.setText("" + weather.atmosphere.humidity + " %");

                    // Wind data
                    tvWindSpeed.setText("" + weather.wind.speed + " " + weather.units.speed);
                    tvWindDeg.setText("" + weather.wind.direction + "°");

                    // Pressure data
                    tvPress.setText("" + weather.atmosphere.pressure + " " + weather.units.pressure);
                    int status = weather.atmosphere.rising;
                    String iconStat = "";
                    switch (status) {
                        case 0 :
                            iconStat = "-";
                            break;
                        case 1 :
                            iconStat = "+";
                            break;
                        case 2 :
                            iconStat = "-";
                            break;
                    };
                    tvPressStatus.setText(iconStat);

                    // Visibility
                    tvVisibility.setText("" + weather.atmosphere.visibility + " " + weather.units.distance);

                    // Astronomy
                    tvSunrise.setText(weather.astronomy.sunRise);
                    tvSunset.setText(weather.astronomy.sunSet);

                    // Update

                    IWeatherImageProvider provider = new WeatherImageProvider();
                    provider.getImage(code, requestQueue, new IWeatherImageProvider.WeatherImageListener() {
                        @Override
                        public void onImageReady(Bitmap image) {
                            weatherImage.setImageBitmap(image);
                        }
                    });
                    handleProgressBar(false);
                }
            });


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("SwA", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
           // Log.d("SwA", "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent();
            i.setClass(this, WeatherPreferenceActivity.class);
            startActivity(i);
        }
        else if (id == R.id.action_refresh) {
            refreshItem = item;
            refreshData();
        }
        else if (id == R.id.action_share) {
            String playStoreLink = "https://play.google.com/store/apps/details?id=" +
                    getPackageName();

            String msg = getResources().getString(R.string.share_msg) + playStoreLink;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if (id == R.id.action_donate) {
            SwABillingUtil.showDonateDialog(this, mHelper, this);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
              mHelper = null;
    }



    private void handleProgressBar(boolean visible) {
        if (refreshItem == null)
            return ;

        if (visible)
            refreshItem.setActionView(R.layout.progress_bar);
        else
            refreshItem.setActionView(null);
    }


    private float convertToC(String unit, float val) {
        if (unit.equalsIgnoreCase("°C"))
            return val;

        return (float) ((val - 32) / 1.8);
    }

    private int getResource(String unit, float val) {
        float temp = convertToC(unit, val);
        Log.d("SwA", "Temp ["+temp+"]");
        int resId = 0;
        if (temp < 10)
            resId = R.drawable.line_shape_blue;
        else if (temp >= 10 && temp <=24)
            resId = R.drawable.line_shape_green;
        else if (temp > 25)
            resId = R.drawable.line_shape_red;

        return resId;

    }




}
