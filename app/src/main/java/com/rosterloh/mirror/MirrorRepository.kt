package com.rosterloh.mirror

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class MirrorRepository private constructor(
        private val apiKey: String
){

    val darkSkyService: DarkSkyService

    init {
        val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                })
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.darksky.net")
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        darkSkyService = retrofit.create(DarkSkyService::class.java)
    }

    fun getWeather(): LiveData<WeatherResult> {

        val result = MutableLiveData<WeatherResult>()

        launch {
            try {
                val request = darkSkyService.getForecast(apiKey, "51.5200982,-0.0902459", "si")
                val response = request.await()
                if (response.isSuccessful) {
                    result.postValue(response.body())
                } else {
                    //result.postValue(WeatherResult.Error(response.code(), response.message()))
                    Timber.e("Request failed: ${response.errorBody()}")
                }
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }

        return result
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: MirrorRepository? = null

        fun getInstance(apiKey: String) =
                instance ?: synchronized(this) {
                    instance ?: MirrorRepository(apiKey).also { instance = it }
                }
    }
}
