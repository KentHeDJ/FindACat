package edu.gwu.findacat

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PersistenceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveFavorites() {

    }

    fun fetchFavorites() {

    }
}