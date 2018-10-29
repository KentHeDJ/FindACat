package edu.gwu.findacat.activity

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import edu.gwu.findacat.*
import edu.gwu.findacat.R.id.recycle_view
import edu.gwu.findacat.R.string.name
import edu.gwu.findacat.activity.PetActivity.Companion.CAT_DATA_KEY
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet.*
import android.location.Geocoder
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import org.jetbrains.anko.toast
import java.util.*


class PetActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener, PetsAdapter.OnItemClickListener, LocationDetector.LocationListener {

    private val TAG="PetAcitivity"
    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var persistenceManager: PersistenceManager
    private lateinit var locationDetector: LocationDetector
    private var flag = 0
    var zipUsed = 22202


    companion object {
        val CAT_DATA_KEY = "catData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        setSupportActionBar(pet_toolbar)

        locationDetector = LocationDetector(this)
        locationDetector.locationListener = this

        persistenceManager = PersistenceManager(this)

        flag = intent.getIntExtra(MenuActivity.FLAG_KEY, 0)
        if (flag == 1) {
            supportActionBar?.title = "Favorite Cats"

            petsAdapter = PetsAdapter(persistenceManager.fetchFavorites(), this)
            recycle_view.layoutManager = GridLayoutManager(this, 2)
            recycle_view.adapter = petsAdapter
        }
        else {

            supportActionBar?.title = "Nearby Cats"

            locationDetector.detectLocation()

            petSearchManager = PetSearchManager()
            petSearchManager.petSearchCompletionListener = this
            petSearchManager.searchPets(zipUsed)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_petactivity, menu)

        return super.onCreateOptionsMenu(menu)
    }

    fun useZipButtonPressed(item: MenuItem) {

        val alert = AlertDialog.Builder(this)
        var editTextZip: EditText?=null

        // Builder
        with (alert) {
            setTitle("Zip Code")

            setMessage("Enter your zip")

            // Add any  input field here
            editTextZip=EditText(context)
            editTextZip!!.inputType = InputType.TYPE_CLASS_NUMBER

            setPositiveButton("OK") {
                dialog, whichButton ->

                var zipString = editTextZip!!.text.toString()
                zipUsed = zipString.toInt()

                petSearchManager = PetSearchManager()
                petSearchManager.petSearchCompletionListener = this@PetActivity
                petSearchManager.searchPets(zipUsed)

                dialog.dismiss()

            }

            setNegativeButton("NO") {
                dialog, whichButton ->
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()
        dialog.setView(editTextZip)
        dialog.show()



    }

    override fun petsLoaded(petItems: List<PetItem>) {
        petsAdapter = PetsAdapter(petItems, this)
        recycle_view.layoutManager = GridLayoutManager(this, 2)
        recycle_view.adapter = petsAdapter
    }

    override fun petsNotLoaded() {
    }

    override fun onItemClick(petItem: PetItem, catImageView: View) {
        val detailIntent = Intent(this@PetActivity, PetDetailActivity::class.java)
        detailIntent.putExtra(CAT_DATA_KEY, petItem)
        startActivity(detailIntent)
    }

    override fun locationFound(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val zip = addresses[0].postalCode.toInt()
        zipUsed = zip

        petSearchManager = PetSearchManager()
        petSearchManager.petSearchCompletionListener = this
        petSearchManager.searchPets(zipUsed)
    }

    override fun locationNotFound(reason: LocationDetector.FailureReason) {
        when (reason) {
            //LocationDetector.FailureReason.TIMEOUT -> toast(getString(R.string.location_not_found))
            LocationDetector.FailureReason.NO_PERMISSION -> toast(getString(R.string.no_location_permission))
        }

    }
}
