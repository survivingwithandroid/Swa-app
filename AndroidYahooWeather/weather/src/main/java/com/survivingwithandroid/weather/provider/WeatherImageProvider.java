package com.survivingwithandroid.weather.provider;


import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

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
 // We retrieve the image from YAHOO! Weather, but you can use any kind of image provider you like

public class WeatherImageProvider implements IWeatherImageProvider {
    private static final String YAHOO_IMG_URL = "http://l.yimg.com/a/i/us/we/52/";

    @Override
    public Bitmap getImage(int code, RequestQueue requestQueue, final WeatherImageListener listener) {
        String imageURL = YAHOO_IMG_URL + code + ".gif";
        ImageRequest ir = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                if (listener != null)
                    listener.onImageReady(response);
            }
        }, 0, 0, null, null);

        requestQueue.add(ir);
        return null;
    }
}
