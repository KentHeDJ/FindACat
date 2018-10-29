package edu.gwu.findacat.activity

import android.content.Intent
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
import edu.gwu.findacat.PersistenceManager
import edu.gwu.findacat.PetSearchManager
import edu.gwu.findacat.PetsAdapter
import edu.gwu.findacat.R
import edu.gwu.findacat.R.id.recycle_view
import edu.gwu.findacat.R.string.name
import edu.gwu.findacat.activity.PetActivity.Companion.CAT_DATA_KEY
import edu.gwu.trivia.model.generated.petfinder.PetItem
import kotlinx.android.synthetic.main.activity_pet.*

class PetActivity : AppCompatActivity(), PetSearchManager.PetSearchCompletionListener, PetsAdapter.OnItemClickListener {

    private val TAG="PetAcitivity"
    private lateinit var petSearchManager: PetSearchManager
    private lateinit var petsAdapter: PetsAdapter
    private lateinit var persistenceManager: PersistenceManager
    private var flag = 0
    var zip = 90210

    companion object {
        val CAT_DATA_KEY = "catData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        setSupportActionBar(pet_toolbar)
        supportActionBar?.title = "Nearby Cats"

        persistenceManager = PersistenceManager(this)

        flag = intent.getIntExtra(MenuActivity.FLAG_KEY, 0)
        if (flag == 1) {
            petsAdapter = PetsAdapter(persistenceManager.fetchFavorites(), this)
            recycle_view.layoutManager = LinearLayoutManager(this)
            recycle_view.adapter = petsAdapter
        }
        else {

            petSearchManager = PetSearchManager()
            petSearchManager.petSearchCompletionListener = this
            petSearchManager.searchPets(zip)
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
                //showMessage("display the game score or anything!")
                var zipString = editTextZip!!.text.toString()
                zip = zipString.toInt()

                petSearchManager = PetSearchManager()
                petSearchManager.petSearchCompletionListener = this@PetActivity
                petSearchManager.searchPets(zip)

                dialog.dismiss()

            }

            setNegativeButton("NO") {
                dialog, whichButton ->
                //showMessage("Close the game or anything!")
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
        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.adapter = petsAdapter
    }

    override fun petsNotLoaded() {
    }

    override fun onItemClick(petItem: PetItem, catImageView: View) {
        val detailIntent = Intent(this@PetActivity, PetDetailActivity::class.java)
        detailIntent.putExtra(CAT_DATA_KEY, petItem)
        startActivity(detailIntent)
    }
}
