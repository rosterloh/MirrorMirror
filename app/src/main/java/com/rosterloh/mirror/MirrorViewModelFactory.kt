package com.rosterloh.mirror

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MirrorViewModelFactory(
        private val repository: MirrorRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MirrorViewModel(repository) as T
}