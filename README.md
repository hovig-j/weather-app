# 🌤️ Weather App

An Android weather application that displays the current weather and a 5-day forecast for any city entered by the user — or for the user's current location.

Written in **Kotlin**, built using **Jetpack Compose**,
the app leverages a clean architecture with **ViewModel**, modular structure,
and industry-standard libraries like **Kotlin Coroutines**, **Retrofit**, **Moshi**, and **Coil**.

---

## 📱 Features

- 🔍 Search weather by city name
- 📍 Get current weather & forecast using device location
- 📊 Displays detailed forecast including temperatures, cloud, rain, etc.
- 🌅 Daily forecast with icons and descriptions
- ⚙️ Asynchronous data handling with Kotlin Coroutines
- 📦 Clean Architecture (separated module data provider)
- ✅ Unit tested components
- 🌐 Data sourced from [OpenWeatherMap.org](https://openweathermap.org/)

---

## 🛠️ Tech Stack

| Category       | Library/Tool                                                                                |
|----------------|---------------------------------------------------------------------------------------------|
| Language       | [Kotlin](https://kotlinlang.org/)                                                           |
| UI             | [Jetpack Compose](https://developer.android.com/jetpack/compose)                            |
| Architecture   | [MVVM with ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) |
| Organization   | [Modular](https://developer.android.com/topic/modularization)                               | 
| Async Handling | [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)                   |
| Networking     | [Retrofit](https://square.github.io/retrofit/)                                              |
| JSON Parsing   | [Moshi](https://github.com/square/moshi)                                                    |
| Image Loading  | [Coil](https://coil-kt.github.io/coil/)                                                     |
| Testing        | JUnit, Mockito, Coroutine Test, etc.                                                        |

---

## 🔧 Setup & Run

1. Clone the repository:
    ```bash
       git clone https://github.com/hovig-j/weather-app.git
       cd weather-app
    ```

2. Add your OpenWeatherMap API key in `gradle.properties`:
   ```groovy
    OPEN_WEATHER_API_KEY=your_api_key_here
   ```

3. Open the project in Android Studio, sync Gradle, and run the app on an emulator or device.

## 🙏 Acknowledgments

- Weather data from [OpenWeatherMap](https://openweathermap.org/).
- Some UI design and this README were generated with the help of [ChatGPT](https://openai.com/chatgpt) to improve development speed.
