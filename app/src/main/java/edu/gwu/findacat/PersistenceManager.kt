package edu.gwu.findacat

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import edu.gwu.findacat.R.string.name
import edu.gwu.findacat.model.FavoriteCat
import edu.gwu.trivia.model.generated.petfinder.PetItem
import java.io.IOException

class PersistenceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveFavorites(petItem: PetItem) {
        val petItems = fetchFavorites().toMutableList()
        petItems.add(petItem)

        val editor = sharedPreferences.edit()

        //convert a list of favoriteCats into a JSON string
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, PetItem::class.java)
        val jsonAdapter = moshi.adapter<List<PetItem>>(listType)
        val jsonString = jsonAdapter.toJson(petItems)

        editor.putString("FAVORITECATS", jsonString)

        editor.apply()

    }

    fun fetchFavorites(): List<PetItem> {

        val jsonString = sharedPreferences.getString("FAVORITECATS", null)

        if (jsonString == null) {
            return arrayListOf<PetItem>()
        }
        else {
            val listType = Types.newParameterizedType(List::class.java, PetItem::class.java)
            val moshi = Moshi.Builder()
                    .build()
            val jsonAdapter = moshi.adapter<List<PetItem>>(listType)

            var petItems:List<PetItem>? = emptyList<PetItem>()
            try {
                petItems = jsonAdapter.fromJson(jsonString)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message)
            }

            if(petItems != null) {
                return petItems
            }
            else {
                return  emptyList<PetItem>()
            }
        }

    }

    fun favoriteCat(): List<PetItem>? {
        val petItems = fetchFavorites()

        return petItems
    }
}