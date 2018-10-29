package edu.gwu.findacat.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import edu.gwu.findacat.CatFactFetcher
import edu.gwu.findacat.PersistenceManager
import edu.gwu.findacat.R
import edu.gwu.findacat.model.FavoriteCat
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MenuActivity : AppCompatActivity(), CatFactFetcher.FactFetchedCompletionListener {
    private val TAG = "MenuActivity"
    private lateinit var catFactFetcher: CatFactFetcher
    private lateinit var persistenceManager: PersistenceManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 7

    companion object {
        val FLAG_KEY = "isFavorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        catFactFetcher = CatFactFetcher(this, textView = text_view)
        catFactFetcher.factFetchedCompletionListener = this
        catFactFetcher.fetchFact()

        persistenceManager = PersistenceManager(this)


        find_a_cat_button.setOnClickListener {
            Log.d(TAG, "Find a cat button tapped")

            loadCatData()
        }

        favorite_cats_button.setOnClickListener {
            Log.d(TAG, "favorite a cat button tapped")

            loadFavorite()
        }

        requestPermissionIfNecessary()
    }

    private fun requestPermissionIfNecessary() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                toast(R.string.permissions_granted)
            }
            else {
                toast(R.string.permissions_denied)
            }
        }
    }

    private fun loadCatData() {
        doAsync {
            activityUiThread {
                val intent = Intent(this@MenuActivity, PetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loadFavorite() {
        doAsync {
            val petItem: List<PetItem> = persistenceManager.fetchFavorites()
            val isFavorite = 1
            activityUiThread {
                val intent = Intent(this@MenuActivity, PetActivity::class.java)
                intent.putExtra(FLAG_KEY, isFavorite)
                //intent.putExtra("FAVORITECAT", petItem)
                startActivity(intent)
            }
        }
    }

    override fun factFetched(fact: String?) {
        text_view.text = "Cat Fact:\n ${fact}"
    }

    override fun factNotFetched() {

    }
}
