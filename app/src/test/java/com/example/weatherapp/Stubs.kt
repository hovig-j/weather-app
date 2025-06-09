package com.example.weatherapp

import com.example.weatherapp.data.Clouds
import com.example.weatherapp.data.Summary
import com.example.weatherapp.data.Temperatures
import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.Wind

object Stubs {

    val weatherJsonStub = "{\"coord\":{\"lon\":4.7009,\"lat\":50.8796},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":16.29,\"feels_like\":15.28,\"temp_min\":15.45,\"temp_max\":17.08,\"pressure\":1019,\"humidity\":50,\"sea_level\":1019,\"grnd_level\":1013},\"visibility\":10000,\"wind\":{\"speed\":7.72,\"deg\":270},\"clouds\":{\"all\":75},\"dt\":1749400682,\"sys\":{\"type\":2,\"id\":2097591,\"country\":\"BE\",\"sunrise\":1749353325,\"sunset\":1749412322},\"timezone\":7200,\"id\":2792482,\"name\":\"Leuven\",\"cod\":200}"

    val weatherStub = Weather(
        dt = 1749400682L,
        temperatures = Temperatures(
            temperature = 16.29,
            minTemperature = 15.45,
            maxTemperature = 17.08,
            feelsLike = 15.28
        ),
        summary = listOf(
            Summary(
                id = 803,
                main = "Clouds",
                description = "broken clouds",
                icon = "04d"
            )
        ),
        clouds = Clouds(cloudiness = 75),
        wind = Wind(speed = 7.72),
        rain = null,
        precipitation = null
    )

    val forecastJsonStub = "{\"cod\":\"200\",\"message\":0,\"cnt\":3,\"list\":[{\"dt\":1749481200,\"main\":{\"temp\":19.1,\"feels_like\":18.53,\"temp_min\":19.1,\"temp_max\":19.34,\"pressure\":1021,\"sea_level\":1021,\"grnd_level\":1017,\"humidity\":56,\"temp_kf\":-0.24},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":90},\"wind\":{\"speed\":3.77,\"deg\":239,\"gust\":6.63},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2025-06-09 15:00:00\"},{\"dt\":1749492000,\"main\":{\"temp\":18.17,\"feels_like\":17.61,\"temp_min\":17.76,\"temp_max\":18.17,\"pressure\":1020,\"sea_level\":1020,\"grnd_level\":1015,\"humidity\":60,\"temp_kf\":0.41},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":95},\"wind\":{\"speed\":3.42,\"deg\":250,\"gust\":6.23},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2025-06-09 18:00:00\"},{\"dt\":1749502800,\"main\":{\"temp\":16.39,\"feels_like\":16.04,\"temp_min\":16.39,\"temp_max\":16.39,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":1015,\"humidity\":75,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":4.1,\"deg\":230,\"gust\":8.78},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2025-06-09 21:00:00\"}],\"city\":{\"id\":2643743,\"name\":\"London\",\"coord\":{\"lat\":51.5085,\"lon\":-0.1257},\"country\":\"GB\",\"population\":1000000,\"timezone\":3600,\"sunrise\":1749440658,\"sunset\":1749500129}}"

    val forecastStub = listOf(
        Weather(
            dt = 1749481200,
            temperatures = Temperatures(
                temperature = 19.1,
                minTemperature = 19.1,
                maxTemperature = 19.34,
                feelsLike = 18.53
            ),
            summary = listOf(
                Summary(
                    id = 804,
                    main = "Clouds",
                    description = "overcast clouds",
                    icon = "04d"
                )
            ),
            clouds = Clouds(cloudiness = 90),
            wind = Wind(speed = 3.77),
            rain = null,
            precipitation = 0.0
        ),
        Weather(
            dt = 1749492000,
            temperatures = Temperatures(
                temperature = 18.17,
                minTemperature = 17.76,
                maxTemperature = 18.17,
                feelsLike = 17.61
            ),
            summary = listOf(
                Summary(
                    id = 804,
                    main = "Clouds",
                    description = "overcast clouds",
                    icon = "04d"
                )
            ),
            clouds = Clouds(cloudiness = 95),
            wind = Wind(speed = 3.42),
            rain = null,
            precipitation = 0.0
        ),
        Weather(
            dt = 1749502800,
            temperatures = Temperatures(
                temperature = 16.39,
                minTemperature = 16.39,
                maxTemperature = 16.39,
                feelsLike = 16.04
            ),
            summary = listOf(
                Summary(
                    id = 804,
                    main = "Clouds",
                    description = "overcast clouds",
                    icon = "04n"
                )
            ),
            clouds = Clouds(cloudiness = 100),
            wind = Wind(speed = 4.1),
            rain = null,
            precipitation = 0.0
        )
    )

}