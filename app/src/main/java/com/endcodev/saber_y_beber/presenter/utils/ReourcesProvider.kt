package com.endcodev.saber_y_beber

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getString(@StringRes stringResId: Int, value : Int): String {
        return context.getString(stringResId, value)
    }

    fun getString(@StringRes stringResId: Int, value : String): String {
        return context.getString(stringResId, value)
    }
}