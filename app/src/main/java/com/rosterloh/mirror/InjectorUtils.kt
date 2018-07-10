package com.rosterloh.mirror

import android.content.Context

object InjectorUtils {

    private fun getMirrorRepository(context: Context): MirrorRepository {
        return MirrorRepository.getInstance(context.getString(R.string.dark_sky_api_key))
    }

    fun provideMirrorViewModelFactory(
            context: Context
    ): MirrorViewModelFactory {
        val repository = getMirrorRepository(context)
        return MirrorViewModelFactory(repository)
    }
}