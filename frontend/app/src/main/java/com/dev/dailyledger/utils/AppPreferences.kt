package com.dev.dailyledger.utils

import android.content.Context
import android.content.SharedPreferences
import com.dev.dailyledger.utils.Constants.APP_PREFERENCES

object AppPreferences {
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.applicationContext.getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
        }
    }

    fun saveDataInSharePreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getDataFromSharePreference(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun removeDataSharePreference(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearSharePreferences() {
        sharedPreferences.edit().clear().apply()
    }
}
