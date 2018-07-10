package com.rosterloh.mirror

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MirrorViewModel(
        private val mirrorRepository: MirrorRepository
): ViewModel() {

    val weather = MutableLiveData<WeatherModel>()

    init {
        mirrorRepository.getWeather().observeForever {
            if (it != null) weather.value = it.currently
        }
    }
}
