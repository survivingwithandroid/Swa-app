package com.survivingwithandroid.weather.model;
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
public class Weather {

    public String imageUrl;

    public Condition condition = new Condition();
    public Wind wind = new Wind();
    public Atmosphere atmosphere = new Atmosphere();
    public Forecast forecast = new Forecast();
    public Location location = new Location();
    public Astronomy astronomy = new Astronomy();
    public Units units = new Units();

    public String lastUpdate;

    public class Condition {
        public  String description;
        public  int code;
        public  String date;
        public  int temp;
    }

    public  class Forecast {
        public  int tempMin;
        public  int tempMax;
        public  String description;
        public  int code;
    }

    public static class Atmosphere {
        public  int humidity;
        public  float visibility;
        public  float pressure;
        public  int rising;
    }

    public class Wind {
        public  int chill;
        public  int direction;
        public  int speed;
    }

    public class Units {
        public String speed;
        public String distance;
        public String pressure;
        public String temperature;
    }

    public class Location {
        public String name;
        public String region;
        public String country;
    }

    public class Astronomy {
        public String sunRise;
        public String sunSet;
    }

}
